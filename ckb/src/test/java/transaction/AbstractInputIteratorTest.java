package transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.Network;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.AbstractInputIterator;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.ArrayList;

class AbstractInputIteratorTest {
  private String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq03ewkvsva4cchhntydu648l7lyvn9w2cctnpask";
  private Address senderAddress = Address.decode(sender);

  @Test
  void TestNext() throws IOException {

    SimpleInputIterator iterator = new SimpleInputIterator(1);
    iterator.addSearchKey(sender);
    AbstractInputIterator.offChainLiveCells.add(
            new AbstractInputIterator.TransactionInputWithBlockNumber(
                    new CellInput(new OutPoint("0xa7b450ea21a8491c783f2fbe99ba2437d32184ee510f02fd596597a64e82ae36", 2)),
                    new CellOutput(50000000000L, senderAddress.getScript()),
                    new byte[0],
                    500)
    );
    Assertions.assertEquals(1, AbstractInputIterator.offChainLiveCells.size());
    Assertions.assertEquals(0, AbstractInputIterator.usedLiveCells.size());

    // create a transaction. Expected:
    //   - input:
    //     - 1 from iterator, 1 from offchainLiveCells.
    //     - Add them all to usedLiveCells.
    //   - output: 2 outputs are all owned by `sender`, so they will be added into offChainLiveCells.
    TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(Network.TESTNET);
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(configuration, iterator)
            .addOutput(sender, 100000000000L)
            .setChangeOutput(sender)
            .build();

    // list size change:
    //   - offChainLiveCells: 1 - 1 + 2 = 2
    //   - usedLiveCells: 0 + 2 = 2
    iterator.applyOffChainTransaction(txWithGroups.getTxView(), Numeric.hexStringToByteArray("0xca2ca7be928300e998f1816392590c6b09c8f2b5fa5f60c5367fb85c84171d96"));
    Assertions.assertEquals(2, AbstractInputIterator.offChainLiveCells.size());
    Assertions.assertEquals(2, AbstractInputIterator.usedLiveCells.size());

    // create a transaction. Expected:
    //   - input:
    //     - 2 from offchainLiveCells.
    //     - Add them all to usedLiveCells.
    //   - output: 1 outputs is owned by `sender`, so it will be added into offChainLiveCells.
    txWithGroups = new CkbTransactionBuilder(configuration, iterator)
            .addOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r", 100000000000L)
            .setChangeOutput(sender)
            .build();
    iterator.applyOffChainTransaction(txWithGroups.getTxView(), new byte[0]);
    // list size change:
    //   - offChainLiveCells: 2 - 2 + 1 = 1
    //   - usedLiveCells: 2 + 2 = 4
    Assertions.assertEquals(1, AbstractInputIterator.offChainLiveCells.size());
    Assertions.assertEquals(4, AbstractInputIterator.usedLiveCells.size());
  }

  public class SimpleInputIterator extends AbstractInputIterator {
    private int count;

    public SimpleInputIterator(int count) {
      this.count = count;
    }

    @Override
    public CellsResponse getLiveCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
      CellsResponse cellsResponse = new CellsResponse();
      cellsResponse.lastCursor = new byte[0];
      cellsResponse.objects = new ArrayList<>();
      if (count == 0) {
        return cellsResponse;
      }
      count -= 1;

      CellResponse cellResponse = new CellResponse();
      cellResponse.blockNumber = 400;
      cellResponse.outPoint = new OutPoint("0xb5bed9b72345c4353c82a9953c46b56f08bacbaefed942673beaceafb9651dc4", 1);
      cellResponse.output = new CellOutput(90000000000L, senderAddress.getScript());
      cellResponse.outputData = new byte[0];

      cellsResponse.objects.add(cellResponse);

      return cellsResponse;
    }

    @Override
    public long getTipBlockNumber() throws IOException {
      return 0;
    }
  }
}
