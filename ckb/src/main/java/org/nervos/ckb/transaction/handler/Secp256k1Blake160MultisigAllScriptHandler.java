package org.nervos.ckb.transaction.handler;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Secp256k1Blake160MultisigAllScriptHandler implements ScriptHandler {
  private Network network;

  public Secp256k1Blake160MultisigAllScriptHandler() {
  }

  public List<CellDep> getCellDeps(MultisigVersion multisigVersion) {
    OutPoint outPoint = new OutPoint();
    if (this.network == Network.MAINNET) {
      switch (multisigVersion) {
        case Legacy:
          outPoint.txHash = Numeric.hexStringToByteArray("0x71a7ba8fc96349fea0ed3a5c47992e3b4084b031a42264a018e0072e8172e46c");
          outPoint.index = 1;
          break;
        case V2:
          outPoint.txHash = Numeric.hexStringToByteArray("0x6888aa39ab30c570c2c30d9d5684d3769bf77265a7973211a3c087fe8efbf738");
          outPoint.index = 0;
          break;
        default:
          throw new IllegalArgumentException("Unsupported multisig version");
      }
    } else if (this.network == Network.TESTNET) {
      switch (multisigVersion) {
        case Legacy:
          outPoint.txHash = Numeric.hexStringToByteArray("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37");
          outPoint.index = 1;
          break;
        case V2:
          outPoint.txHash = Numeric.hexStringToByteArray("0x2eefdeb21f3a3edf697c28a52601b4419806ed60bb427420455cc29a090b26d5");
          outPoint.index = 0;
          break;
        default:
          throw new IllegalArgumentException("Unsupported multisig version");
      }
    } else {
      throw new IllegalArgumentException("Unsupported network");
    }
    CellDep cellDep = new CellDep();
    cellDep.outPoint = outPoint;
    cellDep.depType = CellDep.DepType.DEP_GROUP;
    return Arrays.asList(cellDep);
  }


  @Override
  public void init(Network network) {
    this.network = network;
  }

  private Optional<MultisigVersion> isMatched(Script script) {
    if (script == null) {
      return Optional.empty();
    }

    if (Arrays.equals(script.codeHash, MultisigVersion.Legacy.codeHash()) && script.hashType == MultisigVersion.Legacy.hashType()) {
      return Optional.of(MultisigVersion.Legacy);
    } else if (Arrays.equals(script.codeHash, MultisigVersion.V2.codeHash()) && script.hashType == MultisigVersion.V2.hashType()) {
      return Optional.of(MultisigVersion.V2);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context) {
    if (scriptGroup == null) {
      return false;
    }
    Optional<MultisigVersion> multisigVersion = isMatched(scriptGroup.getScript());
    if (!multisigVersion.isPresent()) {
      return false;
    }

    List<CellDep> cellDeps = this.getCellDeps(multisigVersion.get());

    Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript;
    if (context instanceof Secp256k1Blake160MultisigAllSigner.MultisigScript) {
      multisigScript = (Secp256k1Blake160MultisigAllSigner.MultisigScript) context;
    } else {
      return false;
    }

    // set witness placeholder
    int index = scriptGroup.getInputIndices().get(0);
    byte[] lock = multisigScript.witnessPlaceholderInLock();
    txBuilder.setWitness(index, WitnessArgs.Type.LOCK, lock);

    // add celldeps
    txBuilder.addCellDeps(cellDeps);
    return true;
  }
}
