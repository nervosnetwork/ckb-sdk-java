package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import org.nervos.ckb.methods.response.CkbTransactionHash;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;
import org.nervos.ckb.transaction.CellGatherer;
import org.nervos.ckb.transaction.Receiver;
import org.nervos.ckb.transaction.Sender;
import org.nervos.ckb.transaction.TxGenerator;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static CKBService ckbService;
  private static List<Account> Accounts;

  static {
    HttpService.setDebug(false);
    ckbService = CKBService.build(new HttpService(NODE_URL));
    Accounts =
        Arrays.asList(
            new Account(
                "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2",
                "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g"),
            new Account(
                "a202386cb9e46cecff9bc14b748b714c713075dd964c2507c8a8900540164959",
                "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek"),
            new Account(
                "89b773ec5cf97b8fd2cf280ab1e37cd658dc28d84bac8f8dda4a8646cc08d266",
                "ckt1qyqxvnycu7tdtyuejn3mmcnl4y09muxz8c3s2ewjd4"));
  }

  public static void main(String[] args) throws Exception {
    String minerPrivateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
    List<Receiver> receivers1 =
        Arrays.asList(
            new Receiver(Accounts.get(0).address, new BigInteger("800").multiply(UnitCKB)),
            new Receiver(Accounts.get(1).address, new BigInteger("900").multiply(UnitCKB)),
            new Receiver(Accounts.get(2).address, new BigInteger("1000").multiply(UnitCKB)));

    System.out.println(
        "Before transfer, first receiver1's balance: "
            + getBalance(Accounts.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");

    // miner send capacity to three receiver1 accounts with 800, 900 and 1000 CKB
    sendCapacityWithSinglePrivateKey(minerPrivateKey, receivers1);
    Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds

    System.out.println(
        "After transfer, first receiver1's balance: "
            + getBalance(Accounts.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");

    List<Sender> senders1 =
        Arrays.asList(
            new Sender(Accounts.get(0).privateKey, new BigInteger("500").multiply(UnitCKB)),
            new Sender(Accounts.get(1).privateKey, new BigInteger("600").multiply(UnitCKB)),
            new Sender(Accounts.get(2).privateKey, new BigInteger("700").multiply(UnitCKB)));
    List<Receiver> receivers2 =
        Arrays.asList(
            new Receiver(
                "ckt1qyqqtdpzfjwq7e667ktjwnv3hngrqkmwyhhqpa8dav",
                new BigInteger("400").multiply(UnitCKB)),
            new Receiver(
                "ckt1qyq9ngn77wagfurp29738apv738dqgrpqpssfhr0l6",
                new BigInteger("500").multiply(UnitCKB)),
            new Receiver(
                "ckt1qyq2pmuxkr0xwx8kp3ya2juryrygf27dregs44skek",
                new BigInteger("600").multiply(UnitCKB)));

    System.out.println(
        "Before transfer, first receiver2's balance: "
            + getBalance(receivers2.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");

    // sender1 accounts send capacity to three receiver2 accounts with 400, 500 and 600 CKB
    sendCapacityWithMultiPrivateKey(senders1, receivers2);
    Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds

    System.out.println(
        "After transfer, receiver2's balance: "
            + getBalance(receivers2.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");
  }

  private static BigInteger getBalance(String address) throws IOException {
    CellGatherer cellGatherer = new CellGatherer(ckbService);
    return cellGatherer.getCapacitiesWithAddress(address);
  }

  private static void sendCapacityWithSinglePrivateKey(String privateKey, List<Receiver> receivers)
      throws IOException {
    TxGenerator txGenerator = new TxGenerator(ckbService);
    Transaction transaction = txGenerator.generateTx(privateKey, receivers);
    CkbTransactionHash ckbTransactionHash = ckbService.sendTransaction(transaction).send();
    if (ckbTransactionHash.error != null) {
      throw new IOException(ckbTransactionHash.error.message);
    }
    System.out.println(
        "Single private key transaction hash: " + ckbTransactionHash.getTransactionHash());
  }

  private static void sendCapacityWithMultiPrivateKey(
      List<Sender> senders, List<Receiver> receivers) throws IOException {
    TxGenerator txGenerator = new TxGenerator(ckbService);
    Transaction transaction = txGenerator.generateTx(senders, receivers);
    CkbTransactionHash ckbTransactionHash = ckbService.sendTransaction(transaction).send();
    if (ckbTransactionHash.error != null) {
      throw new IOException(ckbTransactionHash.error.message);
    }
    System.out.println(
        "Multi private key transaction hash: " + ckbTransactionHash.getTransactionHash());
  }

  static class Account {
    String privateKey;
    String address;

    Account(String privateKey, String address) {
      this.privateKey = privateKey;
      this.address = address;
    }
  }
}
