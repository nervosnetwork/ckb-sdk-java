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
import org.nervos.ckb.utils.Utils;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class SendToMultiSigAddressTxExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static String MultiSigAddress = "ckt1qyqlqn8vsj7r0a5rvya76tey9jd2rdnca8lqh4kcuq";
  private static String TestPrivateKey =
      "d00c06bfd800d27397002dca6fb0993d5ba6399b4238b2f29ee9deb97593d2bc";
  private static String TestAddress = "ckt1qyqvsv5240xeh85wvnau2eky8pwrhh4jr8ts8vyj37";

  static {
    api = new Api(NODE_URL, false);
  }

  public static void main(String[] args) throws Exception {
    List<Receiver> receivers =
        Collections.singletonList(new Receiver(MultiSigAddress, Utils.ckbToShannon(20000)));
    String changeAddress = "ckt1qyqvsv5240xeh85wvnau2eky8pwrhh4jr8ts8vyj37";

    System.out.println("Before transferring, sender's balance: " + getBalance() + " CKB");

    System.out.println(
        "Before transferring, multi-sig address balance: " + getMultiSigBalance() + " CKB");

    System.out.println("Before transferring, change address balance: " + getBalance() + " CKB");

    String hash = sendCapacity(receivers, changeAddress);
    System.out.println("Transaction hash: " + hash);

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);

    System.out.println("After transferring, sender's address balance: " + getBalance() + " CKB");

    System.out.println(
        "After transferring, multi-sig address balance: " + getMultiSigBalance() + " CKB");

    System.out.println("After transferring, change address balance: " + getBalance() + " CKB");
  }

  private static String getBalance() {
    return new CollectUtils(api).getCapacityWithAddress(TestAddress).divide(UnitCKB).toString(10);
  }

  private static String getMultiSigBalance() {
    return new CollectUtils(api)
        .getCapacityWithAddress(MultiSigAddress)
        .divide(UnitCKB)
        .toString(10);
  }

  private static String sendCapacity(List<Receiver> receivers, String changeAddress)
      throws IOException {
    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, changeAddress);
    txBuilder.addOutputs(cellOutputs);

    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    // You can get fee rate by rpc or set a simple number
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    CollectResult collectResult =
        txUtils.collectInputs(
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
