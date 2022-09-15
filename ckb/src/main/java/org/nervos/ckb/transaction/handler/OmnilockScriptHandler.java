package org.nervos.ckb.transaction.handler;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.omnilock.OmnilockConfig;
import org.nervos.ckb.sign.omnilock.OmnilockIdentity;
import org.nervos.ckb.sign.omnilock.OmnilockWitnessLock;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;
import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.utils.Numeric;

import java.util.Arrays;
import java.util.Objects;

public class OmnilockScriptHandler implements ScriptHandler {
  private CellDep cellDep;
  private byte[] codeHash;

  // AUTH Mode:
  // 0x00
  // 0x01 ~ 0x05: fixed length witness
  // 0x06: Multisig context
  // 0xFC: input - set by user??
  // 0xFD?
  // 0xFE


  // ADMINISTRATOR Mode:
  // 0x00: celldeps - adminList cell
  // 0xFC: celldeps - adminList cell
  //       input - proof cell
  // 0xFC: input - adminList cell
  //       input - proof cell

  public OmnilockScriptHandler(CellDep cellDep, byte[] codeHash) {
    this.cellDep = cellDep;
    this.codeHash = codeHash;
  }

  public OmnilockScriptHandler(Network network) {
    OutPoint outPoint = new OutPoint();
    if (network == Network.MAINNET) {
      outPoint.txHash = Numeric.hexStringToByteArray("0xdfdb40f5d229536915f2d5403c66047e162e25dedd70a79ef5164356e1facdc8");
      outPoint.index = 0;
      codeHash = Script.OMNILOCK_CODE_HASH_MAINNET;
    } else if (network == Network.TESTNET) {
      outPoint.txHash = Numeric.hexStringToByteArray("0x27b62d8be8ed80b9f56ee0fe41355becdb6f6a40aeba82d3900434f43b1c8b60");
      outPoint.index = 0;
      codeHash = Script.OMNILOCK_CODE_HASH_TESTNET;
    } else {
      throw new IllegalArgumentException("Unsupported network: " + network);
    }
    cellDep = new CellDep();
    cellDep.outPoint = outPoint;
    cellDep.depType = CellDep.DepType.CODE;
  }

  @Override
  public boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context) {
    if (scriptGroup == null || !isMatched(scriptGroup.getScript())) {
      return false;
    }
    OmnilockConfig omnilockConfig;
    if (context instanceof OmnilockConfig) {
      omnilockConfig = (OmnilockConfig) context;
    } else {
      return false;
    }
    txBuilder.addCellDep(cellDep);
    OmnilockConfig.Mode mode = omnilockConfig.getMode();
    switch (mode) {
      case AUTH:
        return buildTransactionForAuthMode(txBuilder, scriptGroup, omnilockConfig);
      case ADMINISTRATOR:
        return buildTransactionForAdministratorMode(txBuilder, scriptGroup, omnilockConfig);
      default:
        throw new IllegalArgumentException("Omnilock mode is null");
    }
  }

  private boolean buildTransactionForAuthMode(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, OmnilockConfig omnilockConfig) {
    OmnilockWitnessLock omnilockWitnessLock = new OmnilockWitnessLock();
    switch (omnilockConfig.getAuthenticationArgs().getFlag()) {
      case CKB_SECP256K1_BLAKE160:
        omnilockWitnessLock.setSignature(new byte[65]);
        break;
      case ETHEREUM:
        throw new UnsupportedOperationException("Ethereum");
      case EOS:
        throw new UnsupportedOperationException("EOS");
      case TRON:
        throw new UnsupportedOperationException("TRON");
      case BITCOIN:
        throw new UnsupportedOperationException("Bitcoin");
      case DOGECOIN:
        throw new UnsupportedOperationException("Dogecoin");
      case CKB_MULTI_SIG:
        Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript = omnilockConfig.getMultisigScript();
        Objects.requireNonNull(multisigScript);
        omnilockWitnessLock.setSignature(multisigScript.witnessPlaceholderInLock());
        break;
      case LOCK_SCRIPT_HASH:
        // TODO: set input by script handler OR by user???
        break;
      case EXEC:
        throw new UnsupportedOperationException("Exec");
      case DYNAMIC_LINKING:
        throw new UnsupportedOperationException("Dynamic linking");
      default:
        throw new IllegalArgumentException("Unknown auth flag " + omnilockConfig.getOmnilockArgs().getFlag());
    }
    byte[] lock = omnilockWitnessLock.pack().toByteArray();
    int index = scriptGroup.getInputIndices().get(0);
    txBuilder.setWitness(index, WitnessArgs.Type.LOCK, lock);
    return true;
  }

  private boolean buildTransactionForAdministratorMode(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, OmnilockConfig omnilockConfig) {
    // set celldep
    CellDep adminListCell = omnilockConfig.getAdminListCell();
    Objects.requireNonNull(adminListCell);
    txBuilder.addCellDep(adminListCell);

    // set lock to witness
    OmnilockIdentity.OmnilockFlag administratorMode = omnilockConfig.getOmnilockIdentity().getFlag();
    byte[] signature = null;
    switch (administratorMode) {
      case CKB_SECP256K1_BLAKE160:
        signature = new byte[65];
        break;
      case LOCK_SCRIPT_HASH:
        // TODO: set input by script handler OR by user???
        break;
      default:
        throw new IllegalArgumentException("Unknown administrator mode " + administratorMode);
    }

    OmnilockWitnessLock omnilockWitnessLock = new OmnilockWitnessLock();
    omnilockWitnessLock.setSignature(signature);
    omnilockWitnessLock.setOmnilockIdentity(omnilockConfig.getOmnilockIdentity());

    byte[] lock = omnilockWitnessLock.pack().toByteArray();
    int index = scriptGroup.getInputIndices().get(0);
    txBuilder.setWitness(index, WitnessArgs.Type.LOCK, lock);
    return true;
  }

  private boolean isMatched(Script script) {
    if (script == null) {
      return false;
    }
    return Arrays.equals(script.codeHash, codeHash);
  }
}
