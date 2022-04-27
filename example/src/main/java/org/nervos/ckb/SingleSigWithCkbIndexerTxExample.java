package org.nervos.ckb;

import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.indexer.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.nervos.ckb.utils.Const.*;

public class SingleSigWithCkbIndexerTxExample {

  private static Api api;
  private static CkbIndexerApi ckbIndexerApi;
  private static List<String> SendPrivateKeys;
  private static List<String> SendAddresses;
  private static List<String> ReceiveAddresses;

  static {
    api = new Api(NODE_URL, false);
    ckbIndexerApi = new CkbIndexerApi(CKB_INDEXER_URL, false);
    SendPrivateKeys =
        Arrays.asList("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    SendAddresses = Arrays.asList("ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83");
    ReceiveAddresses =
        Arrays.asList(
            "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqwgx292hnvmn68xf779vmzrshpmm6epn4c0cgwga",
            "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqde3gn5m4jekd35ku7f3mk4s4zeh5w5tqc5t5mm8",
            "ckt1qg8mxsu48mncexvxkzgaa7mz2g25uza4zpz062relhjmyuc52ps3zn47dugwyk5e6mgxvlf5ukx7k3uyq9wlkkmegke");
  }

  public static void main(String[] args) throws Exception {
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    for (int i = 0; i < SendAddresses.size() - 1; i++) {
      byte[] testPublicKey = ECKeyPair.create(SendPrivateKeys.get(i)).getEncodedPublicKey(true);
      String address = SendAddresses.get(i);
      if (!address.equals(utils.generateFromPublicKey(testPublicKey))) {
        System.out.println("Private key and address " + address + " are not matched");
        return;
      }
    }

    System.out.println("Wait some time for ckb-indexer running");

    List<Receiver> receivers =
        Arrays.asList(
            new Receiver(ReceiveAddresses.get(0), Utils.ckbToShannon(200)),
            new Receiver(ReceiveAddresses.get(1), Utils.ckbToShannon(200)),
            new Receiver(ReceiveAddresses.get(2), Utils.ckbToShannon(300)));

    System.out.println(
        "Before transferring, first sender's balance: "
            + Long.divideUnsigned(getBalance(SendAddresses.get(0)), UnitCKB)
            + " CKB");

    byte[] hash = sendCapacity(receivers, SendAddresses.get(0));
    System.out.println("Transaction hash: " + hash);

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);

    System.out.println(
        "After transferring, first sender's balance: "
            + Long.divideUnsigned(getBalance(SendAddresses.get(0)), UnitCKB)
            + " CKB");
  }

  private static long getBalance(String address) throws IOException {
    return new IndexerCollector(api, ckbIndexerApi).getCapacity(address);
  }

  private static byte[] sendCapacity(List<Receiver> receivers, String changeAddress)
      throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    IndexerCollector txUtils = new IndexerCollector(api, ckbIndexerApi);

    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, changeAddress);
    txBuilder.addOutputs(cellOutputs);

    // You can get fee rate by rpc or set a simple number
    long feeRate = 1024;

    // initial_length = 2 * secp256k1_signature_byte.length
    // collectInputsWithIndexer method uses indexer rpc to collect cells quickly
    CollectResult collectResult =
        txUtils.collectInputs(SendAddresses, txBuilder.buildTx(), feeRate, Sign.SIGN_LENGTH * 2);

    // update change cell output capacity after collecting cells if there is changeOutput
    if (Long.compareUnsigned(collectResult.changeCapacity, 0) > 0) {
      cellOutputs.get(cellOutputs.size() - 1).capacity =
          collectResult.changeCapacity;
      txBuilder.setOutputs(cellOutputs);
    }

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
