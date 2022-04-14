package org.nervos.mercury.signature;

import java.nio.charset.StandardCharsets;

import org.nervos.ckb.crypto.Keccak256;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.resp.MercuryScriptGroup;

public class TransactionSigner {
  private Transaction transaction;

  public TransactionSigner(Transaction transaction) {
    this.transaction = transaction;
  }

  public void Secp256Blake2bSign(MercuryScriptGroup scriptGroup, String privateKey) {
    //    List groupWitnesses = scriptGroup.getGroupWitnesses();
    //
    //    byte[] txHash = transaction.computeHash();
    //    Blake2b blake2b = new Blake2b();
    //    blake2b.update(txHash);
    //    blake2b.update(
    //        new UInt64(Numeric.hexStringToByteArray(scriptGroup.getWitness()).length).toBytes());
    //    blake2b.update(Numeric.hexStringToByteArray(scriptGroup.getWitness()));
    //
    //    for (int i = 1; i < groupWitnesses.size(); i++) {
    //      byte[] bytes = Numeric.hexStringToByteArray((String) groupWitnesses.get(i));
    //      blake2b.update(new UInt64(bytes.length).toBytes());
    //      blake2b.update(bytes);
    //    }
    //    String message = blake2b.doFinalString();
    //    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    //
    //    Witness signedWitness = new Witness();
    //    signedWitness.lock =
    //        Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature();
    //
    //    int replaceIndexStart = scriptGroup.getOffset() * 2 + 2;
    //    int replaceIndexEnd = Witness.SIGNATURE_PLACEHOLDER.length * 2 + replaceIndexStart;
    //    String newWitness =
    //        scriptGroup.getWitness().substring(0, replaceIndexStart)
    //            + Numeric.toHexStringNoPrefix(signedWitness.lock)
    //            + scriptGroup.getWitness().substring(replaceIndexEnd);
    //    transaction.witnesses.set(scriptGroup.getWitnessIndex(), newWitness);
  }

  public void KeccakEthereumPersonalSign(MercuryScriptGroup scriptGroup, String privateKey) {
    //    List groupWitnesses = scriptGroup.getGroupWitnesses();
    //
    //    byte[] txHash = transaction.computeHash();
    //
    //    Keccak256 keccak256 = new Keccak256();
    //    keccak256.update(txHash);
    //    keccak256.update(
    //        new UInt64(Numeric.hexStringToByteArray(scriptGroup.getWitness()).length).toBytes());
    //    keccak256.update(Numeric.hexStringToByteArray(scriptGroup.getWitness()));
    //
    //    for (int i = 1; i < groupWitnesses.size(); i++) {
    //      byte[] bytes = Numeric.hexStringToByteArray((String) groupWitnesses.get(i));
    //      keccak256.update(new UInt64(bytes.length).toBytes());
    //      keccak256.update(bytes);
    //    }
    //    byte[] messageBytes = keccak256.doFinalBytes();
    //
    //    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    //    byte[] signature = ethereumPersonalSign(messageBytes, ecKeyPair);
    //
    //    Witness signedWitness = new Witness();
    //    signedWitness.lock = signature;
    //
    //    int replaceIndexStart = scriptGroup.getOffset() * 2 + 2;
    //    int replaceIndexEnd = Witness.SIGNATURE_PLACEHOLDER.length * 2 + replaceIndexStart;
    //    String newWitness =
    //        scriptGroup.getWitness().substring(0, replaceIndexStart)
    //            + Numeric.toHexStringNoPrefix(signedWitness.lock)
    //            + scriptGroup.getWitness().substring(replaceIndexEnd);
    //    transaction.witnesses.set(scriptGroup.getWitnessIndex(), newWitness);
  }

  private static byte[] ethereumPersonalSign(byte[] message, ECKeyPair ecKeyPair) {
    Keccak256 keccak256 = new Keccak256();
    byte[] ETH_PERSONAL_SIGN =
        new String("\u0019Ethereum Signed Message:\n" + message.length)
            .getBytes(StandardCharsets.UTF_8);

    byte[] rawMessage = new byte[ETH_PERSONAL_SIGN.length + message.length];
    System.arraycopy(ETH_PERSONAL_SIGN, 0, rawMessage, 0, ETH_PERSONAL_SIGN.length);
    System.arraycopy(message, 0, rawMessage, ETH_PERSONAL_SIGN.length, message.length);
    keccak256.update(rawMessage);
    byte[] messageToSign = keccak256.doFinalBytes();
    return Sign.signMessage(messageToSign, ecKeyPair).getSignature();
  }

  public Transaction buildTx() {
    return transaction;
  }
}
