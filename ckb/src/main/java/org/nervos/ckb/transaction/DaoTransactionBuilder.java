package org.nervos.ckb.transaction;

import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.MoleculeConverter;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

import static org.nervos.ckb.transaction.handler.DaoScriptHandler.*;

public class DaoTransactionBuilder extends AbstractTransactionBuilder {
  CkbTransactionBuilder builder;
  private Api api;
  private TransactionType transactionType;
  private long depositBlockNumber = -1;
  private long depositCellCapacity = -1;

  private enum TransactionType {
    WITHDRAW,
    CLAIM,
  }

  public DaoTransactionBuilder(TransactionBuilderConfiguration configuration, Iterator<TransactionInput> availableInputs, OutPoint daoOutPoint, Api api) throws IOException {
    super(configuration, availableInputs);
    builder = new CkbTransactionBuilder(configuration, availableInputs);
    this.api = api;
    CellInput cellInput = new CellInput(daoOutPoint, 0);
    CellWithStatus cellWithStatus = api.getLiveCell(daoOutPoint, true);
    TransactionInput input = new TransactionInput(
        cellInput,
        cellWithStatus.cell.output,
        cellWithStatus.cell.data.content);
    transactionType = getTransactionType(cellWithStatus.cell.data.content);
    switch (transactionType) {
      case WITHDRAW:
        TransactionWithStatus txWithStatus = api.getTransaction(daoOutPoint.txHash);
        depositBlockNumber = api.getHeader(txWithStatus.txStatus.blockHash).number;
        depositCellCapacity = txWithStatus.transaction.outputs.get(daoOutPoint.index).capacity;
        break;
      case CLAIM:
        builder.reward += getDaoReward(daoOutPoint);
        break;
      default:
        throw new IllegalArgumentException("Unsupported transaction type");
    }
    builder.transactionInputs.add(input);
  }

  private static TransactionType getTransactionType(byte[] outputData) {
    if (outputData.length != 8) {
      throw new IllegalArgumentException("Dao cell's output data length should be 8 bytes");
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

  private static long calculateDaoMaximumWithdraw(Header depositBlockHeader, Header withdrawBlockHeader, CellOutput output, long occupiedCapacity) {
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
    return Numeric.littleEndianBytesToBigInteger(slice).longValue();
  }

  public DaoTransactionBuilder(TransactionBuilderConfiguration configuration, Iterator<TransactionInput> availableInputs) {
    super(configuration, availableInputs);
  }

  public DaoTransactionBuilder addOutput(String address, long capacity) {
    builder.addOutput(address, capacity);
    return this;
  }

  public DaoTransactionBuilder setChangeOutput(String address) {
    builder.setChangeOutput(address);
    return this;
  }

  public DaoTransactionBuilder addWithdrawOutput(String address) {
    if (depositBlockNumber == -1) {
      throw new IllegalStateException("Deposit block number is not initialized");
    }
    CellOutput output = new CellOutput(
        depositCellCapacity,
        Address.decode(address).getScript(),
        DAO_SCRIPT);
    byte[] data = MoleculeConverter.packUint64(depositBlockNumber).toByteArray();
    builder.addOutput(output, data);
    return this;
  }

  @Override
  public TransactionWithScriptGroups build(Object... contexts) {
    return builder.build(contexts);
  }
}
