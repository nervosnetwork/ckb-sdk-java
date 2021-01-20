package org.nervos.ckb;

import static org.nervos.ckb.utils.Const.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.indexer.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Utils;
import org.nervos.ckb.utils.address.AddressGenerator;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2021 Nervos Foundation. All rights reserved. */
// ACP RFC:
// https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0026-anyone-can-pay/0026-anyone-can-pay.md
public class ACPTransactionExample {
  // ACP_CKB_MINIMUM is set to 9, which means in each transaction, one must at least transfers 10^9
  // shannons, or 10 CKBytes into the anyone-can-pay cel
  private static final String ACP_CKB_MINIMUM = "09";

  private static Api api;
  private static CkbIndexerApi ckbIndexerApi;

  private static final List<String> SendPrivateKeys =
      Collections.singletonList("d00c06bfd800d27397002dca6fb0993d5ba6399b4238b2f29ee9deb97593d2bc");
  private static final List<String> SendAddresses =
      Collections.singletonList("ckt1qyqvsv5240xeh85wvnau2eky8pwrhh4jr8ts8vyj37");
  private static final List<String> ReceiveAddresses =
      Collections.singletonList("ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek");
  private static String receiverAcpAddress;

  static {
    api = new Api(NODE_URL, false);
    ckbIndexerApi = new CkbIndexerApi(CKB_INDEXER_URL, true);

    Script receiverScript = AddressParser.parse(ReceiveAddresses.get(0)).script;
    receiverScript.codeHash = ACP_CODE_HASH;
    receiverScript.args = receiverScript.args + ACP_CKB_MINIMUM;
    receiverAcpAddress = AddressGenerator.generate(Network.TESTNET, receiverScript);
  }

  public static void main(String[] args) throws Exception {
    System.out.println(
        "Before transferring, first sender's balance: "
            + getBalance(SendAddresses.get(0)).divide(UnitCKB).toString(10)
            + " CKB");

    String acpHash = createACPCell();
    System.out.println("Create acp cell tx hash: " + acpHash);

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);
    System.out.println("Transfer SUDT tx hash: " + transfer(new OutPoint(acpHash, "0x0")));
  }

  private static BigInteger getBalance(String address) throws IOException {
    return new IndexerCollector(api, ckbIndexerApi).getCapacity(address);
  }

  private static String createACPCell() throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    IndexerCollector txUtils = new IndexerCollector(api, ckbIndexerApi);

    List<Receiver> receivers =
        Collections.singletonList(new Receiver(receiverAcpAddress, Utils.ckbToShannon(200)));
    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, SendAddresses.get(0));
    txBuilder.addOutputs(cellOutputs);

    txBuilder.setOutputsData(Arrays.asList("0x", "0x"));
    txBuilder.addCellDep(new CellDep(new OutPoint(ACP_TX_HASH, "0x0"), CellDep.DEP_GROUP));

    // You can get fee rate by rpc or set a simple number
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    // collectInputsWithIndexer method uses indexer rpc to collect cells quickly
    CollectResult collectResult =
        txUtils.collectInputs(SendAddresses, txBuilder.buildTx(), feeRate, Sign.SIGN_LENGTH * 2);

    // update change cell output capacity after collecting cells if there is changeOutput
    if (Numeric.toBigInt(collectResult.changeCapacity).compareTo(MIN_CKB) >= 0) {
      cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
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

  private static String transfer(OutPoint acpOutPoint) throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    IndexerCollector txUtils = new IndexerCollector(api, ckbIndexerApi);

    List<Receiver> receivers =
        Collections.singletonList(new Receiver(receiverAcpAddress, Utils.ckbToShannon(210)));
    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, SendAddresses.get(0));
    txBuilder.addOutputs(cellOutputs);

    txBuilder.addCellDep(new CellDep(new OutPoint(ACP_TX_HASH, "0x0"), CellDep.DEP_GROUP));

    // You can get fee rate by rpc or set a simple number
    BigInteger feeRate = BigInteger.valueOf(1500);

    // initial_length = 2 * secp256k1_signature_byte.length
    // collectInputsWithIndexer method uses indexer rpc to collect cells quickly
    CollectResult collectResult =
        txUtils.collectInputs(SendAddresses, txBuilder.buildTx(), feeRate, Sign.SIGN_LENGTH * 2);

    // update change cell output capacity after collecting cells if there is changeOutput
    if (Numeric.toBigInt(collectResult.changeCapacity).compareTo(MIN_CKB) >= 0) {
      cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
      txBuilder.setOutputs(cellOutputs);
    }

    txBuilder.setOutputsData(Arrays.asList("0x", "0x"));

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
    txBuilder.addWitness("0x");
    txBuilder.addInput(new CellInput(acpOutPoint, "0x0"));

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }
    Transaction tx = signBuilder.buildTx();
    return api.sendTransaction(tx);
  }
}
