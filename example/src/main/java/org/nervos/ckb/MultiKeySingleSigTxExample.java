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
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Utils;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class MultiKeySingleSigTxExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static final String TestAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";
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
    List<Receiver> receivers1 =
        Arrays.asList(
            new Receiver(Addresses.get(0), Utils.ckbToShannon(800)),
            new Receiver(Addresses.get(1), Utils.ckbToShannon(900)),
            new Receiver(Addresses.get(2), Utils.ckbToShannon(1000)));

    String changeAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440a9qpq2m6m";

    System.out.println(
        "Before transferring, sender's balance: " + getBalance(TestAddress) + " CKB");

    System.out.println(
        "Before transferring, first receiver1's balance: " + getBalance(Addresses.get(0)) + " CKB");

    System.out.println(
        "Before transferring, change address's balance: " + getBalance(changeAddress) + " CKB");

    String hash = sendCapacity(TestAddress, receivers1, changeAddress);
    System.out.println("First transaction hash: " + hash);

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);

    System.out.println("After transferring, sender's balance: " + getBalance(TestAddress) + " CKB");

    System.out.println(
        "After transferring, first receiver1's balance: " + getBalance(Addresses.get(0)) + " CKB");

    System.out.println(
        "After transferring, change address's balance: " + getBalance(changeAddress) + " CKB");

    // Second transaction
    List<Receiver> receivers2 =
        Arrays.asList(
            new Receiver(Addresses.get(3), Utils.ckbToShannon(600)),
            new Receiver(Addresses.get(4), Utils.ckbToShannon(700)),
            new Receiver(Addresses.get(5), Utils.ckbToShannon(800)));

    System.out.println(
        "Before transferring, first receiver1's balance: "
            + getBalance(receivers1.get(0).address)
            + " CKB");

    System.out.println(
        "Before transferring, first receiver2's balance: "
            + getBalance(receivers2.get(0).address)
            + " CKB");

    System.out.println(
        "Before transferring, change address's balance: " + getBalance(changeAddress) + " CKB");

    String hash2 = sendCapacity(Addresses.subList(0, 3), receivers2, changeAddress);
    System.out.println("Second transaction hash: " + hash2);

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);

    System.out.println(
        "After transferring, first receiver1's balance: "
            + getBalance(receivers1.get(0).address)
            + " CKB");

    System.out.println(
        "After transferring, first receiver2's balance: "
            + getBalance(receivers2.get(0).address)
            + " CKB");

    System.out.println(
        "After transferring, change address's balance: " + getBalance(changeAddress) + " CKB");
  }

  private static String getBalance(String address) throws IOException {
    CellFetcher cellFetcher = new CellFetcher(api);
    return cellFetcher.getCapacityWithAddress(address).divide(UnitCKB).toString(10);
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
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    CollectResult collectResult =
        txUtils.collectInputs(sendAddresses, txBuilder.buildTx(), feeRate, Sign.SIGN_LENGTH * 2);

    // update change output capacity after collecting cells
    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    txBuilder.setOutputs(cellOutputs);

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : collectResult.cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
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
