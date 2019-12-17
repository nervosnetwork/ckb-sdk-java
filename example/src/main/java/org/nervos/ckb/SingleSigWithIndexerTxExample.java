package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.utils.Utils;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class SingleSigWithIndexerTxExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static List<String> ReceiveAddresses;
  private static String TestPrivateKey =
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
  private static String TestAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";

  static {
    api = new Api(NODE_URL, false);
    ReceiveAddresses =
        Arrays.asList(
            "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g",
            "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek");
  }

  /**
   * Note: If you want to use indexer to collect cell quickly, you should call index_lock_hash rpc
   * firstly. CKB needs time to execute tagging to live cells, so we suggest that if you want to
   * collect live cells, you should await some time after calling index_lock_hash rpc.
   */
  public static void main(String[] args) throws Exception {

    System.out.println("Call index_lock_hash rpc firstly");

    // Call index_lock_hash rpc firstly before collecting live cells
    // api.indexLockHash(LockUtils.generateLockHashWithAddress(TestAddress), "0x0");
    // Wait some time for ckb to execute tagging to live cells
    // Thread.sleep(20000);

    List<Receiver> receivers =
        Arrays.asList(
            new Receiver(ReceiveAddresses.get(0), Utils.ckbToShannon(8000)),
            new Receiver(ReceiveAddresses.get(1), Utils.ckbToShannon(9000)));

    System.out.println(
        "Before transferring, sender's balance: "
            + getBalance(TestAddress).divide(UnitCKB).toString(10)
            + " CKB");

    String hash = sendCapacity(receivers, TestAddress);
    System.out.println("Transaction hash: " + hash);

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);

    System.out.println(
        "After transferring, sender's balance: "
            + getBalance(TestAddress).divide(UnitCKB).toString(10)
            + " CKB");
  }

  private static BigInteger getBalance(String address) throws Exception {
    CellCollectorWithIndexer cellCollector = new CellCollectorWithIndexer(api);
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
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    // collectInputsWithIndexer method uses indexer rpc to collect cells quickly
    CollectResult collectResult =
        txUtils.collectInputsWithIndexer(
            Collections.singletonList(TestAddress),
            txBuilder.buildTx(),
            feeRate,
            Sign.SIGN_LENGTH * 2);

    // update change cell output capacity after collecting cells
    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    txBuilder.setOutputs(cellOutputs);

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : collectResult.cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
      }
      scriptGroupWithPrivateKeysList.add(
          new ScriptGroupWithPrivateKeys(
              new ScriptGroup(NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size())),
              Collections.singletonList(TestPrivateKey)));
    }

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }

    return api.sendTransaction(signBuilder.buildTx());
  }
}
