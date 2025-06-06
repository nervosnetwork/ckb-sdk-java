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
  public static final byte[] SECP256K1_BLAKE160_SIGNHASH_ALL_CODE_HASH =
      Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8");
  // Multisig Script deployed on Genesis Block
  // https://explorer.nervos.org/script/0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8/type
  public static final byte[] SECP256K1_BLAKE160_MULTISIG_ALL_CODE_HASH_LEGACY =
      Numeric.hexStringToByteArray("0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8");
  // Latest multisig script, Enhance multisig handling for optional since value
  // https://github.com/nervosnetwork/ckb-system-scripts/pull/99
  // https://explorer.nervos.org/script/0x36c971b8d41fbd94aabca77dc75e826729ac98447b46f91e00796155dddb0d29/data1
  public static final byte[] SECP256K1_BLAKE160_MULTISIG_ALL_CODE_HASH_V2=
      Numeric.hexStringToByteArray("0x36c971b8d41fbd94aabca77dc75e826729ac98447b46f91e00796155dddb0d29");
  public static final byte[] ANY_CAN_PAY_CODE_HASH_MAINNET =
      Numeric.hexStringToByteArray("0xd369597ff47f29fbc0d47d2e3775370d1250b85140c670e4718af712983a2354");
  public static final byte[] ANY_CAN_PAY_CODE_HASH_TESTNET =
      Numeric.hexStringToByteArray("0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356");
  public static final byte[] CHEQUE_CODE_HASH_MAINNET =
      Numeric.hexStringToByteArray("0xe4d4ecc6e5f9a059bf2f7a82cca292083aebc0c421566a52484fe2ec51a9fb0c");
  public static final byte[] CHEQUE_CODE_HASH_TESTNET =
      Numeric.hexStringToByteArray("0x60d5f39efce409c587cb9ea359cefdead650ca128f0bd9cb3855348f98c70d5b");
  public static final byte[] PW_LOCK_CODE_HASH_MAINNET =
      Numeric.hexStringToByteArray("0xbf43c3602455798c1a61a596e0d95278864c552fafe231c063b3fabf97a8febc");
  public static final byte[] PW_LOCK_CODE_HASH_TESTNET =
      Numeric.hexStringToByteArray("0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63");
  public static final byte[] SUDT_CODE_HASH_MAINNET =
      Numeric.hexStringToByteArray("0x5e7a36a77e68eecc013dfa2fe6a23f3b6c344b04005808694ae6dd45eea4cfd5");
  public static final byte[] SUDT_CODE_HASH_TESTNET =
      Numeric.hexStringToByteArray("0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4");
  public static final byte[] OMNILOCK_CODE_HASH_MAINNET =
      Numeric.hexStringToByteArray("0x9b819793a64463aed77c615d6cb226eea5487ccfc0783043a587254cda2b6f26");
  public static final byte[] OMNILOCK_CODE_HASH_TESTNET =
      Numeric.hexStringToByteArray("0xf329effd1c475a2978453c8600e1eaf0bc2087ee093c3ee64cc96ec6847752cb");
  public static final byte[] DAO_CODE_HASH =
      Numeric.hexStringToByteArray("0x82d76d1b75fe2fd9a27dfbaa65a039221a380d76c926f378d3f81cf3e7e13f2e");

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

  public static Script generateSecp256K1Blake160SignhashAllScript(ECKeyPair keyPair) {
    byte[] publicKey = keyPair.getEncodedPublicKey(true);
    return generateSecp256K1Blake160SignhashAllScript(publicKey);
  }

  public static Script generateSecp256K1Blake160SignhashAllScript(byte[] publicKey) {
    // CKB uses encoded public keys of compressed form (with prefix 0x04)
    // See https://en.bitcoin.it/wiki/Elliptic_Curve_Digital_Signature_Algorithm for details
    byte[] hash = Blake2b.digest(publicKey);
    hash = Arrays.copyOfRange(hash, 0, 20);
    Script scrip = new Script(
        SECP256K1_BLAKE160_SIGNHASH_ALL_CODE_HASH,
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
    DATA1(0x02),
    @SerializedName("data2")
    DATA2(0x04);

    private final byte byteValue;

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
        case 0x04:
          return DATA2;
        default:
          throw new NullPointerException();
      }
    }
  }
}
