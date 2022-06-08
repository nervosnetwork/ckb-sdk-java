package org.nervos.ckb.transaction.scriptHandler;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;
import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.Numeric;

import java.util.*;

public class DaoScriptHandler implements ScriptHandler {
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
    return true;
  }
}
