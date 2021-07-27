package org.nervos.ckb.crypto.secp256k1;

import static org.nervos.ckb.crypto.secp256k1.SecureRandomUtils.secureRandom;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
 * https://github.com/web3j/web3j/blob/master/crypto/src/main/java/org/web3j/crypto/Keys.java
 */
public class Keys {

  static final int PRIVATE_KEY_SIZE = 32;
  static final int PUBLIC_KEY_SIZE = 64;

  public static final int ADDRESS_SIZE = 160;
  public static final int ADDRESS_LENGTH_IN_HEX = ADDRESS_SIZE >> 2;
  static final int PUBLIC_KEY_LENGTH_IN_HEX = PUBLIC_KEY_SIZE << 1;
  public static final int PRIVATE_KEY_LENGTH_IN_HEX = PRIVATE_KEY_SIZE << 1;

  static {
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  private Keys() {}

  /**
   * Create a keypair using SECP-256k1 curve.
   *
   * <p>Private keypairs are encoded using PKCS8
   *
   * <p>Private keys are encoded using X.509
   */
  static KeyPair createSecp256k1KeyPair()
      throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    return createSecp256k1KeyPair(secureRandom());
  }

  static KeyPair createSecp256k1KeyPair(SecureRandom random)
      throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {

    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
    ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
    if (random != null) {
      keyPairGenerator.initialize(ecGenParameterSpec, random);
    } else {
      keyPairGenerator.initialize(ecGenParameterSpec);
    }
    return keyPairGenerator.generateKeyPair();
  }

  public static ECKeyPair createEcKeyPair()
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
    return createEcKeyPair(secureRandom());
  }

  public static ECKeyPair createEcKeyPair(SecureRandom random)
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
    KeyPair keyPair = createSecp256k1KeyPair(random);
    return ECKeyPair.createWithKeyPair(keyPair);
  }
}
