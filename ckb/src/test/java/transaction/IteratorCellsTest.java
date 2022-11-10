package transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.AbstractInputIterator;
import org.nervos.ckb.transaction.IteratorCells;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.*;

class IteratorCellsTest {
  private String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq03ewkvsva4cchhntydu648l7lyvn9w2cctnpask";
  private Address senderAddress = Address.decode(sender);

  @Test
  void applyOffChainTransaction() throws IOException {
    IteratorCells.TransactionInputWithBlockNumber input = getRandomTransactionInput(300);
    IteratorCells cells = new IteratorCells();
    cells.getOffChainLiveCells().add(input);

    Transaction tx = new Transaction();
    tx.inputs.add(input.input);
    tx.outputs.add(getRandomOutput());
    tx.outputsData.add(new byte[0]);
    tx.outputs.add(getRandomOutput());
    tx.outputsData.add(new byte[0]);

    Assertions.assertEquals(0, cells.getUsedLiveCells().size());
    Assertions.assertEquals(1, cells.getOffChainLiveCells().size());
    cells.applyOffChainTransaction(500, tx);
    // test correct cell management
    Assertions.assertEquals(1, cells.getUsedLiveCells().size());  // + 1
    CellInput txInput1 = tx.inputs.get(0);
    Assertions.assertTrue(cells.getUsedLiveCells().stream().anyMatch(
            i -> i.index == txInput1.previousOutput.index && Arrays.equals(i.txHash, txInput1.previousOutput.txHash)));
    CellOutput output1 = tx.outputs.get(0);
    Assertions.assertEquals(2, cells.getOffChainLiveCells().size());
    Assertions.assertTrue(cells.getOffChainLiveCells().stream().anyMatch(
            i -> Objects.equals(i.output.lock, output1.lock) &&
                    i.output.capacity == output1.capacity));

    tx = new Transaction();
    tx.inputs.add(getRandomTransactionInput(600).input);
    tx.outputs.add(getRandomOutput());
    tx.outputsData.add(new byte[0]);
    cells.applyOffChainTransaction(999, tx);
    // Because 999 > 500, so clear all usedLiveCells and offChainLiveCells at first.
    Assertions.assertEquals(1, cells.getUsedLiveCells().size());
    Assertions.assertEquals(1, cells.getOffChainLiveCells().size());


    tx = new Transaction();
    // Consume 1 cell from offChainLiveCells. offChainLiveCells - 1.
    //                                        usedLiveCells + 1
    tx.inputs.add(cells.getOffChainLiveCells().get(0).input);
    // Add 2 cells from offChainLiveCells. offChainLiveCells + 2.
    tx.outputs.add(getRandomOutput());
    tx.outputsData.add(new byte[0]);
    tx.outputs.add(new CellOutput(50000000000L, Address.decode("ckt1qrl2cyw7ulrxu48ysexpwus46r9md670h5h73cxjh3zmxsf4gt3d5qg2d5amjwfzgtqr2l72ulxw4k8c0dpga55qjzdlm749f9ffhpwl8zc422t2hvxmtlkk299l30k6xlgccjps9pe2sfhx5y3flvtlu56lu9u6pcqqqqqqqqvykxmu").getScript()));
    tx.outputsData.add(new byte[0]);
    cells.applyOffChainTransaction(1000, tx);
    Assertions.assertEquals(2, cells.getUsedLiveCells().size());
    Assertions.assertEquals(2, cells.getOffChainLiveCells().size());
  }

  @Test
  void consumeOffChainCells() {
    IteratorCells cells = new IteratorCells();
    cells.getOffChainLiveCells().add(getRandomTransactionInput(100));
    cells.getOffChainLiveCells().add(getRandomTransactionInput(200));
    Assertions.assertEquals(2, cells.getOffChainLiveCells().size());
    cells.consumeOffChainCells();
    Assertions.assertEquals(0, cells.getOffChainLiveCells().size());
  }

  private OutPoint getRandomOutPoint() {
    byte[] hash = new byte[32];
    new Random().nextBytes(hash);
    return new OutPoint(hash, new Random().nextInt(10));
  }

  private IteratorCells.TransactionInputWithBlockNumber getRandomTransactionInput(int blockNumber) {
    return new IteratorCells.TransactionInputWithBlockNumber(
            new CellInput(getRandomOutPoint()), getRandomOutput(),
            new byte[0], blockNumber);
  }

  private CellOutput getRandomOutput() {
    return new CellOutput(new Random().nextLong(), senderAddress.getScript());
  }

}
