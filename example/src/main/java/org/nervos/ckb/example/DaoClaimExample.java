package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.DaoClaimTransactionBuilder;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.EpochUtils;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.InputIterator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

public class DaoClaimExample {
  private static int DAO_LOCK_PERIOD_EPOCHS = 180;

  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String daoDepositBlockHash = "0xadc5aad78f5a72667e90a31e53f1e7be3109180acff082c58ce63022be22fb37";
    String daoWithdrawBlockHash = "0x612c4e41476fba9a0b7df21e743a67254817d540bd6810aabe967d28d720e8b6";
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqgz576nmdewm50k9sxxqlt282em649uvrcrtzl8t";
    OutPoint depositOutpoint = new OutPoint(
        Numeric.hexStringToByteArray("0x9959cd863bd9ad07d541de6820a67433ddbb5e64c9c9c2f257898f0d51f7bcd2"), 0);
    OutPoint withdrawOutpoint = new OutPoint(
        Numeric.hexStringToByteArray("0xf23baa96166cb845c2ce5a5d229cdca0961cf81f6c204be0d3d91468eb68c209"), 0);

    // Send transaction
    Api api = new Api("https://testnet.ckb.dev", false);

    Header depositBlockHeader = api.getHeader(Numeric.hexStringToByteArray(daoDepositBlockHash));
    Header withdrawBlockHeader = api.getHeader(Numeric.hexStringToByteArray(daoWithdrawBlockHash));

    // calculate since
    long minimalSince = calculateDaoMinimalSince(depositBlockHeader, withdrawBlockHeader);

    // calculate dao maximum withdraw
    Transaction daoDepositTransaction = api.getTransaction(depositOutpoint.txHash).transaction;
    CellOutput depositCell = daoDepositTransaction.outputs.get(depositOutpoint.index);
    byte[] depositCellData = daoDepositTransaction.outputsData.get(depositOutpoint.index);
    long occupiedCapacity = depositCell.occupiedCapacity(depositCellData);
    long daoMaximumWithdraw = calculateDaoMaximumWithdraw(depositBlockHeader, withdrawBlockHeader,
                                                      depositCell, occupiedCapacity);
    long daoReward = daoMaximumWithdraw - depositCell.capacity;

    // Construct transaction
    Iterator<TransactionInput> iterator = new InputIterator(sender);

    CellInput withdrawCellInput = new CellInput(withdrawOutpoint, minimalSince);
    CellWithStatus withdrawCell = api.getLiveCell(withdrawOutpoint, true);
    TransactionInput transactionInput = new TransactionInput(
        withdrawCellInput,
        withdrawCell.cell.output,
        withdrawCell.cell.data.content);

    TransactionWithScriptGroups txWithGroups = new DaoClaimTransactionBuilder(iterator, Network.TESTNET)
        .addHeaderDep(daoDepositBlockHash)
        .addHeaderDep(daoWithdrawBlockHash)
        .addInput(transactionInput)
        .addDaoReward(daoReward)
        .setFeeRate(1000)
        .setChangeOutput(sender)
        .build(0);

    // Sign transaction
    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, "6dc5768bf429930df178a1a3b2a6d64e4fb36c52071eaac05574315a06a4a31e");

    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }

  private static long calculateDaoMinimalSince(Header depositBlockHeader,
                                               Header withdrawBlockHeader) {
    EpochUtils.EpochInfo depositEpoch = EpochUtils.parse(depositBlockHeader.epoch);
    EpochUtils.EpochInfo withdrawEpoch = EpochUtils.parse(withdrawBlockHeader.epoch);

    // calculate since
    long withdrawFraction = withdrawEpoch.index * depositEpoch.length;
    long depositFraction = depositEpoch.index * withdrawEpoch.length;
    long depositedEpochs = withdrawEpoch.number - depositEpoch.number;
    if (withdrawFraction > depositFraction) {
      depositedEpochs += 1;
    }
    long lockEpochs = (depositedEpochs + (DAO_LOCK_PERIOD_EPOCHS - 1))
        / DAO_LOCK_PERIOD_EPOCHS
        * DAO_LOCK_PERIOD_EPOCHS;
    long minimalSinceEpochNumber = depositEpoch.number + lockEpochs;
    long minimalSinceEpochIndex = depositEpoch.index;
    long minimalSinceEpochLength = depositEpoch.length;
    long minimalSince =
        EpochUtils.generateSince(
            minimalSinceEpochLength, minimalSinceEpochIndex, minimalSinceEpochNumber);
    return minimalSince;
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
        for (int i = 0 ; i < slice.length / 2; i ++) {
          byte tmp = slice[i];
          slice[i] = slice[slice.length - 1 - i];
          slice[slice.length - 1 - i] = tmp;
        }
    return new BigInteger(1, slice).longValue();
  }
}
