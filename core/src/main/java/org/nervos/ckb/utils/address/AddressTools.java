package org.nervos.ckb.utils.address;

import com.google.common.primitives.Bytes;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;

import java.util.Arrays;

public class AddressTools {
  @Deprecated
  public static Address generateAcpAddress(String secp256k1Address) {
    Address secp256k1AddressObj = Address.decode(secp256k1Address);
    byte[] codeHash = secp256k1AddressObj.getNetwork() == Network.MAINNET
                      ? Script.ANY_CAN_PAY_CODE_HASH_MAINNET
                      : Script.ANY_CAN_PAY_CODE_HASH_TESTNET;
    byte[] args = secp256k1AddressObj.getScript().args;
    Script script = new Script(codeHash, args, Script.HashType.TYPE);

    return new Address(script, secp256k1AddressObj.getNetwork());
  }

  public static Address generateChequeAddress(String encodedSenderAddress, String encodeReceiverAddress) {
    Address senderAddress = Address.decode(encodedSenderAddress);
    Address receiverAddress = Address.decode(encodeReceiverAddress);

    byte[] args = Bytes.concat(
        Arrays.copyOfRange(receiverAddress.script.computeHash(), 0, 20),
        Arrays.copyOfRange(senderAddress.script.computeHash(), 0, 20));

    Network network = senderAddress.getNetwork();
    Script script = new Script(
        network == Network.MAINNET ? Script.CHEQUE_CODE_HASH_MAINNET : Script.CHEQUE_CODE_HASH_TESTNET,
        args,
        Script.HashType.TYPE);

    return new Address(script, network);
  }
}
