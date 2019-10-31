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
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Calculator;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class MultiKeySingleSigTxExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static final BigInteger TxFeeConst = new BigInteger("1000");
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
                "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek"),
            new KeyPair(
                "89b773ec5cf97b8fd2cf280ab1e37cd658dc28d84bac8f8dda4a8646cc08d266",
                "ckt1qyqxvnycu7tdtyuejn3mmcnl4y09muxz8c3s2ewjd4"),
            new KeyPair(
                "fec27185a66dd21abb97eeaaeb6bf63fb9c5b7c7966550e6798a78fbaf4197f0",
                "ckt1qyq8n3400g4lw7xs4denyjprpyzaa6z2z5wsl7e2gs"),
            new KeyPair(
                "2cee134faa03a158011dff33b7756e89a0c76ba64006640615be7b483b2935b4",
                "ckt1qyqd4lgpt5auunu6s3wskkwxmdx548wksvcqyq44en"),
            new KeyPair(
                "55b55c7bd177ed837dde45bbde12fc79c12fb8695be258064f40e6d5f65db96c",
                "ckt1qyqrlj6znd3uhvuln5z83epv54xu8pmphzgse5uylq"));
  }

  public static void main(String[] args) throws Exception {
    String minerPrivateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
    String minerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";
    List<Receiver> receivers1 =
        Arrays.asList(
            new Receiver(KeyPairs.get(0).address, new BigInteger("800").multiply(UnitCKB)),
            new Receiver(KeyPairs.get(1).address, new BigInteger("900").multiply(UnitCKB)),
            new Receiver(KeyPairs.get(2).address, new BigInteger("1000").multiply(UnitCKB)));

    System.out.println(
        "Before transfer, miner's balance: "
            + getBalance(minerAddress).divide(UnitCKB).toString(10)
            + " CKB");

    System.out.println(
        "Before transfer, first receiver1's balance: "
            + getBalance(KeyPairs.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");

    // Transaction fee can be calculated by `estimate_fee_rate` rpc or also set a simple number.
    // BigInteger txFee = estimateTransactionFee(minerPrivateKey, receivers1,
    // minerAddress).add(TxFeeConst);
    BigInteger txFee = BigInteger.valueOf(10000);

    // miner send capacity to three receiver1 accounts with 800, 900 and 1000 CKB
    String hash = sendCapacity(minerPrivateKey, receivers1, minerAddress, txFee);
    System.out.println("First transaction hash: " + hash);
    Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds

    System.out.println(
        "After transfer, first receiver1's balance: "
            + getBalance(KeyPairs.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");

    // Second transaction

    List<Sender> senders1 =
        Arrays.asList(
            new Sender(KeyPairs.get(0).privateKey, new BigInteger("500").multiply(UnitCKB)),
            new Sender(KeyPairs.get(1).privateKey, new BigInteger("600").multiply(UnitCKB)),
            new Sender(KeyPairs.get(2).privateKey, new BigInteger("700").multiply(UnitCKB)));
    List<Receiver> receivers2 =
        Arrays.asList(
            new Receiver(KeyPairs.get(3).address, new BigInteger("400").multiply(UnitCKB)),
            new Receiver(KeyPairs.get(4).address, new BigInteger("500").multiply(UnitCKB)),
            new Receiver(KeyPairs.get(5).address, new BigInteger("600").multiply(UnitCKB)));

    String changeAddress = "ckt1qyqfnym6semhw2vzm33fjvk3ngxuf5433l9qz3af8a";

    System.out.println(
        "Before transfer, first receiver2's balance: "
            + getBalance(receivers2.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");

    // Transaction fee can be calculated by `estimate_fee_rate` rpc or also set a simple number.
    // txFee = estimateTransactionFee(senders1, receivers2, changeAddress).add(TxFeeConst);
    txFee = BigInteger.valueOf(10000);

    // sender1 accounts send capacity to three receiver2 accounts with 400, 500 and 600 CKB
    String hash2 = sendCapacity(senders1, receivers2, changeAddress, txFee);
    System.out.println("Second transaction hash: " + hash2);
    Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds

    System.out.println(
        "After transfer, receiver2's balance: "
            + getBalance(receivers2.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");
  }

  private static BigInteger getBalance(String address) throws IOException {
    CellCollector cellCollector = new CellCollector(api);
    return cellCollector.getCapacityWithAddress(address);
  }

  private static String sendCapacity(
      String privateKey, List<Receiver> receivers, String changeAddress, BigInteger fee)
      throws IOException {
    Transaction transaction = generateTx(privateKey, receivers, changeAddress, fee);
    return api.sendTransaction(transaction);
  }

  private static String sendCapacity(
      List<Sender> senders, List<Receiver> receivers, String changeAddress, BigInteger fee)
      throws IOException {
    Transaction transaction = generateTx(senders, receivers, changeAddress, fee);
    return api.sendTransaction(transaction);
  }

  private static BigInteger estimateTransactionFee(
      String privateKey, List<Receiver> receivers, String changeAddress) throws IOException {
    return Calculator.calculateTransactionFee(
        api, generateTx(privateKey, receivers, changeAddress, BigInteger.ZERO), 5);
  }

  private static BigInteger estimateTransactionFee(
      List<Sender> senders, List<Receiver> receivers, String changeAddress) throws IOException {
    return Calculator.calculateTransactionFee(
        api, generateTx(senders, receivers, changeAddress, BigInteger.ZERO), 5);
  }

  private static Transaction generateTx(
      String privateKey, List<Receiver> receivers, String changeAddress, BigInteger fee)
      throws IOException {
    BigInteger needCapacity = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacity = needCapacity.add(receiver.capacity);
    }
    List<Sender> senders = Collections.singletonList(new Sender(privateKey, needCapacity));
    return generateTx(senders, receivers, changeAddress, fee);
  }

  private static Transaction generateTx(
      List<Sender> senders, List<Receiver> receivers, String changeAddress, BigInteger fee)
      throws IOException {
    TransactionBuilder builder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    List<CellsWithLock> cellsWithLocks = txUtils.collectInputs(senders);
    builder.addInputsWithLocks(cellsWithLocks);

    builder.addOutputs(txUtils.generateOutputs(receivers, changeAddress, fee));

    builder.buildTx();

    return builder.getTransaction();
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
