package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellOutput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class SendToMultiSigAddressTxExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static String MultiSigAddress = "ckt1qyqlqn8vsj7r0a5rvya76tey9jd2rdnca8lqh4kcuq";
  private static String MinerPrivateKey =
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
  private static String MinerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";

  static {
    api = new Api(NODE_URL, false);
  }

  public static void main(String[] args) throws Exception {
    List<Receiver> receivers =
        Collections.singletonList(
            new Receiver(MultiSigAddress, new BigInteger("20000").multiply(UnitCKB)));

    System.out.println(
        "Before transfer, miner's balance: " + getBalance().divide(UnitCKB).toString(10) + " CKB");

    System.out.println(
        "Before transfer, multi-sig address balance: "
            + getMultiSigBalance().divide(UnitCKB).toString(10)
            + " CKB");

    // miner send capacity to three receiver accounts with 800, 900 and 1000 CKB
    String hash = sendCapacity(receivers, MinerAddress);
    System.out.println("Transaction hash: " + hash);
    Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds

    System.out.println(
        "After transfer, multi-sig address balance: "
            + getMultiSigBalance().divide(UnitCKB).toString(10)
            + " CKB");
  }

  private static BigInteger getBalance() throws IOException {
    CellCollector cellCollector = new CellCollector(api);
    return cellCollector.getCapacityWithAddress(MinerAddress);
  }

  private static BigInteger getMultiSigBalance() throws IOException {
    CellCollector cellCollector = new CellCollector(api);
    return cellCollector.getCapacityWithAddress(MultiSigAddress);
  }

  private static String sendCapacity(List<Receiver> receivers, String changeAddress)
      throws IOException {
    BigInteger needCapacity = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacity = needCapacity.add(receiver.capacity);
    }

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, changeAddress);

    txBuilder.addOutputs(cellOutputs);

    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(10000);

    // // initial_length = 2 * secp256k1_signature_byte.length
    List<CellsWithAddress> cellsWithAddresses =
        txUtils.collectInputs(
            Collections.singletonList(MinerAddress), cellOutputs, feeRate, Sign.SIGN_LENGTH * 2);

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
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
