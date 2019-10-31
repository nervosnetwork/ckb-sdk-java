package org.nervos.ckb.example;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.example.transaction.CollectUtils;
import org.nervos.ckb.example.transaction.Receiver;
import org.nervos.ckb.example.transaction.Sender;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.CellCollector;
import org.nervos.ckb.transaction.CellsWithLock;
import org.nervos.ckb.transaction.TransactionBuilder;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class SingleKeySingleSigTxExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static List<KeyPair> KeyPairs;

  static {
    api = new Api(NODE_URL, false);
    KeyPairs =
        Arrays.asList(
            new KeyPair(
                "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2",
                "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g"),
            new KeyPair(
                "a202386cb9e46cecff9bc14b748b714c713075dd964c2507c8a8900540164959",
                "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek"));
  }

  public static void main(String[] args) throws Exception {
    String minerPrivateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
    String minerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";
    List<Receiver> receivers =
        Arrays.asList(
            new Receiver(KeyPairs.get(0).address, new BigInteger("8000").multiply(UnitCKB)),
            new Receiver(KeyPairs.get(1).address, new BigInteger("9000").multiply(UnitCKB)));
    BigInteger txFee = BigInteger.valueOf(10000);

    System.out.println(
        "Before transfer, miner's balance: "
            + getBalance(minerAddress).divide(UnitCKB).toString(10)
            + " CKB");

    System.out.println(
        "Before transfer, first receiver's balance: "
            + getBalance(KeyPairs.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");

    // miner send capacity to three receiver accounts with 800, 900 and 1000 CKB
    String hash = sendCapacity(minerPrivateKey, receivers, minerAddress, txFee);
    System.out.println("Transaction hash: " + hash);
    Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds

    System.out.println(
        "After transfer, receiver's balance: "
            + getBalance(KeyPairs.get(0).address).divide(UnitCKB).toString(10)
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
    for (Receiver receiver : receivers) {
      needCapacity = needCapacity.add(receiver.capacity);
    }

    List<Sender> senders = Collections.singletonList(new Sender(privateKey, needCapacity));
    TransactionBuilder builder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    List<CellsWithLock> cellsWithLocks = txUtils.collectInputs(senders);
    builder.addInputsWithLocks(cellsWithLocks);

    builder.addOutputs(txUtils.generateOutputs(receivers, changeAddress, fee));

    builder.buildTx();

    return api.sendTransaction(builder.getTransaction());
  }

  static class KeyPair {
    String privateKey;
    String address;

    KeyPair(String privateKey, String address) {
      this.privateKey = privateKey;
      this.address = address;
    }
  }
}
