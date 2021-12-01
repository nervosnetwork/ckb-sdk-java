package org.nervos.ckb.utils.address;

import com.google.common.primitives.Bytes;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Objects;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.address.CodeHashType;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Keys;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

/** @author zjh @Created Date: 2021/7/15 @Description: @Modify by: */
public class AddressTools {

  public static final String MAINNET_CHEQUE_CODE_HASH =
      "0xe4d4ecc6e5f9a059bf2f7a82cca292083aebc0c421566a52484fe2ec51a9fb0c";

  public static final String TESTNET_CHEQUE_CODE_HASH =
      "0x60d5f39efce409c587cb9ea359cefdead650ca128f0bd9cb3855348f98c70d5b";

  public static AddressGenerateResult generateAddress(Network network)
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
    return generateBech32mFullAddress(network);
  }

  /**
   * Short address format is deprecated because it is limited (only support secp256k1_blake160, secp256k1_multisig, anyone_can_pay)
   * and a flaw has been found in its encoding method bech32, which could enable attackers to generate valid but unexpected addresses.
   * For more please check https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md
   */
  @Deprecated
  public static AddressGenerateResult generateShortAddress(Network network)
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
    ECKeyPair ecKeyPair = Keys.createEcKeyPair();

    String privateKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey());
    String publicKey = ECKeyPair.publicKeyFromPrivate(privateKey);
    String blake160 = Hash.blake160(publicKey);

    AddressUtils utils = new AddressUtils(network, CodeHashType.BLAKE160);
    String addresss = utils.generate(blake160);

    AddressGenerateResult result = new AddressGenerateResult();
    result.address = addresss;
    result.lockArgs = blake160;
    result.privateKey = privateKey;

    return result;
  }

  /**
   * Old full address format is deprecated because a flaw has been found in its encoding method bech32, which could enable
   * attackers to generate valid but unexpected addresses.
   * For more please check https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md
   */
  @Deprecated
  public static AddressGenerateResult generateFullAddress(Network network)
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

    AddressGenerateResult a = generateShortAddress(network);
    Script script = AddressTools.parse(a.address).script;
    String fullAddress = AddressGenerator.generateFullAddress(network, script);

    AddressGenerateResult result = new AddressGenerateResult();
    result.address = fullAddress;
    result.lockArgs = a.lockArgs;
    result.privateKey = a.privateKey;

    return result;
  }

  public static AddressGenerateResult generateBech32mFullAddress(Network network)
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

    AddressGenerateResult a = generateShortAddress(network);
    Script script = AddressTools.parse(a.address).script;
    String fullAddress = AddressGenerator.generateBech32mFullAddress(network, script);

    AddressGenerateResult result = new AddressGenerateResult();
    result.address = fullAddress;
    result.lockArgs = a.lockArgs;
    result.privateKey = a.privateKey;

    return result;
  }

  public static String convertDeprecatedAddressToBech32mFullAddress(String address) {
    AddressParseResult a = AddressTools.parse(address);
    return AddressGenerator.generateBech32mFullAddress(a.network, a.script);
  }

  public static String convertPublicKeyToAddress(Network network, String publicKey) {
    return convertPublicKeyToBech32mFullAddress(network, publicKey);
  }

  /**
   * Short address format deprecated because it is limited (only support secp256k1_blake160, secp256k1_multisig, anyone_can_pay)
   * and a flaw has been found in its encoding method bech32, which could enable attackers to generate valid but unexpected addresses.
   * For more please check https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md
   */
  @Deprecated
  public static String convertPublicKeyToShortAddress(Network network, String publicKey) {
    if (!AddressUtils.validatePublicKeyHex(publicKey, true)) {
      throw new IllegalArgumentException("Not a valid compressed public key in hex");
    }
    String blake160 = Hash.blake160(publicKey);
    AddressUtils utils = new AddressUtils(network, CodeHashType.BLAKE160);
    return utils.generate(blake160);
  }

  /**
   * Old full address format deprecated because a flaw has been found in its encoding method bech32, which could enable
   * attackers to generate valid but unexpected addresses.
   * For more please check https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md
   */
  @Deprecated
  public static String convertPublicKeyToFullAddress(Network network, String publicKey) {
    String shortAddress = convertPublicKeyToShortAddress(network, publicKey);
    Script script = AddressTools.parse(shortAddress).script;
    return AddressGenerator.generateFullAddress(network, script);
  }

  public static String convertPublicKeyToBech32mFullAddress(Network network, String publicKey) {
    String shortAddress = convertPublicKeyToShortAddress(network, publicKey);
    Script script = AddressTools.parse(shortAddress).script;
    return AddressGenerator.generateBech32mFullAddress(network, script);
  }

  @Deprecated
  public static String generateAcpAddress(String secp256k1Address) {
    AddressParseResult parseScript = AddressParser.parse(secp256k1Address);
    Network network = AddressParser.parseNetwork(secp256k1Address);
    AddressUtils addressUtils = new AddressUtils(network, CodeHashType.ANYONE_CAN_APY);
    return addressUtils.generate(parseScript.script.args);
  }

  public static String generateChequeAddress(String senderAddress, String receiverAddress) {
    AddressParseResult senderAddressScript = AddressParser.parse(senderAddress);
    AddressParseResult receiverAddressScript = AddressParser.parse(receiverAddress);

    System.out.println(AddressTools.getChequeCodeHash(AddressTools.parseNetwork(senderAddress)));

    byte[] bytes =
        Bytes.concat(
            Numeric.hexStringToByteArray(
                Numeric.cleanHexPrefix(receiverAddressScript.script.computeHash())
                    .substring(0, 40)),
            Numeric.hexStringToByteArray(
                Numeric.cleanHexPrefix(senderAddressScript.script.computeHash()).substring(0, 40)));

    String pubKey = Numeric.toHexStringNoPrefix(bytes);

    String fullAddress =
        AddressGenerator.generate(
            Network.TESTNET,
            new Script(
                AddressTools.getChequeCodeHash(AddressTools.parseNetwork(senderAddress)),
                pubKey,
                Script.TYPE));

    return fullAddress;
  }

  public static AddressParseResult parse(String address) throws AddressFormatException {
    return AddressParser.parse(address);
  }

  public static Network parseNetwork(String address) {
    return AddressParser.parseNetwork(address);
  }

  private static String getChequeCodeHash(Network network) {
    if (Objects.equals(network, Network.MAINNET)) {
      return AddressTools.MAINNET_CHEQUE_CODE_HASH;
    } else {
      return AddressTools.TESTNET_CHEQUE_CODE_HASH;
    }
  }

  public static class AddressGenerateResult {

    public String address;

    public String lockArgs;

    public String privateKey;

    @Override
    public String toString() {
      return "AddressGenerateResult{"
          + "address='"
          + address
          + '\''
          + ", lockArgs='"
          + lockArgs
          + '\''
          + ", privateKey='"
          + privateKey
          + '\''
          + '}';
    }
  }
}
