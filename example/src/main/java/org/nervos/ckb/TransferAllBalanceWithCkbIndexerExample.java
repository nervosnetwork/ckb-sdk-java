package org.nervos.ckb;

import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.indexer.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.nervos.ckb.utils.Const.*;

public class TransferAllBalanceWithCkbIndexerExample {

  private static Api api;
  private static CkbIndexerApi ckbIndexerApi;
  private static List<String> SendPrivateKeys;
  private static List<String> SendAddresses;
  private static List<String> ReceiveAddresses;

  static {
    api = new Api(NODE_URL, false);
    ckbIndexerApi = new CkbIndexerApi(CKB_INDEXER_URL, false);
    SendPrivateKeys =
        Collections.singletonList(
            "d00c06bfd800d27397002dca6fb0993d5ba6399b4238b2f29ee9deb97593d2bc");
    SendAddresses =
        Collections.singletonList(
            "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqwgx292hnvmn68xf779vmzrshpmm6epn4c0cgwga");
    ReceiveAddresses =
        Collections.singletonList(
            "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtxfjvw09k4jwvefcaaufl6j8ja7rprugc6v4690");
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
        Collections.singletonList(new Receiver(ReceiveAddresses.get(0), Utils.ckbToShannon(100)));

    System.out.println(
        "Before transferring, first sender's balance: "
            + Long.divideUnsigned(getBalance(SendAddresses.get(0)), UnitCKB)
            + " CKB");

    // For transferring all balance, change address is set to be null
    // Because the transfer of the entire balance will not set the change cell, you need to
    // carefully calculate the transfer amount
    byte[] hash = sendCapacity(receivers, null);
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
