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
import org.nervos.ckb.transaction.TxGenerator;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionClient {

  private static final String NODE_URL = "http://localhost:8114";
  private static CKBService ckbService = CKBService.build(new HttpService(NODE_URL));

  public static void main(String[] args) throws Exception {
    String senderPrivateKey = "0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
    List<Receiver> receivers =
        Arrays.asList(
            new Receiver(
                "ckt1qyqqtdpzfjwq7e667ktjwnv3hngrqkmwyhhqpa8dav", new BigInteger("10000000000")),
            new Receiver(
                "ckt1qyq9ngn77wagfurp29738apv738dqgrpqpssfhr0l6", new BigInteger("12000000000")),
            new Receiver(
                "ckt1qyq2pmuxkr0xwx8kp3ya2juryrygf27dregs44skek", new BigInteger("15000000000")));
    System.out.println("Sender's balance: " + getBalance(senderPrivateKey).toString(10));
    sendCapacity(senderPrivateKey, receivers);
    System.out.println("Sender's balance: " + getBalance(senderPrivateKey).toString(10));
  }

  private static BigInteger getBalance(String privateKey) throws IOException {
    CellGatherer cellGatherer = new CellGatherer(ckbService);
    return cellGatherer.getCapacitiesWithPrivateKey(privateKey);
  }

  private static void sendCapacity(String privateKey, List<Receiver> receivers) throws IOException {
    TxGenerator txGenerator = new TxGenerator(privateKey, ckbService);
    Transaction transaction = txGenerator.generateTx(receivers);
    CkbTransactionHash ckbTransactionHash = ckbService.sendTransaction(transaction).send();
    if (ckbTransactionHash.error != null) {
      throw new IOException(ckbTransactionHash.error.message);
    }
    System.out.println("transaction hash: " + ckbTransactionHash.getTransactionHash());
  }
}
