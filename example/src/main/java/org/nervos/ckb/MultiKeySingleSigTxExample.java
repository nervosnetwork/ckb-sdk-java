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
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class MultiKeySingleSigTxExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static List<String> PrivateKeys;
  private static List<String> Addresses;

  static {
    api = new Api(NODE_URL, false);
    PrivateKeys =
        Arrays.asList(
            "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2",
            "a202386cb9e46cecff9bc14b748b714c713075dd964c2507c8a8900540164959",
            "89b773ec5cf97b8fd2cf280ab1e37cd658dc28d84bac8f8dda4a8646cc08d266",
            "fec27185a66dd21abb97eeaaeb6bf63fb9c5b7c7966550e6798a78fbaf4197f0",
            "2cee134faa03a158011dff33b7756e89a0c76ba64006640615be7b483b2935b4",
            "55b55c7bd177ed837dde45bbde12fc79c12fb8695be258064f40e6d5f65db96c",
            "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    Addresses =
        Arrays.asList(
            "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g",
            "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek",
            "ckt1qyqxvnycu7tdtyuejn3mmcnl4y09muxz8c3s2ewjd4",
            "ckt1qyq8n3400g4lw7xs4denyjprpyzaa6z2z5wsl7e2gs",
            "ckt1qyqd4lgpt5auunu6s3wskkwxmdx548wksvcqyq44en",
            "ckt1qyqrlj6znd3uhvuln5z83epv54xu8pmphzgse5uylq",
            "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83");
  }

  public static void main(String[] args) throws Exception {
    String minerPrivateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
    String minerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";
    List<Receiver> receivers1 =
        Arrays.asList(
            new Receiver(Addresses.get(0), new BigInteger("800").multiply(UnitCKB)),
            new Receiver(Addresses.get(1), new BigInteger("900").multiply(UnitCKB)),
            new Receiver(Addresses.get(2), new BigInteger("1000").multiply(UnitCKB)));

    System.out.println(
        "Before transfer, miner's balance: "
            + getBalance(minerAddress).divide(UnitCKB).toString(10)
            + " CKB");

    System.out.println(
        "Before transfer, first receiver1's balance: "
            + getBalance(Addresses.get(0)).divide(UnitCKB).toString(10)
            + " CKB");

    // miner send capacity to three receiver1 accounts with 800, 900 and 1000 CKB
    String hash = sendCapacity(minerAddress, receivers1, minerAddress);
    System.out.println("First transaction hash: " + hash);
    Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds

    System.out.println(
        "After transfer, first receiver1's balance: "
            + getBalance(Addresses.get(0)).divide(UnitCKB).toString(10)
            + " CKB");

    // Second transaction

    List<Receiver> receivers2 =
        Arrays.asList(
            new Receiver(Addresses.get(3), new BigInteger("400").multiply(UnitCKB)),
            new Receiver(Addresses.get(4), new BigInteger("500").multiply(UnitCKB)),
            new Receiver(Addresses.get(5), new BigInteger("600").multiply(UnitCKB)));

    String changeAddress = "ckt1qyqfnym6semhw2vzm33fjvk3ngxuf5433l9qz3af8a";

    System.out.println(
        "Before transfer, first receiver2's balance: "
            + getBalance(receivers2.get(0).address).divide(UnitCKB).toString(10)
            + " CKB");

    // send capacity to three receiver2 accounts with 400, 500 and 600 CKB
    String hash2 = sendCapacity(Addresses.subList(0, 3), receivers2, changeAddress);
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
      String sendAddress, List<Receiver> receivers, String changeAddress) throws IOException {
    Transaction transaction = generateTx(sendAddress, receivers, changeAddress);
    return api.sendTransaction(transaction);
  }

  private static String sendCapacity(
      List<String> sendAddresses, List<Receiver> receivers, String changeAddress)
      throws IOException {
    Transaction transaction = generateTx(sendAddresses, receivers, changeAddress);
    return api.sendTransaction(transaction);
  }

  private static Transaction generateTx(
      String address, List<Receiver> receivers, String changeAddress) throws IOException {
    BigInteger needCapacity = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacity = needCapacity.add(receiver.capacity);
    }
    return generateTx(Collections.singletonList(address), receivers, changeAddress);
  }

  private static Transaction generateTx(
      List<String> sendAddresses, List<Receiver> receivers, String changeAddress)
      throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();
    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, changeAddress);
    txBuilder.addOutputs(cellOutputs);

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(10000);

    List<CellsWithAddress> cellsWithAddresses =
        txUtils.collectInputs(sendAddresses, cellOutputs, feeRate);
    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (CellInput cellInput : cellsWithAddress.inputs) {
        txBuilder.addWitness(new Witness(Witness.EMPTY_LOCK));
      }
      if (cellsWithAddress.inputs.size() > 0) {
        scriptGroupWithPrivateKeysList.add(
            new ScriptGroupWithPrivateKeys(
                new ScriptGroup(
                    NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size())),
                Collections.singletonList(
                    PrivateKeys.get(Addresses.indexOf(cellsWithAddress.address)))));
        startIndex += cellsWithAddress.inputs.size();
      }
    }

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }

    return signBuilder.buildTx();
  }
}
