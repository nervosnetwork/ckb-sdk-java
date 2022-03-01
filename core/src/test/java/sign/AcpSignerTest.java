package sign;

import static org.nervos.ckb.sign.TransactionSigner.TESTNET_TRANSACTION_SIGNER;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.type.transaction.Transaction;

public class AcpSignerTest {
  @Test
  void testACP() {
    // This test transaction is from
    // https://explorer.nervos.org/aggron/transaction/0x5be4c85879bbe5c9eceb799c0ef09e9c839a14ec4f3ae9b107bf7b5c5c3f24ce
    Transaction tx =
        Transaction.builder()
            .addCellDep("0xec26b0f85ed839ece5f11c4c4e837ec359f5adc4420410f6453b1f6b60fb96a6", 0)
            .addCellDep("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", 0)
            .addCellDep(
                "0xe12877ebd2c3c364dc46c5c992bcfaf4fee33fa13eebdf82c591fc9825aab769", 0, "code")
            .addInput("0xd4287542c102b57476db7a63f2932ad5fabcf4c4b21de0c965f94afcc3255560", 1)
            .addWitness(
                "0x55000000100000005500000055000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
            .addOutput(
                "0x35458af00",
                "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
                "0x1fc78a5fb55d20ebac89478f6181ce92bc06a741",
                "0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4",
                "0x96b4d3f7cc380ed4fe5597a2ab49a396920de4f4da153b0c6a0eae378229400c")
            .addOutput(
                "0xf6eab7ac8e",
                "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
                "0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b",
                "0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4",
                "0xc772f4d885ca6285d87d82b8edc1643df9f3ce63c40d0f81f2a38c147328d430")
            .addOutputData("0x00000000000000000000000000000000")
            .addOutputData("0x00000000000000000000000000000000")
            .build();

    TransactionWithScriptGroups txWithScriptGroup =
        TransactionWithScriptGroups.builder()
            .setTxView(tx)
            .addLockScriptGroup(
                "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
                "0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b",
                0)
            .build();

    Set<Integer> signedGroupsIndices =
        TESTNET_TRANSACTION_SIGNER.signTransaction(
            txWithScriptGroup, "6fc935dad260867c749cf1ba6602d5f5ed7fb1131f1beb65be2d342e912eaafe");

    Assertions.assertEquals(1, signedGroupsIndices.size());
    Assertions.assertEquals(true, signedGroupsIndices.contains(0));

    List<String> witnesses = txWithScriptGroup.getTxView().witnesses;
    Assertions.assertEquals(1, witnesses.size());
    Assertions.assertEquals(
        "0x550000001000000055000000550000004100000061fa2f71f90620340ab9c3826623cf792830d1f8cc0a809ba537663d8b7ab0bf7399aa7c21553c3ad6af8b10b5bb51960dcbaa4ed9f1b034bceec52e0d12b3fa00",
        witnesses.get(0));
  }
}
