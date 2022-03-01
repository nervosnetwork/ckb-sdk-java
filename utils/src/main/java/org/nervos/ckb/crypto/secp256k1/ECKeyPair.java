package org.nervos.ckb.crypto.secp256k1;

import static org.nervos.ckb.crypto.secp256k1.Sign.CURVE;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Objects;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.nervos.ckb.utils.Numeric;

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

  public static ECKeyPair createWithPrivateKey(BigInteger privateKey, boolean compressed) {
    return new ECKeyPair(privateKey, publicKeyFromPrivate(privateKey, compressed));
  }

  public static ECKeyPair createWithPrivateKey(String privateKeyHex, boolean compressed) {
    return new ECKeyPair(
        Numeric.toBigInt(privateKeyHex),
        publicKeyFromPrivate(Numeric.toBigInt(privateKeyHex), compressed));
  }

  public static ECKeyPair createWithPrivateKey(BigInteger privateKey) {
    return new ECKeyPair(privateKey, publicKeyFromPrivate(privateKey));
  }

  public static ECKeyPair createWithPrivateKey(byte[] privateKey) {
    return createWithPrivateKey(Numeric.toBigInt(privateKey));
  }

  /**
   * Returns public key from the given private key.
   *
   * @param privateKey the private key to derive the public key from
   * @return BigInteger encoded public key
   */
  public static BigInteger publicKeyFromPrivate(BigInteger privateKey) {
    return publicKeyFromPrivate(privateKey, true);
  }

  public static String publicKeyFromPrivate(String privateKeyHex) {
    return '0' + publicKeyFromPrivate(Numeric.toBigInt(privateKeyHex), true).toString(16);
  }

  /**
   * Returns public key from the given private key.
   *
   * @param privateKey the private key to derive the public key from
   * @return BigInteger encoded public key
   */
  public static BigInteger publicKeyFromPrivate(BigInteger privateKey, boolean compressed) {
    ECPoint point = publicPointFromPrivate(privateKey);

    byte[] encoded = point.getEncoded(compressed);
    return new BigInteger(1, Arrays.copyOfRange(encoded, compressed ? 0 : 1, encoded.length));
  }

  /** Returns public key point from the given private key. */
  private static ECPoint publicPointFromPrivate(BigInteger privateKey) {
    if (privateKey.bitLength() > CURVE.getN().bitLength()) {
      privateKey = privateKey.mod(CURVE.getN());
    }
    return new FixedPointCombMultiplier().multiply(CURVE.getG(), privateKey);
  }

  // See: https://stackoverflow.com/questions/4407779/biginteger-to-byte
  public byte[] getPublicKeyBytes() {
    byte[] array = publicKey.toByteArray();
    if (array[0] == 0) {
      byte[] tmp = new byte[array.length - 1];
      System.arraycopy(array, 1, tmp, 0, tmp.length);
      array = tmp;
    }
    return array;
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
