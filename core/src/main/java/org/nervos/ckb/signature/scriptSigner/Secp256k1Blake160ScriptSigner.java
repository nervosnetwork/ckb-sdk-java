package org.nervos.ckb.signature.scriptSigner;

import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.signature.Context;
import org.nervos.ckb.signature.Contexts;
import org.nervos.ckb.signature.ScriptGroup;
import org.nervos.ckb.signature.ScriptSigner;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;

public class Secp256k1Blake160ScriptSigner implements ScriptSigner {
  private static final int WITNESS_OFFSET_IN_BYTE = 20;
  private static final int SIGNATURE_LENGTH_IN_BYTE = 65;

//  @Override
//  // TODO: need more verification for private key
//  public boolean canSign(String scriptArgs, Context context) {
//    if (scriptArgs == null || context == null) {
//      return false;
//    }
//    return scriptArgs.equals(Hash.blake160(context.getPrivateKey()));
//  }

  @Override
  public boolean signTx(Transaction transaction, ScriptGroup scriptGroup, Context context) {
    if (scriptGroup == null
        || scriptGroup.getInputIndices() == null
        || scriptGroup.getOutputIndices().size() == 0) {
      return false;
    }

    String privateKey = context.getPrivateKey();

    String txHash = transaction.computeHash();
    List<String> witnesses = transaction.witnesses;
    String witness = witnesses.get(0);

    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(txHash));
    blake2b.update(new UInt64(Numeric.hexStringToByteArray(witness).length).toBytes());
    blake2b.update(Numeric.hexStringToByteArray(witness));

    for (int i = 1; i < witnesses.size(); i++) {
      byte[] bytes = Numeric.hexStringToByteArray((String) witnesses.get(i));
      blake2b.update(new UInt64(bytes.length).toBytes());
      blake2b.update(bytes);
    }
    String message = blake2b.doFinalString();
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);

    String signatureInHex =
        Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());

    // +2 for 0x prefix
    witness =
        witness.substring(0, 2 + WITNESS_OFFSET_IN_BYTE * 2)
            + Numeric.cleanHexPrefix(signatureInHex)
            + witness.substring(2 + WITNESS_OFFSET_IN_BYTE * 2 + SIGNATURE_LENGTH_IN_BYTE * 2);

    witnesses.set(0, witness);
    return true;
  }
}
