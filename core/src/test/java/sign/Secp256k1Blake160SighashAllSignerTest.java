package sign;

import static org.junit.jupiter.api.Assertions.*;
import static org.nervos.ckb.sign.TransactionSigner.TESTNET_TRANSACTION_SIGNER;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.*;
import org.nervos.ckb.sign.signer.Secp256k1Blake160SighashAllSigner;
import org.nervos.ckb.type.transaction.Transaction;

public class Secp256k1Blake160SighashAllSignerTest {

  @Test
  void testIsMatched() {
    Secp256k1Blake160SighashAllSigner signer = Secp256k1Blake160SighashAllSigner.getInstance();

    assertTrue(
        signer.isMatched(
            "9d8ca87d75d150692211fa62b0d30de4d1ee6c530d5678b40b8cedacf0750d0f",
            "0xaf0b41c627807fbddcee75afa174d5a7e5135ebd"));
    assertFalse(
        signer.isMatched(
            "0x9d8ca87d75d150692211fa62b0d30de4d1ee6c530d5678b40b8cedacf0750d0f",
            "0x0450340178ae277261a838c89f9ccb76a190ed4b"));
    assertFalse(signer.isMatched(null, "0xaf0b41c627807fbddcee75afa174d5a7e5135ebd"));
    assertFalse(
        signer.isMatched("9d8ca87d75d150692211fa62b0d30de4d1ee6c530d5678b40b8cedacf0750d0f", null));
  }

  @Test
  void testSecp256k1Blake160OneInput() {
    SignerChecker.signAndCheck("secp256k1_blake16_sighash_all_one_input");
  }

  @Test
  void testSecp256k1Blake160OneGroup() {
    SignerChecker.signAndCheck("secp256k1_blake16_sighash_all_one_group");
  }

  @Test
  void testTwoSecp256k1Blake160Scripts() {
    // This test transaction is from
    // https://explorer.nervos.org/aggron/transaction/0x91ea0744a38483842c288d18f2e59f5d90d479d32acc2deae4350a96de15a76a
    Transaction tx =
        Transaction.builder()
            .addCellDep("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", 0)
            .addInput("0x42dc1a2cba2a12303f4b901a78fa029b742a56f1e46b9026302d384dafdc1200", 1)
            .addInput("0x000b0db9fdca54cc2f9426e3868117c727d9274eb800ed54c054247f4d3ef9d2", 4)
            .addInput("0xb312eb6c372c6a35d18fd178bee5a4826ad08d3685b66edffd5cdd8e1542055a", 1)
            .addWitness(
                "0x55000000100000005500000055000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
            .addWitness(
                "0x55000000100000005500000055000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
            .addWitness("0x10000000100000001000000010000000")
            .addOutput(
                "0x3c5985ef6",
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                "0xaf0b41c627807fbddcee75afa174d5a7e5135ebd")
            .addOutput(
                "0x34e62d2f0",
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                "0xc5f94bc585ba8ddb71cfbc94c79d64d728affc0b")
            .addOutput(
                "0x2b369ea81",
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                "0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b")
            .addOutputData("0x")
            .addOutputData("0x")
            .addOutputData("0x")
            .build();

    TransactionWithScriptGroups txWithScriptGroup =
        TransactionWithScriptGroups.builder()
            .setTxView(tx)
            .addLockScriptGroup(
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                "0xaf0b41c627807fbddcee75afa174d5a7e5135ebd",
                0)
            .addLockScriptGroup(
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                "0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b",
                1,
                2)
            .build();

    Set<Integer> signedGroupsIndices =
        TESTNET_TRANSACTION_SIGNER.signTransaction(
            txWithScriptGroup,
            "6fc935dad260867c749cf1ba6602d5f5ed7fb1131f1beb65be2d342e912eaafe",
            "9d8ca87d75d150692211fa62b0d30de4d1ee6c530d5678b40b8cedacf0750d0f");

    assertEquals(2, signedGroupsIndices.size());
    assertEquals(true, signedGroupsIndices.contains(0));
    assertEquals(true, signedGroupsIndices.contains(1));

    List<String> witnesses = txWithScriptGroup.getTxView().witnesses;
    assertEquals(3, witnesses.size());
    assertEquals(
        "0x5500000010000000550000005500000041000000860e2e7a830991dae28c2207263b22e6d66a41572bd315b41528bcaf6e26056a76d8c0e43157feed32741a4b038a665461ba93a91f6ce72d43034cc4fe8b9d3b00",
        witnesses.get(0));
    assertEquals(
        "0x550000001000000055000000550000004100000022c333e42676e6749a806f952ca12079c2e7e634af2a5737288d2973645edae61db81fe84410c8fd20a5d0743d60429335aeb0a486c6c804fbc5424add690ec901",
        witnesses.get(1));
    assertEquals("0x10000000100000001000000010000000", witnesses.get(2));
  }
}
