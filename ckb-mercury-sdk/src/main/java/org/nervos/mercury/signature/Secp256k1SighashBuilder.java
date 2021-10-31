package org.nervos.mercury.signature;

import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.mercury.model.resp.MercuryScriptGroup;

public class Secp256k1SighashBuilder {
  private Transaction transaction;

  public Secp256k1SighashBuilder(Transaction transaction) {
    this.transaction = transaction;
  }

  public void sign(MercuryScriptGroup scriptGroup, String privateKey) {
    List groupWitnesses = scriptGroup.getGroupWitnesses();

    String txHash = transaction.computeHash();
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(txHash));
    blake2b.update(
        new UInt64(Numeric.hexStringToByteArray(scriptGroup.getWitness()).length).toBytes());
    blake2b.update(Numeric.hexStringToByteArray(scriptGroup.getWitness()));

    for (int i = 1; i < groupWitnesses.size(); i++) {
      byte[] bytes = Numeric.hexStringToByteArray((String) groupWitnesses.get(i));
      blake2b.update(new UInt64(bytes.length).toBytes());
      blake2b.update(bytes);
    }
    String message = blake2b.doFinalString();
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);

    Witness signedWitness = new Witness();
    signedWitness.lock =
        Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());

    int replaceIndexStart = scriptGroup.getOffset() * 2 + 2;
    int replaceIndexEnd = Witness.SIGNATURE_PLACEHOLDER.length() + replaceIndexStart;
    String newWitness =
        scriptGroup.getWitness().substring(0, replaceIndexStart)
            + Numeric.cleanHexPrefix(signedWitness.lock)
            + scriptGroup.getWitness().substring(replaceIndexEnd);
    transaction.witnesses.set(scriptGroup.getWitnessIndex(), newWitness);
  }

  public Transaction buildTx() {
    return transaction;
  }
}
