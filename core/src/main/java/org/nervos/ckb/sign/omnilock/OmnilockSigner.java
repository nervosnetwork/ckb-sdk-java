package org.nervos.ckb.sign.omnilock;

import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.ScriptSigner;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.sign.signer.Secp256k1Blake160SighashAllSigner;
import org.nervos.ckb.type.Transaction;

import java.util.Arrays;

public class OmnilockSigner implements ScriptSigner {
  @Override
  public boolean signTransaction(Transaction transaction, ScriptGroup scriptGroup, Context context) {
    OmnilockWitnessLock omnilockWitnessLock = sign(transaction, scriptGroup, context);
    if (omnilockWitnessLock == null) {
      return false;
    } else {
      // TODO: set omnilockWitnessLock back to witness field in transaction
      return true;
    }
  }

  public static OmnilockWitnessLock sign(Transaction transaction, ScriptGroup scriptGroup, Context context) {
    byte[] args = scriptGroup.getScript().args;
    AuthenticationArgs authenticationArgs = AuthenticationArgs.decode(args);
    args = Arrays.copyOfRange(args, 21, args.length);
    OmnilockArgs omnilockArgs = OmnilockArgs.decode(args);
    OmnilockWitnessLock witnessLock = new OmnilockWitnessLock();

    if (omnilockArgs.isAdminModeEnabled() && context.getPayload() instanceof OmnilockIdentity) {
      OmnilockIdentity omnilockIdentity = (OmnilockIdentity) context.getPayload();
      witnessLock.setOmnilockIdentity(omnilockIdentity);
      if (omnilockIdentity.getFlag() == OmnilockIdentity.OmnilockFlag.CKB_SECP256K1_BLAKE160) {
        // TODO: sign by Secp256k1Blake160SighashAllSigner
      }
    } else {
      byte[] signature = new byte[0];
      // TODO: compute signature and put it to witnessLock
      ECKeyPair keyPair = context.getKeyPair();
      byte[] authContent = authenticationArgs.getAuthContent();
      switch (authenticationArgs.getFlag()) {
        case CKB_SECP256K1_BLAKE160:
          if (Secp256k1Blake160SighashAllSigner.isMatched(keyPair, authContent)) {
            signature = Secp256k1Blake160SighashAllSigner.signTransaction(transaction, scriptGroup, keyPair);
          } else {
            return null;
          }
          break;
        case ETHEREUM:
          // TODO: sign by PwSigner
          break;
        case EOS:
          break;
        case TRON:
          break;
        case BITCOIN:
          break;
        case DOGECOIN:
          break;
        case CKB_MULTI_SIG:
          if (context.getPayload() instanceof Secp256k1Blake160MultisigAllSigner.MultisigScript) {
            Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript = (Secp256k1Blake160MultisigAllSigner.MultisigScript) context.getPayload();
            if (Secp256k1Blake160MultisigAllSigner.isMatched(keyPair, authContent, multisigScript)) {
              signature = Secp256k1Blake160MultisigAllSigner.signTransaction(transaction, scriptGroup, keyPair, multisigScript);
              // TODO: read current witness in transaction and set signature back
            } else {
              return null;
            }
          } else {
            return null;
          }
          break;
        case LOCK_SCRIPT_HASH:
          // Do nothing
          break;
        case EXEC:
          throw new UnsupportedOperationException("not support EXEC mode");
        case DYNAMIC_LINKING:
          throw new UnsupportedOperationException("not support DYNAMIC LINKING mode");
      }
      witnessLock.setSignature(signature);
    }
    return witnessLock;
  }
}
