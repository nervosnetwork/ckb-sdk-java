package org.nervos.ckb.transaction.handler;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;
import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.utils.Numeric;

import java.util.Arrays;
import java.util.List;

public class Secp256k1Blake160SighashAllScriptHandler implements ScriptHandler {
  private List<CellDep> cellDeps;
  private byte[] codeHash;

  public Secp256k1Blake160SighashAllScriptHandler() {
  }

  public List<CellDep> getCellDeps() {
    return cellDeps;
  }

  public void setCellDeps(List<CellDep> cellDeps) {
    this.cellDeps = cellDeps;
  }

  public byte[] getCodeHash() {
    return codeHash;
  }

  public void setCodeHash(byte[] codeHash) {
    this.codeHash = codeHash;
  }

  @Override
  public void init(Network network) {
    OutPoint outPoint = new OutPoint();
    if (network == Network.MAINNET) {
      outPoint.txHash = Numeric.hexStringToByteArray("0x71a7ba8fc96349fea0ed3a5c47992e3b4084b031a42264a018e0072e8172e46c");
      outPoint.index = 0;
    } else if (network == Network.TESTNET) {
      outPoint.txHash = Numeric.hexStringToByteArray("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37");
      outPoint.index = 0;
    } else {
      throw new IllegalArgumentException("Unsupported network");
    }
    CellDep cellDep = new CellDep();
    cellDep.outPoint = outPoint;
    cellDep.depType = CellDep.DepType.DEP_GROUP;
    cellDeps = Arrays.asList(cellDep);
    codeHash = Script.SECP256K1_BLAKE160_SIGNHASH_ALL_CODE_HASH;
  }

  private boolean isMatched(Script script) {
    if (script == null) {
      return false;
    }
    return Arrays.equals(script.codeHash, codeHash);
  }

  @Override
  public boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context) {
    if (scriptGroup == null || !isMatched(scriptGroup.getScript())) {
      return false;
    }
    // set witness placeholder
    int index = scriptGroup.getInputIndices().get(0);
    byte[] lock = new byte[65];
    txBuilder.setWitness(index, WitnessArgs.Type.LOCK, lock);
    // add celldeps
    txBuilder.addCellDeps(cellDeps);
    return true;
  }

}
