package org.nervos.ckb;

import static org.nervos.ckb.utils.Const.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.indexer.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.fixed.UInt128;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Utils;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2021 Nervos Foundation. All rights reserved. */
// SUDT RFC:
// https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0025-simple-udt/0025-simple-udt.md
public class SUDTExample {
  private static final BigInteger SUDT_ISSUE_SUM_AMOUNT = new BigInteger("1000000000000");
  private static final BigInteger SUDT_TRANSFER_AMOUNT = new BigInteger("60000000000");

  private static Api api;
  private static CkbIndexerApi ckbIndexerApi;
  private static final List<String> SendPrivateKeys =
      Collections.singletonList("08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2");
  private static final List<String> SendAddresses =
      Collections.singletonList(
          "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtyqlpwlx7ed68pftzv69wcvr5nxxqzzus2zxwa6");
  private static final List<String> ReceiveAddresses =
      Collections.singletonList(
          "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqwgx292hnvmn68xf779vmzrshpmm6epn4c0cgwga");
  public static Script sudtType;

  static {
    api = new Api(NODE_URL, false);
    ckbIndexerApi = new CkbIndexerApi(CKB_INDEXER_URL, true);

    Script senderScript = AddressParser.parse(SendAddresses.get(0)).script;
    byte[] sendLockHash = senderScript.computeHash();
    sudtType = new Script(SUDT_CODE_HASH, sendLockHash, Script.HashType.TYPE);
  }

  public static void main(String[] args) throws Exception {
    System.out.println(
        "Before transferring, first sender's balance: "
            + getBalance(SendAddresses.get(0)).divide(UnitCKB).toString(10)
            + " CKB");

    System.out.println("Issue SUDT tx hash: " + issue());

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);

    System.out.println("Transfer SUDT tx hash: " + transfer());
  }

  private static BigInteger getBalance(String address) throws IOException {
    return new IndexerCollector(api, ckbIndexerApi).getCapacity(address);
  }

  private static String issue() throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    IndexerCollector txUtils = new IndexerCollector(api, ckbIndexerApi);

    List<Receiver> receivers =
        Collections.singletonList(new Receiver(SendAddresses.get(0), Utils.ckbToShannon(2000)));
    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, SendAddresses.get(0));
    cellOutputs.get(0).type = sudtType;
    txBuilder.addOutputs(cellOutputs);

    txBuilder.setOutputsData(
        Arrays.asList(new UInt128(SUDT_ISSUE_SUM_AMOUNT).toBytes(), new byte[]{}));
    txBuilder.addCellDep(new CellDep(new OutPoint(SUDT_TX_HASH, 0), CellDep.DepType.CODE));

    // You can get fee rate by rpc or set a simple number
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    // collectInputsWithIndexer method uses indexer rpc to collect cells quickly
    CollectResult collectResult =
        txUtils.collectInputs(SendAddresses, txBuilder.buildTx(), feeRate, Sign.SIGN_LENGTH * 2);

    // update change cell output capacity after collecting cells if there is changeOutput
    if (Numeric.toBigInt(collectResult.changeCapacity).compareTo(MIN_CKB) >= 0) {
      cellOutputs.get(cellOutputs.size() - 1).capacity = new BigInteger(collectResult.changeCapacity);
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

  private static String transfer() throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    IndexerCollector txUtils = new IndexerCollector(api, ckbIndexerApi);

    List<Receiver> receivers =
        Collections.singletonList(new Receiver(ReceiveAddresses.get(0), MIN_SUDT_CKB));
    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, SendAddresses.get(0));
    cellOutputs.get(0).type = sudtType;
    txBuilder.addOutputs(cellOutputs);

    txBuilder.addCellDep(new CellDep(new OutPoint(SUDT_TX_HASH, 0), CellDep.DepType.CODE));

    // You can get fee rate by rpc or set a simple number
    BigInteger feeRate = BigInteger.valueOf(1500);

    // initial_length = 2 * secp256k1_signature_byte.length
    // collectInputsWithIndexer method uses indexer rpc to collect cells quickly
    CollectResult collectResult =
        txUtils.collectInputs(
            SendAddresses, txBuilder.buildTx(), feeRate, Sign.SIGN_LENGTH * 2, sudtType);

    // update change cell output capacity after collecting cells if there is changeOutput
    if (Numeric.toBigInt(collectResult.changeCapacity).compareTo(MIN_CKB) >= 0) {
      cellOutputs.get(cellOutputs.size() - 1).capacity = new BigInteger(collectResult.changeCapacity);
      cellOutputs.get(cellOutputs.size() - 1).type = sudtType;
      txBuilder.setOutputs(cellOutputs);
    }

    txBuilder.setOutputsData(
        Arrays.asList(
            new UInt128((SUDT_TRANSFER_AMOUNT)).toBytes(),
                new UInt128(SUDT_ISSUE_SUM_AMOUNT.subtract(SUDT_TRANSFER_AMOUNT)).toBytes()));

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
