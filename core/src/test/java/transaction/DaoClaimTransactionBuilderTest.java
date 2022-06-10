package transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.Network;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.DaoClaimTransactionBuilder;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

class DaoClaimTransactionBuilderTest {
  private static Script daoScript = new Script(Script.DAO_CODE_HASH,
                                               new byte[0],
                                               Script.HashType.TYPE);
  @Test
  public void testBuild() {
    // test comes from https://pudge.explorer.nervos.org/transaction/0xc78ab7cbbae72d8dbb63713018bdb18f465536898615f398685ca96bf239a210
    Network network = Network.TESTNET;
    String daoDepositBlockHash = "0x9f2b44451708cd7dcf671613cf30409b7b2f94dc32a35babb7cdca085a8062e7";
    String daoWithdrawBlockHash = "0x712e7d5e534e4cc4ff6849c42663fc83aa2b47114338ee638cfc0b4286fb3ef5";

    Script lock = new Script(
        Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
        Numeric.hexStringToByteArray("0x115504026fd3af2521e52520e0fe4d66ada41b4c"),
        Script.HashType.TYPE);

    long depositCapacity = 19000000000L;
    long daoMaximumWithdraw = 19491546594L + 936L;
    long daoReward =  daoMaximumWithdraw - depositCapacity;

    CellInput cellInput = new CellInput(new OutPoint(
        Numeric.hexStringToByteArray("0x3db659f4c6bd4ca54c87ea4a8ad78aefa017cb3c105b43dda0156f77f14f6bca"), 0),
                                        0x20028c0033000e6dL);
    CellOutput cellOutput = new CellOutput(19000000000L, lock, daoScript);
    byte[] data = Numeric.hexStringToByteArray("0x0be6020000000000");
    TransactionInput transactionInput = new TransactionInput(cellInput, cellOutput, data);

    TransactionWithScriptGroups txWithGroups = new DaoClaimTransactionBuilder(null, network)
        .addHeaderDep(daoDepositBlockHash)
        .addHeaderDep(daoWithdrawBlockHash)
        .addInput(transactionInput)
        .addDaoReward(daoReward)
        .setFeeRate(1000)
        .setChangeOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtu9n4dh7af6k90mxtqcwq34rvjveres8cvcq8wj")
        .build(0);
    Assertions.assertArrayEquals(
        Numeric.hexStringToByteArray("0x61000000100000005500000061000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080000000000000000000000"),
        txWithGroups.getTxView().witnesses.get(0));
  }
}