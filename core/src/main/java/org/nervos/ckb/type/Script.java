package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Utils;

import java.util.Arrays;

import static org.nervos.ckb.utils.MoleculeConverter.packByte32;
import static org.nervos.ckb.utils.MoleculeConverter.packBytes;

public class Script {
  public static final byte[] SECP256_BLAKE160_SIGNHASH_ALL_CODE_HASH =
      Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8");
  public static final byte[] SECP256_BLAKE160_MULTISIG_ALL_CODE_HASH =
      Numeric.hexStringToByteArray("0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8");
  public static final byte[] ANY_CAN_PAY_CODE_HASH_MAINNET =
      Numeric.hexStringToByteArray("0xd369597ff47f29fbc0d47d2e3775370d1250b85140c670e4718af712983a2354");
  public static final byte[] ANY_CAN_PAY_CODE_HASH_TESTNET =
      Numeric.hexStringToByteArray("0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356");

  public byte[] codeHash;
  public byte[] args;
  public HashType hashType;

  public Script() {
  }

  public Script(byte[] codeHash, byte[] args) {
    this(codeHash, args, HashType.DATA);
  }

  public Script(byte[] codeHash, byte[] args, HashType hashType) {
    this.codeHash = codeHash;
    this.args = args;
    this.hashType = hashType;
  }

  public byte[] computeHash() {
    Blake2b blake2b = new Blake2b();
    blake2b.update(this.pack().toByteArray());
    return blake2b.doFinal();
  }

  public long occupiedCapacity() {
    int byteSize = 1;
    if (codeHash != null) {
      byteSize += codeHash.length;
    }
    if (args != null) {
      byteSize += args.length;
    }
    return Utils.ckbToShannon(byteSize);
  }

  public org.nervos.ckb.type.concrete.Script pack() {
    return org.nervos.ckb.type.concrete.Script.builder()
        .setCodeHash(packByte32(codeHash))
        .setArgs(packBytes(args))
        .setHashType(hashType.pack())
        .build();
  }

  public static Script generateSecp256K1Blake160SignhashAllScript(ECKeyPair publicKey) {
    // CKB uses encoded public keys of compressed form (with prefix 0x04)
    // See https://en.bitcoin.it/wiki/Elliptic_Curve_Digital_Signature_Algorithm for details
    byte[] publicKeyBytes = publicKey.getEncodedPublicKey(true);
    byte[] hash = Blake2b.digest(publicKeyBytes);
    hash = Arrays.copyOfRange(hash, 0, 20);
    Script scrip = new Script(
        SECP256_BLAKE160_SIGNHASH_ALL_CODE_HASH,
        hash,
        HashType.TYPE
    );
    return scrip;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Script script = (Script) o;

    if (!Arrays.equals(codeHash, script.codeHash)) return false;
    if (!Arrays.equals(args, script.args)) return false;
    return hashType == script.hashType;
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(codeHash);
    result = 31 * result + Arrays.hashCode(args);
    result = 31 * result + hashType.hashCode();
    return result;
  }

  public enum HashType {
    @SerializedName("data")
    DATA(0x00),
    @SerializedName("type")
    TYPE(0x01),
    @SerializedName("data1")
    DATA1(0x02);

    private byte byteValue;

    HashType(int byteValue) {
      this.byteValue = (byte) byteValue;
    }

    public byte pack() {
      return byteValue;
    }

    public static HashType unpack(byte value) {
      switch (value) {
        case 0x00:
          return DATA;
        case 0x01:
          return TYPE;
        case 0x02:
          return DATA1;
        default:
          throw new NullPointerException();
      }
    }
  }
}
