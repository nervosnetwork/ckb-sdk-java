package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.scriptHandler.ScriptHandler;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.MoleculeConverter;
import org.nervos.ckb.utils.address.Address;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

import static org.nervos.ckb.transaction.scriptHandler.DaoScriptHandler.*;

public class DaoClaimTransactionBuilder extends AbstractTransactionBuilder {
  CkbTransactionBuilder builder;
  private Api api;
  private TransactionType transactionType;
  private long depositBlockNumber = -1;

  private enum TransactionType {
    WITHDRAW,
    CLAIM,
  }

  public DaoClaimTransactionBuilder(Iterator<TransactionInput> availableInputs, Network network, OutPoint daoOutpoint, Api api) throws IOException {
    super(availableInputs, network);
    builder = new CkbTransactionBuilder(availableInputs, network);
    this.api = api;
    CellInput cellInput = new CellInput(daoOutpoint, 0);
    CellWithStatus cellWithStatus = api.getLiveCell(daoOutpoint, true);
    TransactionInput input = new TransactionInput(
        cellInput,
        cellWithStatus.cell.output,
        cellWithStatus.cell.data.content);
    transactionType = getTransactionType(cellWithStatus.cell.data.content);
    switch (transactionType) {
      case WITHDRAW:
        TransactionWithStatus txWithStatus = api.getTransaction(daoOutpoint.txHash);
        depositBlockNumber = api.getHeader(txWithStatus.txStatus.blockHash).number;
        break;
      case CLAIM:
        builder.reward += getDaoReward(daoOutpoint);
        break;
    }
    builder.transactionInputs.add(input);
  }

  private static TransactionType getTransactionType(byte[] outputData) {
    if (outputData.length != 8) {
      throw new IllegalArgumentException("Dao cell's length should be 8 bytes");
    }
    if (Arrays.equals(outputData, DEPOSIT_CELL_DATA)) {
      return TransactionType.WITHDRAW;
    } else {
      return TransactionType.CLAIM;
    }
  }

  private long getDaoReward(OutPoint withdrawOutpoint) throws IOException {
    TransactionWithStatus txWithStatus = api.getTransaction(withdrawOutpoint.txHash);
    Transaction withdrawTx = txWithStatus.transaction;
    byte[] withdrawBlockHash = txWithStatus.txStatus.blockHash;

    CellOutput depositCell = null;
    byte[] depositCellData = null;
    byte[] depositBlockHash = null;

    for (int i = 0; i < withdrawTx.inputs.size(); i++) {
      OutPoint outPoint = withdrawTx.inputs.get(i).previousOutput;
      txWithStatus = api.getTransaction(outPoint.txHash);
      Transaction tx = txWithStatus.transaction;
      CellOutput output = tx.outputs.get(outPoint.index);
      byte[] data = tx.outputsData.get(outPoint.index);
      if (isDepositCell(output, data)) {
        depositCell = output;
        depositCellData = data;
        depositBlockHash = txWithStatus.txStatus.blockHash;
        break;
      }
    }
    if (depositCell == null) {
      throw new RuntimeException("Can find deposit cell");
    }

    Header depositBlockHeader = api.getHeader(depositBlockHash);
    Header withdrawBlockHeader = api.getHeader(withdrawBlockHash);
    long occupiedCapacity = depositCell.occupiedCapacity(depositCellData);
    long daoMaximumWithdraw = calculateDaoMaximumWithdraw(depositBlockHeader, withdrawBlockHeader,
                                                          depositCell, occupiedCapacity);
    long daoReward = daoMaximumWithdraw - depositCell.capacity;
    return daoReward;
  }

  private static long calculateDaoMaximumWithdraw(Header depositBlockHeader,
                                                  Header withdrawBlockHeader,
                                                  CellOutput output,
                                                  long occupiedCapacity) {
    BigInteger depositAr = BigInteger.valueOf(extractAr(depositBlockHeader.dao));
    BigInteger withdrawAr = BigInteger.valueOf(extractAr(withdrawBlockHeader.dao));

    BigInteger maximumWithdraw = BigInteger.valueOf(output.capacity - occupiedCapacity)
        .multiply(withdrawAr)
        .divide(depositAr)
        .add(BigInteger.valueOf(occupiedCapacity));
    return maximumWithdraw.longValue();
  }

  public static long extractAr(byte[] dao) {
    byte[] slice = Arrays.copyOfRange(dao, 8, 16);
    for (int i = 0; i < slice.length / 2; i++) {
      byte tmp = slice[i];
      slice[i] = slice[slice.length - 1 - i];
      slice[slice.length - 1 - i] = tmp;
    }
    return new BigInteger(1, slice).longValue();
  }

  public DaoClaimTransactionBuilder(Iterator<TransactionInput> availableInputs, Network network) {
    super(availableInputs, network);
  }

  @Override
  public DaoClaimTransactionBuilder registerScriptHandler(ScriptHandler scriptHandler) {
    builder.registerScriptHandler(scriptHandler);
    return this;
  }

  @Override
  public long getFeeRate() {
    return builder.getFeeRate();
  }

  @Override
  public Transaction getTx() {
    return builder.getTx();
  }

  public DaoClaimTransactionBuilder setFeeRate(long feeRate) {
    builder.setFeeRate(feeRate);
    return this;
  }

  public DaoClaimTransactionBuilder addOutput(String address, long capacity) {
    builder.addOutput(address, capacity);
    return this;
  }

  public DaoClaimTransactionBuilder setChangeOutput(String address) {
    builder.setChangeOutput(address);
    return this;
  }

  public DaoClaimTransactionBuilder addWithdrawOutput(String address, long capacity) {
    if (depositBlockNumber == -1) {
      throw new IllegalStateException("Deposit block number is not initialized");
    }
    CellOutput output = new CellOutput(
        capacity,
        Address.decode(address).getScript()
        , DAO_SCRIPT);
    byte[] data = MoleculeConverter.packUint64(depositBlockNumber).toByteArray();
    builder.addOutput(output, data);
    return this;
  }

  public TransactionWithScriptGroups build() {
    return builder.build();
  }

  public TransactionWithScriptGroups build(Object... contexts) {
    return builder.build(contexts);
  }
}
