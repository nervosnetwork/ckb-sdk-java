package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellOutput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class SingleKeySingleSigTxExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static List<String> ReceiveAddresses;
  private static String MinerPrivateKey =
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
  private static String MinerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";

  static {
    api = new Api(NODE_URL, false);
    ReceiveAddresses =
        Arrays.asList(
            "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g",
            "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek");
  }

  public static void main(String[] args) throws Exception {
    List<Receiver> receivers =
        Arrays.asList(
            new Receiver(ReceiveAddresses.get(0), new BigInteger("8000").multiply(UnitCKB)),
            new Receiver(ReceiveAddresses.get(1), new BigInteger("9000").multiply(UnitCKB)));

    System.out.println(
        "Before transfer, miner's balance: "
            + getBalance(MinerAddress).divide(UnitCKB).toString(10)
            + " CKB");

    System.out.println(
        "Before transfer, first receiver's balance: "
            + getBalance(ReceiveAddresses.get(0)).divide(UnitCKB).toString(10)
            + " CKB");

    // miner send capacity to three receiver accounts with 800, 900 and 1000 CKB
    String hash = sendCapacity(receivers, MinerAddress);
    System.out.println("Transaction hash: " + hash);
    Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds

    System.out.println(
        "After transfer, receiver's balance: "
            + getBalance(ReceiveAddresses.get(0)).divide(UnitCKB).toString(10)
            + " CKB");
  }

  private static BigInteger getBalance(String address) throws IOException {
    CellCollector cellCollector = new CellCollector(api);
    return cellCollector.getCapacityWithAddress(address);
  }

  private static String sendCapacity(List<Receiver> receivers, String changeAddress)
      throws IOException {
    BigInteger needCapacity = BigInteger.ZERO;
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();
    for (Receiver receiver : receivers) {
      needCapacity = needCapacity.add(receiver.capacity);
    }

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, changeAddress);
    txBuilder.addOutputs(cellOutputs);

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(10000);

    List<CellsWithAddress> cellsWithAddresses =
        txUtils.collectInputs(Collections.singletonList(MinerAddress), cellOutputs, feeRate);
    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.EMPTY_LOCK) : new Witness());
      }
      scriptGroupWithPrivateKeysList.add(
          new ScriptGroupWithPrivateKeys(
              new ScriptGroup(NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size())),
              Collections.singletonList(MinerPrivateKey)));
    }

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }

    return api.sendTransaction(signBuilder.buildTx());
  }
}
