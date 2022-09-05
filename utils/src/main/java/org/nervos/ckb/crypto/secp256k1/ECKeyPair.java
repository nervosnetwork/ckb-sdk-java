package org.nervos.ckb.crypto.secp256k1;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.nervos.ckb.utils.Numeric;

import java.math.BigInteger;
import java.security.KeyPair;

import static org.nervos.ckb.crypto.secp256k1.Sign.CURVE;

/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * https://github.com/web3j/web3j/blob/master/crypto/src/main/java/org/web3j/crypto/ECKeyPair.java
 */
public class ECKeyPair {
  public static final int PRIVATE_KEY_SIZE = 32;
  private final BigInteger privateKey;
  private final BigInteger publicKey;

  public ECKeyPair(BigInteger privateKey) {
    this.privateKey = privateKey;
    byte[] encoded = publicKeyFromPrivate(privateKey, true);
    this.publicKey = new BigInteger(1, encoded);
  }

  public BigInteger getPrivateKey() {
    return privateKey;
  }

  public byte[] getEncodedPrivateKey() {
    byte[] encoded = Numeric.toBytesPadded(privateKey, PRIVATE_KEY_SIZE);
    return encoded;
  }

  public BigInteger getPublicKey() {
    return publicKey;
  }

  public byte[] getEncodedPublicKey(boolean compressed) {
    return publicKeyFromPrivate(privateKey, compressed);
  }

  public static ECKeyPair create(KeyPair keyPair) {
    BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
    BigInteger privateKeyValue = privateKey.getD();
    return new ECKeyPair(privateKeyValue);
  }

  public static ECKeyPair create(BigInteger privateKey) {
    return new ECKeyPair(privateKey);
  }

  public static ECKeyPair create(String privateKey) {
    return create(Numeric.toBigInt(privateKey));
  }

  public static ECKeyPair create(byte[] privateKey) {
    return create(Numeric.toBigInt(privateKey));
  }

  /**
   * Returns public key from the given private key.
   *
   * @param privateKey the private key to derive the public key from
   * @param compressed whether the public key should be compressed
   * @return byte array encoded public key
   */
  public static byte[] publicKeyFromPrivate(BigInteger privateKey, boolean compressed) {
    ECPoint point = publicPointFromPrivate(privateKey);
    return point.getEncoded(compressed);
  }

  /** Returns public key point from the given private key. */
  private static ECPoint publicPointFromPrivate(BigInteger privateKey) {
    if (privateKey.bitLength() > CURVE.getN().bitLength()) {
      privateKey = privateKey.mod(CURVE.getN());
    }
    return new FixedPointCombMultiplier().multiply(CURVE.getG(), privateKey);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ECKeyPair keyPair = (ECKeyPair) o;

    if (!privateKey.equals(keyPair.privateKey)) return false;
    return publicKey.equals(keyPair.publicKey);
  }

  @Override
  public int hashCode() {
    int result = privateKey.hashCode();
    result = 31 * result + publicKey.hashCode();
    return result;
  }
}
