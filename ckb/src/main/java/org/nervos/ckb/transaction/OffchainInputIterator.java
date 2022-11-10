package org.nervos.ckb.transaction;

import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OffchainInputIterator extends AbstractInputIterator {
  private AbstractInputIterator iterator;
  private IteratorCells iteratorCells;
  private boolean consumeOffChainCellsFirstly = false;

  public OffchainInputIterator(AbstractInputIterator iterator, IteratorCells iteratorCells) {
    this.iterator = iterator;
    this.iteratorCells = iteratorCells;
  }

  public AbstractInputIterator getIterator() {
    return iterator;
  }

  public IteratorCells getIteratorCells() {
    return iteratorCells;
  }

  public boolean isConsumeOffChainCellsFirstly() {
    return consumeOffChainCellsFirstly;
  }

  protected void fetchTransactionInputs(SearchKey searchKey) throws IOException {
    CellsResponse response = getLiveCells(searchKey, order, limit, afterCursor);
    List<TransactionInput> newTransactionInputs = new ArrayList<>();
    if (consumeOffChainCellsFirstly) {
      newTransactionInputs = consumeOffChainCells();
    }

    for (CellResponse liveCell: response.objects) {
      if (iteratorCells != null && iteratorCells.getUsedLiveCells().stream().anyMatch(
              o -> Arrays.equals(o.txHash, liveCell.outPoint.txHash)
                      && o.index == liveCell.outPoint.index)) {
        continue;
      }
      CellInput cellInput = new CellInput(liveCell.outPoint);
      newTransactionInputs.add(new TransactionInput(cellInput, liveCell.output, liveCell.outputData));
    }
    if (newTransactionInputs.size() == 0 && !consumeOffChainCellsFirstly) {
      newTransactionInputs = consumeOffChainCells();
    }
    transactionInputs = newTransactionInputs;
    afterCursor = response.lastCursor;
  }

  private List<TransactionInput> consumeOffChainCells() {
    List<TransactionInput> inputs = new ArrayList<>();
    for (IteratorCells.TransactionInputWithBlockNumber input: iteratorCells.consumeOffChainCells()) {
      inputs.add(input);
    }
    return inputs;
  }

  @Override
  public CellsResponse getLiveCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    return iterator.getLiveCells(searchKey, order, limit, afterCursor);
  }

  @Override
  public long getTipBlockNumber() throws IOException {
    return iterator.getTipBlockNumber();
  }
}
