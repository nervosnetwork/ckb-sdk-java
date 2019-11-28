package org.nervos.ckb;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.type.transaction.TransactionWithStatus;
import org.nervos.ckb.utils.EpochParser;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;
import org.nervos.ckb.utils.Utils;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class NervosDaoExample {
  private static final String NERVOS_DAO_DATA = "0x0000000000000000";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static final int DAO_LOCK_PERIOD_EPOCHS = 180;
  private static final int DAO_MATURITY_BLOCKS = 5;

  private static final String NODE_URL = "http://localhost:8114";
  private static Api api;
  private static String DaoPrivateKey =
      "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2";
  private static String DaoAddress = "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g";

  static {
    api = new Api(NODE_URL, false);
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Before depositing, balance: " + getBalance(DaoAddress) + " CKB");
    OutPoint depositOutPoint = depositToDao(Utils.ckbToShannon(1000));
    System.out.println(depositOutPoint.txHash);

    Thread.sleep(5000);
    System.out.println("After depositing, balance: " + getBalance(DaoAddress) + " CKB");

    // Nervos DAO withdraw phase1 must be after 4 epoch of depositing transaction
    OutPoint withdrawOutPoint = startWithdrawingFromDao(depositOutPoint);
    System.out.println("Nervos DAO withdraw phase1 tx hash: " + withdrawOutPoint.txHash);

    // Nervos DAO withdraw phase2 must be after 180 epoch of depositing transaction
    Thread.sleep(24000);
    Transaction tx =
        generateWithdrawFromDaoTransaction(
            depositOutPoint, withdrawOutPoint, Utils.ckbToShannon(0.01));
    System.out.println(new Gson().toJson(tx));
    String txHash = api.sendTransaction(tx);
    System.out.println("Nervos DAO phase2 tx hash: " + txHash);

    Thread.sleep(2000);

    System.out.println("After withdrawing balance: " + getBalance(DaoAddress) + "CKB");
  }

  private static String getBalance(String address) throws IOException {
    CellCollector cellCollector = new CellCollector(api, true);
    return cellCollector.getCapacityWithAddress(address).divide(UnitCKB).toString(10);
  }

  private static OutPoint depositToDao(BigInteger capacity) throws IOException {
    Script type =
        new Script(SystemContract.getSystemNervosDaoCell(api).cellHash, "0x", Script.TYPE);

    CollectUtils txUtils = new CollectUtils(api);

    List<CellOutput> cellOutputs =
        txUtils.generateOutputs(
            Collections.singletonList(new Receiver(DaoAddress, capacity)), DaoAddress);
    cellOutputs.get(0).type = type;

    List<String> cellOutputsData = Arrays.asList(NERVOS_DAO_DATA, "0x");

    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();
    TransactionBuilder txBuilder = new TransactionBuilder(api);

    txBuilder.setOutputsData(cellOutputsData);
    txBuilder.addCellDep(
        new CellDep(SystemContract.getSystemNervosDaoCell(api).outPoint, CellDep.CODE));

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(1024);
    CollectUtils collectUtils = new CollectUtils(api, true);
    CollectResult collectResult =
        collectUtils.collectInputs(
            Collections.singletonList(DaoAddress),
            cellOutputs,
            feeRate,
            Sign.SIGN_LENGTH * 2,
            txBuilder.getCellDeps(),
            cellOutputsData,
            null);

    // update change output capacity after collecting cells
    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    txBuilder.addOutputs(cellOutputs);

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : collectResult.cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
      }
      scriptGroupWithPrivateKeysList.add(
          new ScriptGroupWithPrivateKeys(
              new ScriptGroup(NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size())),
              Collections.singletonList(DaoPrivateKey)));
      startIndex += cellsWithAddress.inputs.size();
    }

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }
    System.out.println(new Gson().toJson(signBuilder.buildTx()));
    String txHash = api.sendTransaction(signBuilder.buildTx());
    return new OutPoint(txHash, "0x0");
  }

  private static OutPoint startWithdrawingFromDao(OutPoint depositOutPoint) throws IOException {
    CellWithStatus cellWithStatus = api.getLiveCell(depositOutPoint, false);
    if (!CellWithStatus.LIVE.equals(cellWithStatus.status)) {
      throw new IOException("Cell is not yet live!");
    }
    TransactionWithStatus transactionWithStatus = api.getTransaction(depositOutPoint.txHash);
    if (!TransactionWithStatus.COMMITTED.equals(transactionWithStatus.txStatus.status)) {
      throw new IOException("Transaction is not committed yet!");
    }
    Block depositBlock = api.getBlock(transactionWithStatus.txStatus.blockHash);
    BigInteger depositBlockNumber = Numeric.toBigInt(depositBlock.header.number);
    CellOutput cellOutput = cellWithStatus.cell.output;
    String outputData = Numeric.toHexString(new UInt64(depositBlockNumber).toBytes());

    Script lock = LockUtils.generateLockScriptWithAddress(DaoAddress);
    CellOutput changeOutput = new CellOutput("0x0", lock);

    List<CellOutput> cellOutputs = Arrays.asList(cellOutput, changeOutput);
    List<String> cellOutputsData = Arrays.asList(outputData, "0x");
    List<String> headerDeps = Collections.singletonList(depositBlock.header.hash);

    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();
    TransactionBuilder txBuilder = new TransactionBuilder(api);
    txBuilder.addCellDep(
        new CellDep(SystemContract.getSystemNervosDaoCell(api).outPoint, CellDep.CODE));
    txBuilder.setOutputsData(cellOutputsData);
    txBuilder.setHeaderDeps(headerDeps);

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(1024);
    CollectUtils collectUtils = new CollectUtils(api, true);
    CollectResult collectResult =
        collectUtils.collectInputs(
            Collections.singletonList(DaoAddress),
            cellOutputs,
            feeRate,
            Sign.SIGN_LENGTH * 2,
            txBuilder.getCellDeps(),
            cellOutputsData,
            headerDeps);

    // update change output capacity after collecting cells
    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    txBuilder.addOutputs(cellOutputs);

    txBuilder.addInput(new CellInput(depositOutPoint, "0x0"));
    txBuilder.addWitness(new Witness());

    CellsWithAddress cellsWithAddress = collectResult.cellsWithAddresses.get(0);
    txBuilder.addInputs(cellsWithAddress.inputs);
    for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
      if (i == 0) {
        byte[] bytes =
            Serializer.serializeWitnessArgs(new Witness(Witness.SIGNATURE_PLACEHOLDER)).toBytes();
        txBuilder.addWitness(Numeric.toHexString(bytes));
      } else {
        txBuilder.addWitness("0x");
      }
    }
    ScriptGroup scriptGroup = new ScriptGroup();
    scriptGroup.addIndex(0);
    scriptGroup.addIndices(NumberUtils.regionToList(1, cellsWithAddress.inputs.size()));
    scriptGroupWithPrivateKeysList.add(
        new ScriptGroupWithPrivateKeys(scriptGroup, Collections.singletonList(DaoPrivateKey)));

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }
    System.out.println(new Gson().toJson(signBuilder.buildTx()));
    String txHash = api.sendTransaction(signBuilder.buildTx());
    return new OutPoint(txHash, "0x0");
  }

  private static Transaction generateWithdrawFromDaoTransaction(
      OutPoint depositOutPoint, OutPoint withdrawingOutPoint, BigInteger fee) throws IOException {
    Script lock = LockUtils.generateLockScriptWithAddress(DaoAddress);
    CellWithStatus cellWithStatus = api.getLiveCell(withdrawingOutPoint, true);
    if (!CellWithStatus.LIVE.equals(cellWithStatus.status)) {
      throw new IOException("Cell is not yet live!");
    }
    TransactionWithStatus transactionWithStatus = api.getTransaction(withdrawingOutPoint.txHash);
    if (!TransactionWithStatus.COMMITTED.equals(transactionWithStatus.txStatus.status)) {
      throw new IOException("Transaction is not committed yet!");
    }

    BigInteger depositBlockNumber =
        new UInt64(Numeric.hexStringToByteArray(cellWithStatus.cell.data.content)).getValue();
    Block depositBlock = api.getBlockByNumber(Numeric.toHexStringWithPrefix(depositBlockNumber));
    EpochParser.EpochParams depositEpoch = EpochParser.parse(depositBlock.header.epoch);

    Block withdrawBlock = api.getBlock(transactionWithStatus.txStatus.blockHash);
    EpochParser.EpochParams withdrawEpoch = EpochParser.parse(withdrawBlock.header.epoch);

    long withdrawFraction = withdrawEpoch.index * depositEpoch.length;
    long depositFraction = depositEpoch.index * withdrawEpoch.length;
    long depositedEpochs = withdrawEpoch.number - depositEpoch.number;
    if (withdrawFraction > depositFraction) {
      depositedEpochs += 1;
    }
    long lockEpochs =
        (depositedEpochs + (DAO_LOCK_PERIOD_EPOCHS - 1))
            / DAO_LOCK_PERIOD_EPOCHS
            * DAO_LOCK_PERIOD_EPOCHS;
    long minimalSinceEpochNumber = depositEpoch.number + lockEpochs;
    long minimalSinceEpochIndex = depositEpoch.index;
    long minimalSinceEpochLength = depositEpoch.length;

    String minimalSince =
        EpochParser.parse(minimalSinceEpochLength, minimalSinceEpochIndex, minimalSinceEpochNumber);
    String outputCapacity =
        api.calculateDaoMaximumWithdraw(depositOutPoint, withdrawBlock.header.hash);

    CellOutput cellOutput =
        new CellOutput(
            Numeric.toHexStringWithPrefix(Numeric.toBigInt(outputCapacity).subtract(fee)), lock);

    SystemScriptCell secpCell = SystemContract.getSystemSecpCell(api);
    SystemScriptCell nervosDaoCell = SystemContract.getSystemNervosDaoCell(api);

    Transaction tx =
        new Transaction(
            "0x0",
            Arrays.asList(
                new CellDep(secpCell.outPoint, CellDep.DEP_GROUP),
                new CellDep(nervosDaoCell.outPoint)),
            Arrays.asList(depositBlock.header.hash, withdrawBlock.header.hash),
            Collections.singletonList(new CellInput(withdrawingOutPoint, minimalSince)),
            Collections.singletonList(cellOutput),
            Collections.singletonList("0x"),
            Collections.singletonList(new Witness("", NERVOS_DAO_DATA, "")));

    return tx.sign(Numeric.toBigInt(DaoPrivateKey));
  }
}
