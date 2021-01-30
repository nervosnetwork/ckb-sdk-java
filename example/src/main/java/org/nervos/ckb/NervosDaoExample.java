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
import org.nervos.ckb.utils.EpochUtils;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Utils;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class NervosDaoExample {
  private static final String NERVOS_DAO_DATA = "0x0000000000000000";
  private static final int DAO_LOCK_PERIOD_EPOCHS = 180;
  private static final int DAO_MATURITY_BLOCKS = 5;

  private static Api api;
  private static CkbIndexerApi ckbIndexerApi;
  private static String DaoTestPrivateKey =
      "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2";
  private static String DaoTestAddress = "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g";

  private static final String DEPOSIT = "deposit";
  private static final String WITHDRAW_PHASE1 = "withdraw";
  private static final String WITHDRAW_PHASE2 = "claim";

  static {
    api = new Api(NODE_URL, false);
    ckbIndexerApi = new CkbIndexerApi(CKB_INDEXER_URL, false);
  }

  public static void main(String[] args) throws Exception {
    if (args.length > 0) {
      if (DEPOSIT.equals(args[0])) {
        System.out.println("Before depositing, balance: " + getBalance(DaoTestAddress) + " CKB");
        Transaction transaction = generateDepositingToDaoTx(Utils.ckbToShannon(1000));
        String txHash = api.sendTransaction(transaction);
        System.out.println("Nervos DAO deposit tx hash: " + txHash);
        // Waiting some time to make tx into blockchain
        System.out.println("After depositing, balance: " + getBalance(DaoTestAddress) + " CKB");
      } else if (WITHDRAW_PHASE1.equals(args[0])) {
        // Nervos DAO withdraw phase1 must be after 4 epoch of depositing transaction
        String depositTxHash = args[1];
        OutPoint depositOutPoint = new OutPoint(depositTxHash, "0x0");
        Transaction transaction = generateWithdrawingFromDaoTx(depositOutPoint);
        String txHash = api.sendTransaction(transaction);
        System.out.println("Nervos DAO withdraw phase1 tx hash: " + txHash);
      } else if (WITHDRAW_PHASE2.equals(args[0])) {
        // Nervos DAO withdraw phase2 must be after 180 epoch of depositing transaction
        String withdrawTxHash = args[1];
        TransactionWithStatus withdrawTx = api.getTransaction(withdrawTxHash);
        OutPoint depositOutPoint = withdrawTx.transaction.inputs.get(0).previousOutput;
        OutPoint withdrawOutPoint = new OutPoint(withdrawTxHash, "0x0");
        Transaction transaction =
            generateClaimingFromDaoTx(depositOutPoint, withdrawOutPoint, Utils.ckbToShannon(0.01));
        String txHash = api.sendTransaction(transaction);
        System.out.println("Nervos DAO withdraw phase2 tx hash: " + txHash);
        // Waiting some time to make tx into blockchain
        System.out.println("After withdrawing, balance: " + getBalance(DaoTestAddress) + " CKB");
      }
    }
  }

  private static String getBalance(String address) throws IOException {
    return new IndexerCollector(api, ckbIndexerApi)
        .getCapacity(address)
        .divide(UnitCKB)
        .toString(10);
  }

  private static Transaction generateDepositingToDaoTx(BigInteger capacity) throws IOException {
    Script type =
        new Script(SystemContract.getSystemNervosDaoCell(api).cellHash, "0x", Script.TYPE);

    IndexerCollector txUtils = new IndexerCollector(api, ckbIndexerApi);

    List<CellOutput> cellOutputs =
        txUtils.generateOutputs(
            Collections.singletonList(new Receiver(DaoTestAddress, capacity)), DaoTestAddress);
    cellOutputs.get(0).type = type;

    List<String> cellOutputsData = Arrays.asList(NERVOS_DAO_DATA, "0x");

    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();
    TransactionBuilder txBuilder = new TransactionBuilder(api);
    txBuilder.addOutputs(cellOutputs);
    txBuilder.setOutputsData(cellOutputsData);
    txBuilder.addCellDep(
        new CellDep(SystemContract.getSystemNervosDaoCell(api).outPoint, CellDep.CODE));

    // You can get fee rate by rpc or set a simple number
    BigInteger feeRate = BigInteger.valueOf(1024);
    IndexerCollector indexerCollector = new IndexerCollector(api, ckbIndexerApi);
    CollectResult collectResult =
        indexerCollector.collectInputs(
            Collections.singletonList(DaoTestAddress),
            txBuilder.buildTx(),
            feeRate,
            Sign.SIGN_LENGTH * 2);

    // update change output capacity after collecting cells
    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    txBuilder.setOutputs(cellOutputs);

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : collectResult.cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
      }
      scriptGroupWithPrivateKeysList.add(
          new ScriptGroupWithPrivateKeys(
              new ScriptGroup(NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size())),
              Collections.singletonList(DaoTestPrivateKey)));
      startIndex += cellsWithAddress.inputs.size();
    }

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }
    return signBuilder.buildTx();
  }

  private static Transaction generateWithdrawingFromDaoTx(OutPoint depositOutPoint)
      throws IOException {
    CellWithStatus cellWithStatus = api.getLiveCell(depositOutPoint, true);
    if (!CellWithStatus.Status.LIVE.getValue().equals(cellWithStatus.status)) {
      throw new IOException("Cell is not yet live!");
    }
    TransactionWithStatus transactionWithStatus = api.getTransaction(depositOutPoint.txHash);
    if (!TransactionWithStatus.Status.COMMITTED
        .getValue()
        .equals(transactionWithStatus.txStatus.status)) {
      throw new IOException("Transaction is not committed yet!");
    }
    Block depositBlock = api.getBlock(transactionWithStatus.txStatus.blockHash);
    BigInteger depositBlockNumber = Numeric.toBigInt(depositBlock.header.number);
    CellOutput cellOutput = cellWithStatus.cell.output;

    String outputData = Numeric.toHexString(new UInt64(depositBlockNumber).toBytes());

    Script lock = LockUtils.generateLockScriptWithAddress(DaoTestAddress);
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
    txBuilder.addOutputs(cellOutputs);
    txBuilder.addInput(new CellInput(depositOutPoint, "0x0"));

    // You can get fee rate by rpc or set a simple number
    BigInteger feeRate = BigInteger.valueOf(1024);
    IndexerCollector indexerCollector = new IndexerCollector(api, ckbIndexerApi);
    CollectResult collectResult =
        indexerCollector.collectInputs(
            Collections.singletonList(DaoTestAddress),
            txBuilder.buildTx(),
            feeRate,
            Sign.SIGN_LENGTH * 2);

    // update change output capacity after collecting cells
    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    txBuilder.setOutputs(cellOutputs);

    CellsWithAddress cellsWithAddress = collectResult.cellsWithAddresses.get(0);
    txBuilder.setInputs(cellsWithAddress.inputs);
    for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
      if (i == 0) {
        txBuilder.addWitness(new Witness(Witness.SIGNATURE_PLACEHOLDER));
      } else {
        txBuilder.addWitness("0x");
      }
    }
    ScriptGroup scriptGroup =
        new ScriptGroup(NumberUtils.regionToList(0, cellsWithAddress.inputs.size()));
    scriptGroupWithPrivateKeysList.add(
        new ScriptGroupWithPrivateKeys(scriptGroup, Collections.singletonList(DaoTestPrivateKey)));

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }
    return signBuilder.buildTx();
  }

  private static Transaction generateClaimingFromDaoTx(
      OutPoint depositOutPoint, OutPoint withdrawingOutPoint, BigInteger fee) throws IOException {
    Script lock = LockUtils.generateLockScriptWithAddress(DaoTestAddress);
    CellWithStatus cellWithStatus = api.getLiveCell(withdrawingOutPoint, true);
    if (!CellWithStatus.Status.LIVE.getValue().equals(cellWithStatus.status)) {
      throw new IOException("Cell is not yet live!");
    }
    TransactionWithStatus transactionWithStatus = api.getTransaction(withdrawingOutPoint.txHash);
    if (!TransactionWithStatus.Status.COMMITTED
        .getValue()
        .equals(transactionWithStatus.txStatus.status)) {
      throw new IOException("Transaction is not committed yet!");
    }

    BigInteger depositBlockNumber =
        new UInt64(Numeric.hexStringToByteArray(cellWithStatus.cell.data.content)).getValue();
    Block depositBlock = api.getBlockByNumber(Numeric.toHexStringWithPrefix(depositBlockNumber));
    EpochUtils.EpochInfo depositEpoch = EpochUtils.parse(depositBlock.header.epoch);

    Block withdrawBlock = api.getBlock(transactionWithStatus.txStatus.blockHash);
    EpochUtils.EpochInfo withdrawEpoch = EpochUtils.parse(withdrawBlock.header.epoch);

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
        EpochUtils.generateSince(
            minimalSinceEpochLength, minimalSinceEpochIndex, minimalSinceEpochNumber);
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

    return tx.sign(Numeric.toBigInt(DaoTestPrivateKey));
  }
}
