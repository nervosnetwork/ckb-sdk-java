package org.nervos.ckb.crypto.secp256k1;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Objects;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.nervos.ckb.utils.Numeric;

/** Created by duanyytop on 2019-01-31. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class ECKeyPair {
  private final BigInteger privateKey;
  private final BigInteger publicKey;

  public ECKeyPair(BigInteger privateKey, BigInteger publicKey) {
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }

  public BigInteger getPrivateKey() {
    return privateKey;
  }

  public BigInteger getPublicKey() {
    return publicKey;
  }

  public static ECKeyPair createWithKeyPair(KeyPair keyPair) {
    BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
    BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

    BigInteger privateKeyValue = privateKey.getD();

    byte[] publicKeyBytes = publicKey.getQ().getEncoded(false);
    BigInteger publicKeyValue =
        new BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.length));

    return new ECKeyPair(privateKeyValue, publicKeyValue);
  }

  public static ECKeyPair createWithPrivateKey(BigInteger privateKey) {
    return new ECKeyPair(privateKey, Sign.publicKeyFromPrivate(privateKey));
  }

  public static ECKeyPair createWithPrivateKey(byte[] privateKey) {
    return createWithPrivateKey(Numeric.toBigInt(privateKey));
  }

  /**
   * Sign a hash with the private key of this key pair.
   *
   * @param transactionHash the hash to sign
   * @return An {@link ECDSASignature} of the hash
   */
  public ECDSASignature sign(byte[] transactionHash) {
    ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));

    ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateKey, Sign.CURVE);
    signer.init(true, privKey);
    BigInteger[] components = signer.generateSignature(transactionHash);

    return new ECDSASignature(components[0], components[1]).toCanonicalised();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ECKeyPair ecKeyPair = (ECKeyPair) o;

    if (!Objects.equals(privateKey, ecKeyPair.privateKey)) {
      return false;
    }

    return Objects.equals(publicKey, ecKeyPair.publicKey);
  }

  @Override
  public int hashCode() {
    int result = privateKey != null ? privateKey.hashCode() : 0;
    result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
    return result;
  }
}
