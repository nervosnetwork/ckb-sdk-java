package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.scriptHandler.ScriptHandler;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DaoClaimTransactionBuilder extends AbstractTransactionBuilder {
  private static Script daoScript = new Script(Script.DAO_CODE_HASH,
                                               new byte[0],
                                               Script.HashType.TYPE);
  private static byte[] depositDaoData = Numeric.hexStringToByteArray("0x0000000000000000");

  CkbTransactionBuilder builder;
  private Api api;
  private DaoType daoType;

  private enum DaoType {
    DEPOSIT,
    CLAIM,
  }

  public DaoClaimTransactionBuilder(Iterator<TransactionInput> availableInputs, Network network, List<TransactionInput> transactionInputs, OutPoint daoOutpoint, Api api) throws IOException {
    super(availableInputs, network);
    builder = new CkbTransactionBuilder(availableInputs, network);
    this.api = api;
    CellInput withdrawCellInput = new CellInput(daoOutpoint, 0);
    CellWithStatus cellWithStatus = api.getLiveCell(daoOutpoint, true);
    TransactionInput input = new TransactionInput(
        withdrawCellInput,
        cellWithStatus.cell.output,
        cellWithStatus.cell.data.content);
    daoType = resolveDaoType(cellWithStatus.cell.data.content);
    if (daoType == DaoType.CLAIM) {
      builder.reward += getDaoReward(daoOutpoint);
    }
    transactionInputs.add(input);
  }

  private DaoType resolveDaoType(byte[] outputData) {
    if (outputData.length != 8) {
      throw new IllegalArgumentException("Dao cell's length should be 8 bytes");
    }
    if (Arrays.equals(outputData, depositDaoData)) {
      return DaoType.DEPOSIT;
    } else {
      return DaoType.CLAIM;
    }
  }

  private long getDaoReward(OutPoint withdrawOutpoint) throws IOException {
    Transaction withdrawTx = api.getTransaction(withdrawOutpoint.txHash).transaction;
    CellOutput depositCell = null;
    byte[] depositCellData = null;
    OutPoint depositOutpoint = null;
    for (int i = 0; i < withdrawTx.inputs.size(); i++) {
      OutPoint outPoint = withdrawTx.inputs.get(i).previousOutput;
      CellWithStatus cellWithStatus = api.getLiveCell(outPoint, true);
      if (isDepositCell(cellWithStatus)) {
        depositCell = cellWithStatus.cell.output;
        depositCellData = cellWithStatus.cell.data.content;
        depositOutpoint = outPoint;
        break;
      }
    }
    if (depositCell == null || depositDaoData == null) {
      throw new RuntimeException("Can find deposit cell");
    }

    Header depositBlockHeader = api.getHeader(depositOutpoint.txHash);
    Header withdrawBlockHeader = api.getHeader(withdrawTx.hash);
    long occupiedCapacity = depositCell.occupiedCapacity(depositCellData);
    long daoMaximumWithdraw = calculateDaoMaximumWithdraw(depositBlockHeader, withdrawBlockHeader,
                                                          depositCell, occupiedCapacity);
    long daoReward = daoMaximumWithdraw - depositCell.capacity;
    return daoReward;
  }

  private static boolean isDepositCell(CellWithStatus cellWithStatus) {
    CellOutput output = cellWithStatus.cell.output;
    byte[] data = cellWithStatus.cell.data.content;
    return daoScript.equals(output.type) && Arrays.equals(data, depositDaoData);
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

  public DaoClaimTransactionBuilder setFeeRate(long feeRate) {
    builder.setFeeRate(feeRate);
    return this;
  }

  public DaoClaimTransactionBuilder addOutput(String address, long capacity) {
    builder.addOutput(address, capacity);
    return this;
  }

  //  public DaoClaimTransactionBuilder setChangeOutput(CellOutput output, byte[] data) {
  //    builder.setChangeOutput(output, data);
  //    return this;
  //  }

  public DaoClaimTransactionBuilder setChangeOutput(String address) {
    builder.setChangeOutput(address);
    return this;
  }

  public TransactionWithScriptGroups build(Object... contexts) {
    return builder.build(contexts);
  }
}
