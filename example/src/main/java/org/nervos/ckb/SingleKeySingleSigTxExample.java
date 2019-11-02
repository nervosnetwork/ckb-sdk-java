package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class SingleKeySingleSigTxExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static List<String> ReceiveAddresses;

  static {
    api = new Api(NODE_URL, false);
    ReceiveAddresses =
        Arrays.asList(
            "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g",
            "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek");
  }

  public static void main(String[] args) throws Exception {
    String minerPrivateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
    String minerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";
    List<Receiver> receivers =
        Arrays.asList(
            new Receiver(ReceiveAddresses.get(0), new BigInteger("8000").multiply(UnitCKB)),
            new Receiver(ReceiveAddresses.get(1), new BigInteger("9000").multiply(UnitCKB)));
    BigInteger txFee = BigInteger.valueOf(10000);

    System.out.println(
        "Before transfer, miner's balance: "
            + getBalance(minerAddress).divide(UnitCKB).toString(10)
            + " CKB");

    System.out.println(
        "Before transfer, first receiver's balance: "
            + getBalance(ReceiveAddresses.get(0)).divide(UnitCKB).toString(10)
            + " CKB");

    // miner send capacity to three receiver accounts with 800, 900 and 1000 CKB
    String hash = sendCapacity(minerPrivateKey, receivers, minerAddress, txFee);
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

  private static String sendCapacity(
      String privateKey, List<Receiver> receivers, String changeAddress, BigInteger fee)
      throws IOException {
    BigInteger needCapacity = BigInteger.ZERO;
    List<WitnessGroup> witnessGroups = new ArrayList<>();
    for (Receiver receiver : receivers) {
      needCapacity = needCapacity.add(receiver.capacity);
    }

    List<Sender> senders = Collections.singletonList(new Sender(privateKey, needCapacity));
    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    List<CellsWithPrivateKey> cellsWithPrivateKeys = txUtils.collectInputs(senders);
    int startIndex = 0;
    for (CellsWithPrivateKey cellsWithPrivateKey : cellsWithPrivateKeys) {
      txBuilder.addInputs(cellsWithPrivateKey.inputs);
      witnessGroups.add(
          new WitnessGroup(
              NumberUtils.regionToList(startIndex, cellsWithPrivateKey.inputs.size()),
              cellsWithPrivateKey.privateKey));
    }

    txBuilder.addOutputs(txUtils.generateOutputs(receivers, changeAddress, fee));

    SignatureBuilder signBuilder = new SignatureBuilder(txBuilder.buildTx());

    for (WitnessGroup witnessGroup : witnessGroups) {
      signBuilder.addWitnessGroup(witnessGroup);
    }

    return api.sendTransaction(signBuilder.buildTx());
  }
}
