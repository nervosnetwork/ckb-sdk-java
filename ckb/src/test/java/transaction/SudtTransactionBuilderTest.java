package transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.Network;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.SudtTransactionBuilder;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.nervos.ckb.utils.AmountUtils.dataToSudtAmount;
import static org.nervos.ckb.utils.AmountUtils.sudtAmountToData;

class SudtTransactionBuilderTest {
  byte[] sudtArgs = Numeric.hexStringToByteArray("0xae4147ba8412767b3fd9bd16d45dab2fa5df283a6fd68dae5367524daa767ca7");
  byte[] fakeHash = Numeric.hexStringToByteArray("0x0000000000000000000000000000000000000000000000000000000000000000");
  Address sender = Address.decode("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq02cgdvd5mng9924xarf3rflqzafzmzlpsuhh83c");
  Script lock = sender.getScript();
  Script type = new Script(
      Numeric.hexStringToByteArray("0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4"),
      sudtArgs,
      Script.HashType.TYPE);
 
  @Test
  void testBalance() {
    Iterator<TransactionInput> iterator = newTransactionInputs();
    TransactionWithScriptGroups txWithGroups = new SudtTransactionBuilder(iterator,
                                                                          Network.TESTNET,
                                                                          SudtTransactionBuilder.TransactionType.TRANSFER,
                                                                          sudtArgs)
        .addSudtOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdamwzrffgc54ef48493nfd2sd0h4cjnxg4850up",
                       1L)
        .setFeeRate(1000)
        .setChangeOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdamwzrffgc54ef48493nfd2sd0h4cjnxg4850up")
        .build();

    Transaction tx = txWithGroups.getTxView();
    BigInteger amount1 = dataToSudtAmount(tx.outputsData.get(0));
    BigInteger amount2 = dataToSudtAmount(tx.outputsData.get(1));
    Assertions.assertEquals(
        BigInteger.valueOf(100L),
        amount1.add(amount2));
  }

  private Iterator<TransactionInput> newTransactionInputs() {
    List<TransactionInput> inputs = new ArrayList<>();

    CellInput cellInput = new CellInput(new OutPoint(fakeHash, 0), 0);
    CellOutput cellOutput = new CellOutput(100000000000L, lock, type);  // 1000 CKB
    byte[] data = sudtAmountToData(BigInteger.valueOf(100L));  // 100 SUDT
    inputs.add(new TransactionInput(cellInput, cellOutput, data));

    cellInput = new CellInput(new OutPoint(fakeHash, 1), 0);
    cellOutput = new CellOutput(10000000000L, lock, type); // 100 CKB
    data = sudtAmountToData(BigInteger.valueOf(10L));  // 10 SUDT
    inputs.add(new TransactionInput(cellInput, cellOutput, data));

    return inputs.iterator();
  }
}