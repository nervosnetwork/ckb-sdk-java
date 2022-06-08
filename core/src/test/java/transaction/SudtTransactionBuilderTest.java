package transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.Network;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.SudtTransactionBuilder;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.transaction.scriptHandler.Secp256k1Blake160SighashAllScriptHandler;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.nervos.ckb.utils.AmountUtils.SudtAmountToData;
import static org.nervos.ckb.utils.AmountUtils.dataToSudtAmount;

class SudtTransactionBuilderTest {
  byte[] fakeHash = Numeric.hexStringToByteArray("0x0000000000000000000000000000000000000000000000000000000000000000");
  Address sender = Address.decode("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq02cgdvd5mng9924xarf3rflqzafzmzlpsuhh83c");
  Script lock = sender.getScript();
  Script type = new Script(
        Numeric.hexStringToByteArray("0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4"),
        Numeric.hexStringToByteArray("0xae4147ba8412767b3fd9bd16d45dab2fa5df283a6fd68dae5367524daa767ca7"),
        Script.HashType.TYPE);

  @Test
  void testSingleInput() {
    CellOutput output = new CellOutput(501, lock, type);
    CellOutput changeOutput = new CellOutput(0, lock, type);

    Iterator<TransactionInput> iterator = newTransactionInputs();
    TransactionWithScriptGroups txWithGroups = new SudtTransactionBuilder(iterator)
        .registerScriptHandler(new Secp256k1Blake160SighashAllScriptHandler(Network.TESTNET))
        .addOutput(output, BigInteger.valueOf(99))
        .setFeeRate(1000)
        .setChangeOutput(changeOutput)
        .build(null);

    Transaction tx = txWithGroups.getTxView();
    Assertions.assertEquals(1, tx.inputs.size());
    Assertions.assertEquals(2, txWithGroups.scriptGroups.size());
    Assertions.assertEquals(lock, txWithGroups.scriptGroups.get(0).getScript());
    Assertions.assertEquals(2, tx.outputs.size());
    long fee = 100000000000L - tx.outputs.get(0).capacity - tx.outputs.get(1).capacity;
    Assertions.assertEquals(670, fee);
    BigInteger outputSudtAmount = dataToSudtAmount(tx.outputsData.get(0));
    BigInteger changeSudtAmount = dataToSudtAmount(tx.outputsData.get(1));
    Assertions.assertEquals(BigInteger.valueOf(100L), outputSudtAmount.add(changeSudtAmount));
  }

  @Test
  void testMultipleInput() {
    CellOutput output = new CellOutput(501, lock, type);
    CellOutput changeOutput = new CellOutput(0, lock, type);

    Iterator<TransactionInput> iterator = newTransactionInputs();
    TransactionWithScriptGroups txWithGroups = new SudtTransactionBuilder(iterator)
        .registerScriptHandler(new Secp256k1Blake160SighashAllScriptHandler(Network.TESTNET))
        .addOutput(output, BigInteger.valueOf(105L))
        .setFeeRate(1000)
        .setChangeOutput(changeOutput)
        .build(null);

    Transaction tx = txWithGroups.getTxView();
    Assertions.assertEquals(2, tx.inputs.size());
    Assertions.assertEquals(2, txWithGroups.scriptGroups.size());
    Assertions.assertEquals(lock, txWithGroups.scriptGroups.get(0).getScript());
    Assertions.assertEquals(2, tx.outputs.size());
    long fee = 110000000000L - tx.outputs.get(0).capacity - tx.outputs.get(1).capacity;
    Assertions.assertEquals(722, fee);
    BigInteger outputSudtAmount = dataToSudtAmount(tx.outputsData.get(0));
    BigInteger changeSudtAmount = dataToSudtAmount(tx.outputsData.get(1));
    Assertions.assertEquals(BigInteger.valueOf(110L), outputSudtAmount.add(changeSudtAmount));
  }

  private Iterator<TransactionInput> newTransactionInputs() {
    List<TransactionInput> inputs = new ArrayList<>();

    CellInput cellInput = new CellInput(new OutPoint(fakeHash, 0), 0);
    CellOutput cellOutput = new CellOutput(100000000000L, lock, type);  // 1000 CKB
    byte[] data = SudtAmountToData(BigInteger.valueOf(100L));  // 100 SUDT
    inputs.add(new TransactionInput(cellInput, cellOutput, data));

    cellInput = new CellInput(new OutPoint(fakeHash, 0), 0);
    cellOutput = new CellOutput(10000000000L, lock, type); // 100 CKB
    data = SudtAmountToData(BigInteger.valueOf(10L));  // 10 SUDT
    inputs.add(new TransactionInput(cellInput, cellOutput, data));

    return inputs.iterator();
  }
}