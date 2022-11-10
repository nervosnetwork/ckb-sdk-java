package org.nervos.ckb.transaction;

import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.indexer.model.Filter;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OffChainInputIterator extends AbstractInputIterator {
  private AbstractInputIterator iterator;
  private OffChainInputCollector offChainInputCollector;
  private boolean consumeOffChainCellsFirstly;
  private boolean isCurrentFromOffChain = false;

  public OffChainInputIterator(AbstractInputIterator iterator, OffChainInputCollector offChainInputCollector, boolean consumeOffChainCellsFirstly) {
    this.offChainInputCollector = offChainInputCollector;
    this.consumeOffChainCellsFirstly = consumeOffChainCellsFirstly;
    this.iterator = iterator;
    this.transactionInputs = iterator.transactionInputs;
    this.current = iterator.current;
    this.afterCursor = iterator.afterCursor;
    this.inputIndex = iterator.inputIndex;
    this.searchKeysIndex = iterator.searchKeysIndex;
    this.searchKeys = iterator.searchKeys;
    this.order = iterator.order;
    this.limit = iterator.limit;
  }

  public OffChainInputIterator(AbstractInputIterator iterator, OffChainInputCollector offChainInputCollector) {
    this(iterator, offChainInputCollector, false);
  }

  public OffChainInputIterator(AbstractInputIterator iterator) {
    this(iterator, OffChainInputCollector.getGlobalInstance());
  }

  public OffChainInputCollector getOffChainInputCollector() {
    return offChainInputCollector;
  }

  public void setOffChainInputCollector(OffChainInputCollector offChainInputCollector) {
    this.offChainInputCollector = offChainInputCollector;
  }

  public boolean isConsumeOffChainCellsFirstly() {
    return consumeOffChainCellsFirstly;
  }

  public void setConsumeOffChainCellsFirstly(boolean consumeOffChainCellsFirstly) {
    this.consumeOffChainCellsFirstly = consumeOffChainCellsFirstly;
  }

  @Override
  public TransactionInput next() {
    updateCurrent();
    if (current != null) {
      if (!isCurrentFromOffChain) {
        inputIndex += 1;
      }
      TransactionInput input = current;
      current = null;
      return input;
    } else {
      throw new NoSuchElementException();
    }
  }

  @Override
  protected void updateCurrent() {
    if (isCurrentFromOffChain && current != null) {
      return;
    }

    if (consumeOffChainCellsFirstly) {
      current = consumeNextOffChainCell();
      if (current != null) {
        isCurrentFromOffChain = true;
        return;
      }
    }

    // Update from RPC client
    isCurrentFromOffChain = false;
    super.updateCurrent();

    if (current == null && !consumeOffChainCellsFirstly) {
      current = consumeNextOffChainCell();
      if (current != null) {
        isCurrentFromOffChain = true;
      }
    }
  }

  private TransactionInput consumeNextOffChainCell() {
    Iterator<OffChainInputCollector.TransactionInputWithBlockNumber> it = offChainInputCollector.getOffChainLiveCells().iterator();
    while (it.hasNext()) {
      OffChainInputCollector.TransactionInputWithBlockNumber input = it.next();
      if (isTransactionInputForSearchKey(input, searchKeys)) {
        it.remove();
        return input;
      }
    }
    return null;
  }

  private static boolean isTransactionInputForSearchKey(OffChainInputCollector.TransactionInputWithBlockNumber transactionInputWithBlockNumber, List<SearchKey> searchKeys) {
    CellOutput cellOutput = transactionInputWithBlockNumber.output;
    byte[] cellOutputData = transactionInputWithBlockNumber.outputData;
    for (SearchKey searchKey: searchKeys) {
      switch (searchKey.scriptType) {
        case LOCK:
          if (!Objects.equals(cellOutput.lock, searchKey.script)) {
            continue;
          }
          break;
        case TYPE:
          if (!Objects.equals(cellOutput.type, searchKey.script)) {
            continue;
          }
          break;
      }
      Filter filter = searchKey.filter;
      if (filter != null) {
        if (filter.script != null) {
          switch (searchKey.scriptType) {
            case LOCK:
              if (!Objects.equals(cellOutput.type, filter.script)) {
                continue;
              }
              break;
            case TYPE:
              if (!Objects.equals(cellOutput.lock, filter.script)) {
                continue;
              }
              break;
          }
          if (filter.outputCapacityRange != null) {
            if (cellOutput.capacity < filter.outputCapacityRange.get(0) ||
                    cellOutput.capacity >= filter.outputCapacityRange.get(1)) {
              continue;
            }
          }
          if (filter.blockRange != null) {
            if (transactionInputWithBlockNumber.blockNumber < filter.blockRange.get(0) ||
                    transactionInputWithBlockNumber.blockNumber >= filter.blockRange.get(1)) {
              continue;
            }
          }
          if (filter.outputDataLenRange != null) {
            if (cellOutputData.length < filter.outputDataLenRange.get(0) ||
                    cellOutputData.length >= filter.outputDataLenRange.get(1)) {
              continue;
            }
          }
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public CellsResponse getLiveCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    return iterator.getLiveCells(searchKey, order, limit, afterCursor);
  }
}
