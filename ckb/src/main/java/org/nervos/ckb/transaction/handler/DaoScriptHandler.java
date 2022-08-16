package org.nervos.ckb.transaction.handler;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.EpochUtils;
import org.nervos.ckb.utils.MoleculeConverter;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DaoScriptHandler implements ScriptHandler {
  public static Script DAO_SCRIPT = new Script(Script.DAO_CODE_HASH,
                                               new byte[0],
                                               Script.HashType.TYPE);
  public static byte[] DEPOSIT_CELL_DATA = Numeric.hexStringToByteArray("0x0000000000000000");
  public static int DAO_LOCK_PERIOD_EPOCHS = 180;


  private List<CellDep> cellDeps;
  private byte[] codeHash;

  public DaoScriptHandler(Network network) {
    OutPoint outPoint = new OutPoint();
    if (network == Network.MAINNET) {
      outPoint.txHash = Numeric.hexStringToByteArray("0xe2fb199810d49a4d8beec56718ba2593b665db9d52299a0f9e6e75416d73ff5c");
      outPoint.index = 2;
    } else if (network == Network.TESTNET) {
      outPoint.txHash = Numeric.hexStringToByteArray("0x8f8c79eb6671709633fe6a46de93c0fedc9c1b8a6527a18d3983879542635c9f");
      outPoint.index = 2;
    } else {
      throw new IllegalArgumentException("Unsupported network");
    }
    codeHash = Script.DAO_CODE_HASH;
    CellDep cellDep = new CellDep();
    cellDep.outPoint = outPoint;
    cellDep.depType = CellDep.DepType.CODE;
    cellDeps = Arrays.asList(cellDep);
  }

  private boolean isMatched(Script script) {
    if (script == null) {
      return false;
    }
    return Arrays.equals(script.codeHash, codeHash);
  }

  public static boolean isDepositCell(CellOutput output, byte[] data) {
    return DAO_SCRIPT.equals(output.type) && Arrays.equals(data, DEPOSIT_CELL_DATA);
  }

  @Override
  public boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context) {
    if (scriptGroup == null || !isMatched(scriptGroup.getScript())) {
      return false;
    }

    // add celldeps
    txBuilder.addCellDeps(cellDeps);

    if (context instanceof ClaimInfo) {
      ClaimInfo info = (ClaimInfo) context;
      int index = scriptGroup.getInputIndices().get(0);
      // add header deps
      int depositHeaderDepIndex = txBuilder.setHeaderDep(info.depositBlockHeader.hash);
      txBuilder.setHeaderDep(info.withdrawBlockHeader.hash);
      // update witness
      byte[] inputType = MoleculeConverter.packUint64(depositHeaderDepIndex).toByteArray();
      txBuilder.setWitness(index, WitnessArgs.Type.INPUT_TYPE, inputType);
      // update input since
      txBuilder.setInputSince(index, info.calculateDaoMinimumSince());
    } else if (context instanceof WithdrawInfo) {
      WithdrawInfo info = (WithdrawInfo) context;
      txBuilder.setHeaderDep(info.depositBlockHash);
    }
    return true;
  }

  public static class ClaimInfo {
    Header depositBlockHeader;
    Header withdrawBlockHeader;
    OutPoint withdrawOutpoint;

    public ClaimInfo(Api api, OutPoint withdrawOutpoint) {
      this.withdrawOutpoint = withdrawOutpoint;
      try {
        TransactionWithStatus txWithStatus = api.getTransaction(withdrawOutpoint.txHash);
        Transaction withdrawTx = txWithStatus.transaction;
        byte[] depositBlockHash = null;
        for (int i = 0; i < withdrawTx.inputs.size(); i++) {
          OutPoint outPoint = withdrawTx.inputs.get(i).previousOutput;
          int index = outPoint.index;
          txWithStatus = api.getTransaction(outPoint.txHash);
          Transaction tx = txWithStatus.transaction;
          if (isDepositCell(tx.outputs.get(index), tx.outputsData.get(index))) {
            depositBlockHash = txWithStatus.txStatus.blockHash;
            break;
          }
        }
        if (depositBlockHash == null) {
          throw new RuntimeException("Can find deposit cell");
        }
        byte[] withdrawBlockHash = txWithStatus.txStatus.blockHash;
        depositBlockHeader = api.getHeader(depositBlockHash);
        withdrawBlockHeader = api.getHeader(withdrawBlockHash);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public long calculateDaoMinimumSince() {
      return calculateDaoMinimumSince(depositBlockHeader, withdrawBlockHeader);
    }

    private static long calculateDaoMinimumSince(Header depositBlockHeader, Header withdrawBlockHeader) {
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
      long minimumSinceEpochNumber = depositEpoch.number + lockEpochs;
      long minimumSinceEpochIndex = depositEpoch.index;
      long minimumSinceEpochLength = depositEpoch.length;
      long minimumSince =
          EpochUtils.generateSince(
              minimumSinceEpochLength, minimumSinceEpochIndex, minimumSinceEpochNumber);
      return minimumSince;
    }
  }

  public static class WithdrawInfo {
    OutPoint depositOutPoint;
    long depositBlockNumber;
    byte[] depositBlockHash;

    public WithdrawInfo(Api api, OutPoint depositOutPoint) {
      this.depositOutPoint = depositOutPoint;
      try {
        TransactionWithStatus txWithStatus = api.getTransaction(depositOutPoint.txHash);
        depositBlockHash = txWithStatus.txStatus.blockHash;
        depositBlockNumber = api.getHeader(depositBlockHash).number;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
