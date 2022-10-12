package org.nervos.ckb.transaction.handler;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.omnilock.OmnilockWitnessLock;
import org.nervos.ckb.sign.signer.OmnilockSigner;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;
import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.WitnessArgs;

import java.util.Arrays;
import java.util.Objects;

public class OmnilockScriptHandler implements ScriptHandler {
  private CellDep cellDep;
  private CellDep singleSignCellDep;
  private CellDep multiSignCellDep;
  private byte[] codeHash;

  public OmnilockScriptHandler() {
  }

  @Override
  public ScriptHandler init(Network network) {
    if (network == Network.MAINNET) {
      codeHash = Script.OMNILOCK_CODE_HASH_MAINNET;
      cellDep = new CellDep("0xdfdb40f5d229536915f2d5403c66047e162e25dedd70a79ef5164356e1facdc8", 0, CellDep.DepType.CODE);
      singleSignCellDep = new CellDep("0x71a7ba8fc96349fea0ed3a5c47992e3b4084b031a42264a018e0072e8172e46c", 0, CellDep.DepType.DEP_GROUP);
      multiSignCellDep = new CellDep("0x71a7ba8fc96349fea0ed3a5c47992e3b4084b031a42264a018e0072e8172e46c", 1, CellDep.DepType.DEP_GROUP);
    } else if (network == Network.TESTNET) {
      codeHash = Script.OMNILOCK_CODE_HASH_TESTNET;
      cellDep = new CellDep("0x27b62d8be8ed80b9f56ee0fe41355becdb6f6a40aeba82d3900434f43b1c8b60", 0, CellDep.DepType.CODE);
      singleSignCellDep = new CellDep("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", 0, CellDep.DepType.DEP_GROUP);
      multiSignCellDep = new CellDep("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", 1, CellDep.DepType.DEP_GROUP);
    } else {
      throw new IllegalArgumentException("Unsupported network: " + network);
    }
    return this;
  }

  public void setCellDep(CellDep cellDep) {
    this.cellDep = cellDep;
  }

  public void setSingleSignCellDep(CellDep singleSignCellDep) {
    this.singleSignCellDep = singleSignCellDep;
  }

  public void setMultiSignCellDep(CellDep multiSignCellDep) {
    this.multiSignCellDep = multiSignCellDep;
  }

  public void setCodeHash(byte[] codeHash) {
    this.codeHash = codeHash;
  }

  public CellDep getCellDep() {
    return cellDep;
  }

  public CellDep getSingleSignCellDep() {
    return singleSignCellDep;
  }

  public CellDep getMultiSignCellDep() {
    return multiSignCellDep;
  }

  public byte[] getCodeHash() {
    return codeHash;
  }

  @Override
  public boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context) {
    if (scriptGroup == null || !isMatched(scriptGroup.getScript())) {
      return false;
    }
    OmnilockSigner.Configuration configuration;
    if (context instanceof OmnilockSigner.Configuration) {
      configuration = (OmnilockSigner.Configuration) context;
    } else {
      return false;
    }
    txBuilder.addCellDep(cellDep);
    OmnilockSigner.Configuration.Mode mode = configuration.getMode();
    switch (mode) {
      case AUTH:
        return buildTransactionForAuthMode(txBuilder, scriptGroup, configuration);
      case ADMINISTRATOR:
        return buildTransactionForAdministratorMode(txBuilder, scriptGroup, configuration);
      default:
        throw new IllegalArgumentException("Omnilock mode is null");
    }
  }

  private boolean buildTransactionForAuthMode(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, OmnilockSigner.Configuration configuration) {
    OmnilockWitnessLock omnilockWitnessLock = new OmnilockWitnessLock();
    switch (configuration.getOmnilockArgs().getAuthenticationArgs().getFlag()) {
      case CKB_SECP256K1_BLAKE160:
        txBuilder.addCellDep(singleSignCellDep);
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
        txBuilder.addCellDep(multiSignCellDep);
        Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript = configuration.getMultisigScript();
        Objects.requireNonNull(multisigScript);
        omnilockWitnessLock.setSignature(multisigScript.witnessEmptyPlaceholderInLock());
        break;
      case LOCK_SCRIPT_HASH:
        break;
      case EXEC:
        throw new UnsupportedOperationException("Exec");
      case DYNAMIC_LINKING:
        throw new UnsupportedOperationException("Dynamic linking");
      default:
        throw new IllegalArgumentException("Unknown auth flag " + configuration.getOmnilockArgs().getOmniArgs().getFlag());
    }
    int index = scriptGroup.getInputIndices().get(0);
    txBuilder.setWitness(index, WitnessArgs.Type.LOCK, omnilockWitnessLock.packAsEmptyPlaceholder());
    return true;
  }

  private boolean buildTransactionForAdministratorMode(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, OmnilockSigner.Configuration configuration) {
    throw new UnsupportedOperationException();
  }

  private boolean isMatched(Script script) {
    if (script == null) {
      return false;
    }
    return Arrays.equals(script.codeHash, codeHash);
  }
}
