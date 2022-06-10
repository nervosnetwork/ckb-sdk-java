package org.nervos.ckb.transaction.scriptHandler;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.EpochUtils;
import org.nervos.ckb.utils.MoleculeConverter;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;
import java.util.*;

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

  private List<CellDep> getCellDeps() {
    return cellDeps;
  }

  public static boolean isDepositCell(CellOutput output, byte[] data) {
    return DAO_SCRIPT.equals(output.type) && Arrays.equals(data, DEPOSIT_CELL_DATA);
  }

  @Override
  public boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context) {
    if (scriptGroup == null || !isMatched(scriptGroup.getScript())) {
      return false;
    }

    Transaction tx = txBuilder.getTx();
    // add celldeps
    Set<CellDep> cellDeps = new HashSet<>(tx.cellDeps);
    cellDeps.addAll(getCellDeps());
    tx.cellDeps = new ArrayList<>(cellDeps);

    if (context instanceof ClaimInfo) {
      ClaimInfo info = (ClaimInfo) context;
      int index = scriptGroup.getInputIndices().get(0);
      // add header deps
      int depositHeaderDepIndex = setHeaderDep(tx.headerDeps, info.depositBlockHeader.hash);
      setHeaderDep(tx.headerDeps, info.withdrawBlockHeader.hash);
      // update witness
      byte[] witness = tx.witnesses.get(index);
      WitnessArgs witnessArgs = getWitnessArgs(witness);
      witnessArgs.setInputType(
          MoleculeConverter.packUint64(depositHeaderDepIndex).toByteArray());
      tx.witnesses.set(index, witnessArgs.pack().toByteArray());
      // update input since
      CellInput input = tx.inputs.get(index);
      input.since = info.calculateDaoMinimalSince();
    } else if (context instanceof WithdrawInfo) {
      WithdrawInfo info = (WithdrawInfo) context;
      setHeaderDep(tx.headerDeps, info.depositBlockHash);
    }
    return true;
  }

  private int setHeaderDep(List<byte[]> headerDeps, byte[] headerDep) {
    int index = -1;
    for (int i = 0; i < headerDeps.size(); i++) {
      if (Arrays.equals(headerDep, headerDeps.get(i))) {
        index = i;
        break;
      }
    }
    if (index == -1) {
      index = headerDeps.size();
      headerDeps.add(headerDep);
    }
    return index;
  }

  private WitnessArgs getWitnessArgs(byte[] originalWitness) {
    WitnessArgs witnessArgs;
    if (originalWitness == null || originalWitness.length == 0) {
      witnessArgs = new WitnessArgs();
    } else {
      witnessArgs = WitnessArgs.unpack(originalWitness);
    }
    return witnessArgs;
  }

  public static class ClaimInfo {
    Header depositBlockHeader;
    Header withdrawBlockHeader;
    OutPoint withdrawOutpoint;
    private Api api;

    public ClaimInfo(Api api, OutPoint withdrawOutpoint) {
      this.withdrawOutpoint = withdrawOutpoint;
      this.api = api;
      try {
        TransactionWithStatus txWithStatus = api.getTransaction(withdrawOutpoint.txHash);
        Transaction withdrawTx = txWithStatus.transaction;
        byte[] withdrawBlockHash = txWithStatus.txStatus.blockHash;
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
        depositBlockHeader = api.getHeader(depositBlockHash);
        withdrawBlockHeader = api.getHeader(withdrawBlockHash);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public long calculateDaoMinimalSince() {
      return calculateDaoMinimalSince(depositBlockHeader, withdrawBlockHeader);
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
  }

  public static class WithdrawInfo {
    Api api;
    OutPoint depositOutpoint;
    long depositBlockNumber;
    byte[] depositBlockHash;

    public WithdrawInfo(Api api, OutPoint depositOutpoint) {
      this.api = api;
      this.depositOutpoint = depositOutpoint;
      try {
        TransactionWithStatus txWithStatus = api.getTransaction(depositOutpoint.txHash);
        depositBlockHash = txWithStatus.txStatus.blockHash;
        depositBlockNumber = api.getHeader(depositBlockHash).number;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
