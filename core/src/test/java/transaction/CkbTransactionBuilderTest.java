package transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.Network;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.transaction.scriptHandler.Secp256k1Blake160SighashAllScriptHandler;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class CkbTransactionBuilderTest {
  byte[] fakeHash = Numeric.hexStringToByteArray("0x0000000000000000000000000000000000000000000000000000000000000000");
  Address sender = Address.decode("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq02cgdvd5mng9924xarf3rflqzafzmzlpsuhh83c");
  Script lock = sender.getScript();

  @Test
  void testSingleInput() {
    Iterator<TransactionInput> iterator = newTransactionInputs();
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(iterator)
        .registerScriptHandler(new Secp256k1Blake160SighashAllScriptHandler(Network.TESTNET))
        .addOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r",
                   50100000000L)
        .setFeeRate(1000)
        .setChangeOutput(sender.encode())
        .build(null);

    Transaction tx = txWithGroups.getTxView();
    Assertions.assertEquals(1, tx.inputs.size());
    Assertions.assertEquals(1, txWithGroups.scriptGroups.size());
    Assertions.assertEquals(lock, txWithGroups.scriptGroups.get(0).getScript());
    Assertions.assertEquals(2, tx.outputs.size());
    long fee = 100000000000L - tx.outputs.get(0).capacity - tx.outputs.get(1).capacity;
    Assertions.assertEquals(468, fee);
  }

  @Test
  void testMultipleInputs() {
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r";

    Iterator<TransactionInput> iterator = newTransactionInputs();
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(iterator)
        .registerScriptHandler(new Secp256k1Blake160SighashAllScriptHandler(Network.TESTNET))
        .addOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r",
                   100000000000L)
        .setFeeRate(1000)
        .setChangeOutput(sender)
        .build(null);

    Transaction tx = txWithGroups.getTxView();
    Assertions.assertEquals(2, tx.inputs.size());
    Assertions.assertEquals(1, txWithGroups.scriptGroups.size());
    Assertions.assertEquals(lock, txWithGroups.scriptGroups.get(0).getScript());
    Assertions.assertEquals(2, tx.outputs.size());
    long fee = 110000000000L - tx.outputs.get(0).capacity - tx.outputs.get(1).capacity;
    Assertions.assertEquals(520, fee);
  }

  private Iterator<TransactionInput> newTransactionInputs() {
    List<TransactionInput> inputs = new ArrayList<>();

    CellInput cellInput = new CellInput(new OutPoint(fakeHash, 0), 0);
    CellOutput cellOutput = new CellOutput(100000000000L, lock);  // 1000 CKB
    inputs.add(new TransactionInput(cellInput, cellOutput));

    cellInput = new CellInput(new OutPoint(fakeHash, 0), 0);
    cellOutput = new CellOutput(10000000000L, lock); // 100 CKB
    inputs.add(new TransactionInput(cellInput, cellOutput));

    return inputs.iterator();
  }
}