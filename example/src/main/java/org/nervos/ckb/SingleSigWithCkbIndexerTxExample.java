package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.CkbIndexerApi;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Utils;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class SingleSigWithCkbIndexerTxExample {

  private static final String NODE_URL = "http://localhost:8118";
  private static final String CKB_INDEXER_NODE_URL = "http://localhost:8116";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static CkbIndexerApi ckbIndexerApi;
  private static List<String> SendPrivateKeys;
  private static List<String> SendAddresses;
  private static List<String> ReceiveAddresses;

  static {
    api = new Api(NODE_URL, false);
    ckbIndexerApi = new CkbIndexerApi(CKB_INDEXER_NODE_URL, false);
    SendPrivateKeys =
        Arrays.asList(
            "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2",
            "d00c06bfd800d27397002dca6fb0993d5ba6399b4238b2f29ee9deb97593d2bc");
    SendAddresses =
        Arrays.asList(
            "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g",
            "ckt1qyqvsv5240xeh85wvnau2eky8pwrhh4jr8ts8vyj37");
    ReceiveAddresses =
        Arrays.asList(
            "ckt1qyqxvnycu7tdtyuejn3mmcnl4y09muxz8c3s2ewjd4",
            "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek");
  }

  public static void main(String[] args) throws Exception {
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    for (int i = 0; i < SendAddresses.size() - 1; i++) {
      String testPublicKey = ECKeyPair.publicKeyFromPrivate(SendPrivateKeys.get(i));
      String address = SendAddresses.get(i);
      if (!address.equals(utils.generateFromPublicKey(testPublicKey))) {
        System.out.println("Private key and address " + address + " are not matched");
        return;
      }
    }

    System.out.println("Wait some time for ckb-indexer running");

    List<Receiver> receivers =
        Arrays.asList(
            new Receiver(ReceiveAddresses.get(0), Utils.ckbToShannon(800)),
            new Receiver(ReceiveAddresses.get(1), Utils.ckbToShannon(900)));

    System.out.println(
        "Before transferring, first sender's balance: "
            + getBalance(SendAddresses.get(0)).divide(UnitCKB).toString(10)
            + " CKB");

    String hash = sendCapacity(receivers, SendAddresses.get(0));
    System.out.println("Transaction hash: " + hash);

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);

    System.out.println(
        "After transferring, first sender's balance: "
            + getBalance(SendAddresses.get(0)).divide(UnitCKB).toString(10)
            + " CKB");
  }

  private static BigInteger getBalance(String address) throws IOException {
    return new CollectUtils(ckbIndexerApi).getCapacityWithAddressByCkbIndexer(address);
  }

  private static String sendCapacity(List<Receiver> receivers, String changeAddress)
      throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(ckbIndexerApi);

    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, changeAddress);
    txBuilder.addOutputs(cellOutputs);

    // You can get fee rate by rpc or set a simple number
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    // collectInputsWithIndexer method uses indexer rpc to collect cells quickly
    CollectResult collectResult =
        txUtils.collectInputsWithCkbIndexer(
            SendAddresses, txBuilder.buildTx(), feeRate, Sign.SIGN_LENGTH * 2, true);

    // update change cell output capacity after collecting cells
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
                    SendPrivateKeys.get(SendAddresses.indexOf(cellsWithAddress.address)))));
        startIndex += cellsWithAddress.inputs.size();
      }
    }

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }
    Transaction tx = signBuilder.buildTx();
    return api.sendTransaction(tx);
  }
}
