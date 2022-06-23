package org.nervos.ckb.crypto.secp256k1;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

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
 *  https://github.com/web3j/web3j/blob/master/crypto/src/main/java/org/web3j/crypto/Sign.java
 */

public class Sign {

  public static final int SIGN_LENGTH = 65;

  private static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
  static final ECDomainParameters CURVE =
      new ECDomainParameters(
          CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());
  static final BigInteger HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);

  public static SignatureData signMessage(byte[] message, ECKeyPair keyPair) {
    return signMessage(message, keyPair, false);
  }

  public static SignatureData signMessage(byte[] message, ECKeyPair keyPair, boolean isHash) {
    BigInteger publicKey = keyPair.getPublicKey();

    byte[] messageHash = isHash ? Blake2b.digest(message) : message;

    ECDSASignature sig = sign(keyPair, messageHash);
    // Now we have to work backwards to figure out the recId needed to recover the signature.
    int recId = -1;
    for (int i = 0; i < 4; i++) {
      BigInteger k = recoverFromSignature(i, sig, messageHash);
      if (k != null && k.equals(publicKey)) {
        recId = i;
        break;
      }
    }
    if (recId == -1) {
      throw new RuntimeException(
          "Could not construct a recoverable key. This should never happen.");
    }

    int headerByte = recId;

    // 1 header + 32 bytes for R + 32 bytes for S
    byte v = (byte) headerByte;
    byte[] r = Numeric.toBytesPadded(sig.r, 32);
    byte[] s = Numeric.toBytesPadded(sig.s, 32);

    return new SignatureData(v, r, s);
  }
 
  /**
   * Verify that the provided precondition holds true.
   *
   * @param assertionResult assertion value
   * @param errorMessage    error message if precondition failure
   */
  private static void verifyPrecondition(boolean assertionResult, String errorMessage) {
    if (!assertionResult) {
      throw new RuntimeException(errorMessage);
    }
  }

  /**
   * Given the components of a signature and a selector value, recover and return the public key
   * that generated the signature according to the algorithm in SEC1v2 section 4.1.6.
   *
   * <p>The recId is an index from 0 to 3 which indicates which of the 4 possible keys is the
   * correct one. Because the key recovery operation yields multiple potential keys, the correct key
   * must either be stored alongside the signature, or you must be willing to try each recId in turn
   * until you find one that outputs the key you are expecting.
   *
   * <p>If this method returns null it means recovery was not possible and recId should be iterated.
   *
   * <p>Given the above two points, a correct usage of this method is inside a for loop from 0 to 3,
   * and if the output is null OR a key that is not the one you expect, you try again with the next
   * recId.
   *
   * @param recId   Which possible key to recover.
   * @param sig     the R and S components of the signature, wrapped.
   * @param message Hash of the data that was signed.
   * @return An ECKey containing only the public part, or null if recovery wasn't possible.
   */
  private static BigInteger recoverFromSignature(int recId, ECDSASignature sig, byte[] message) {
    verifyPrecondition(recId >= 0, "recId must be positive");
    verifyPrecondition(sig.r.signum() >= 0, "r must be positive");
    verifyPrecondition(sig.s.signum() >= 0, "s must be positive");
    verifyPrecondition(message != null, "message cannot be null");

    // 1.0 For j from 0 to h   (h == recId here and the loop is outside this function)
    //   1.1 Let x = r + jn
    BigInteger n = CURVE.getN(); // Curve order.
    BigInteger i = BigInteger.valueOf((long) recId / 2);
    BigInteger x = sig.r.add(i.multiply(n));
    //   1.2. Convert the integer x to an octet string X of length mlen using the conversion
    //        routine specified in Section 2.3.7, where mlen = ⌈(log2 p)/8⌉ or mlen = ⌈m/8⌉.
    //   1.3. Convert the octet string (16 set binary digits)||X to an elliptic curve point R
    //        using the conversion routine specified in Section 2.3.4. If this conversion
    //        routine outputs "invalid", then do another iteration of Step 1.
    //
    // More concisely, what these points mean is to use X as a compressed public key.
    BigInteger prime = SecP256K1Curve.q;
    if (x.compareTo(prime) >= 0) {
      // Cannot have point co-ordinates larger than this as everything takes place modulo Q.
      return null;
    }
    // Compressed keys require you to know an extra bit of data about the y-coord as there are
    // two possibilities. So it's encoded in the recId.
    ECPoint R = decompressKey(x, (recId & 1) == 1);
    //   1.4. If nR != point at infinity, then do another iteration of Step 1 (callers
    //        responsibility).
    if (!R.multiply(n).isInfinity()) {
      return null;
    }
    //   1.5. Compute e from M using Steps 2 and 3 of ECDSA signature verification.
    BigInteger e = new BigInteger(1, message);
    //   1.6. For k from 1 to 2 do the following.   (loop is outside this function via
    //        iterating recId)
    //   1.6.1. Compute a candidate public key as:
    //               Q = mi(r) * (sR - eG)
    //
    // Where mi(x) is the modular multiplicative inverse. We transform this into the following:
    //               Q = (mi(r) * s ** R) + (mi(r) * -e ** G)
    // Where -e is the modular additive inverse of e, that is z such that z + e = 0 (mod n).
    // In the above equation ** is point multiplication and + is point addition (the EC group
    // operator).
    //
    // We can find the additive inverse by subtracting e from zero then taking the mod. For
    // example the additive inverse of 3 modulo 11 is 8 because 3 + 8 mod 11 = 0, and
    // -3 mod 11 = 8.
    BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
    BigInteger rInv = sig.r.modInverse(n);
    BigInteger srInv = rInv.multiply(sig.s).mod(n);
    BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
    ECPoint q = ECAlgorithms.sumOfTwoMultiplies(CURVE.getG(), eInvrInv, R, srInv);

    byte[] qBytes = q.getEncoded(true);
    return new BigInteger(1, qBytes);
  }

  /** Decompress a compressed public key (x co-ord and low-bit of y-coord). */
  private static ECPoint decompressKey(BigInteger xBN, boolean yBit) {
    X9IntegerConverter x9 = new X9IntegerConverter();
    byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.getCurve()));
    compEnc[0] = (byte) (yBit ? 0x03 : 0x02);
    return CURVE.getCurve().decodePoint(compEnc);
  }

  /**
   * Sign a hash with the private key of this key pair.
   *
   * @param transactionHash the hash to sign
   * @return An {@link ECDSASignature} of the hash
   */
  private static ECDSASignature sign(ECKeyPair ecKeyPair, byte[] transactionHash) {
    ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));

    ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(ecKeyPair.getPrivateKey(), CURVE);
    signer.init(true, privKey);
    BigInteger[] components = signer.generateSignature(transactionHash);

    return new ECDSASignature(components[0], components[1]).toCanonicalised();
  }

  public static class SignatureData {
    private final byte v;
    private final byte[] r;
    private final byte[] s;

    public SignatureData(byte v, byte[] r, byte[] s) {
      this.v = v;
      this.r = r;
      this.s = s;
    }

    public byte getV() {
      return v;
    }

    public byte[] getR() {
      return r;
    }

    public byte[] getS() {
      return s;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      SignatureData that = (SignatureData) o;

      if (v != that.v) {
        return false;
      }
      if (!Arrays.equals(r, that.r)) {
        return false;
      }
      return Arrays.equals(s, that.s);
    }

    @Override
    public int hashCode() {
      int result = (int) v;
      result = 31 * result + Arrays.hashCode(r);
      result = 31 * result + Arrays.hashCode(s);
      return result;
    }

    public byte[] getSignature() {
      byte[] sig = new byte[SIGN_LENGTH];
      System.arraycopy(r, 0, sig, 0, 32);
      System.arraycopy(s, 0, sig, 32, 32);
      sig[64] = v;
      return sig;
    }

    // https://crypto.stackexchange.com/questions/1795/how-can-i-convert-a-der-ecdsa-signature-to-asn-1
    public byte[] getDerSignature() {
      int rLen = (r[0] & 0xFF) > 0x7F ? r.length + 1 : r.length;
      int sLen = (s[0] & 0xFF) > 0x7F ? s.length + 1 : s.length;
      int len = 6 + rLen + sLen;
      byte[] sig = new byte[len];

      sig[0] = 0x30;
      sig[1] = (byte) (len - 2);
      sig[2] = 0x02;
      sig[3] = (byte) rLen;

      if ((r[0] & 0xFF) > 0x7F) {
        sig[4] = 0x00;
        System.arraycopy(r, 0, sig, 5, rLen - 1);
      } else {
        System.arraycopy(r, 0, sig, 4, rLen);
      }

      sig[4 + rLen] = 0x02;
      sig[4 + rLen + 1] = (byte) sLen;

      if ((s[0] & 0xFF) > 0x7F) {
        sig[4 + rLen + 2] = 0x00;
        System.arraycopy(s, 0, sig, 4 + rLen + 3, sLen - 1);
      } else {
        System.arraycopy(s, 0, sig, 4 + rLen + 2, sLen);
      }

      return sig;
    }
  }
}
