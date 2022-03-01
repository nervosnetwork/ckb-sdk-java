package sign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.nervos.ckb.sign.TransactionSigner.TESTNET_TRANSACTION_SIGNER;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.sign.signer.PwSigner;
import org.nervos.ckb.type.transaction.Transaction;

public class PwSignerTest {

  @Test
  void testIsMatched() {
    PwSigner signer = PwSigner.getINSTANCE();

    Assertions.assertTrue(
        signer.isMatched(
            "f8f8a2f43c8376ccb0871305060d7b27b0554d2cc72bccf41b2705608452f315",
            "001d3f1ef827552ae1114027bd3ecf1f086ba0f9"));
    Assertions.assertTrue(
        signer.isMatched(
            "e0ccb2548af279947b452efda4535dd4bcadf756d919701fcd4c382833277f85",
            "adabffb9c27cb4af100ce7bca6903315220e87a2"));
  }

  @Test
  void testSignPwEthereum() {
    // This test transaction is from
    // https://explorer.nervos.org/aggron/transaction/0x8bc55b093c6dfd1b6178683e249841eafb2299cc616468f1d93e22f748aec691

    Transaction tx =
        Transaction.builder()
            .addCellDep("0xec26b0f85ed839ece5f11c4c4e837ec359f5adc4420410f6453b1f6b60fb96a6", 0)
            .addCellDep(
                "0x57a62003daeab9d54aa29b944fc3b451213a5ebdf2e232216a3cfed0dde61b38", 0, "code")
            .addCellDep("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", 0)
            .addCellDep(
                "0xe12877ebd2c3c364dc46c5c992bcfaf4fee33fa13eebdf82c591fc9825aab769", 0, "code")
            .addInput("0x62ffb4a4dc4eae43210bfa157f283fd23a1842101050acff8784503db1100382", 1)
            .addInput("0x62ffb4a4dc4eae43210bfa157f283fd23a1842101050acff8784503db1100382", 2)
            .addWitness(
                "0x55000000100000005500000055000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
            .addWitness("0x10000000100000001000000010000000")
            .addOutput(
                "0x8a94ae8fa",
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63",
                "0xadabffb9c27cb4af100ce7bca6903315220e87a2",
                "0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4",
                "0x7c7f0ee1d582c385342367792946cff3767fe02f26fd7f07dba23ae3c65b28bc")
            .addOutputData("0x00000000000000000000000000000000")
            .build();

    TransactionWithScriptGroups txWithScriptGroup =
        TransactionWithScriptGroups.builder()
            .setTxView(tx)
            .addLockScriptGroup(
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63",
                "0xadabffb9c27cb4af100ce7bca6903315220e87a2",
                0,
                1)
            .build();

    Set<Integer> signedGroupsIndices =
        TESTNET_TRANSACTION_SIGNER.signTransaction(
            txWithScriptGroup, "e0ccb2548af279947b452efda4535dd4bcadf756d919701fcd4c382833277f85");

    assertEquals(1, signedGroupsIndices.size());
    assertEquals(true, signedGroupsIndices.contains(0));

    List<String> witnesses = txWithScriptGroup.getTxView().witnesses;
    assertEquals(2, witnesses.size());
    assertEquals(
        "0x5500000010000000550000005500000041000000fed79d78a964af9ba4151f43e8e513c003fa78a2086c89ec1157714225b85c0a360b9ba06c1acc0c1cb2fced6009b7bf3838daede1c4d6101bdc82b2080fcc0201",
        witnesses.get(0));
  }
}
