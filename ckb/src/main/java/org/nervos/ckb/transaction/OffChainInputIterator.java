package org.nervos.ckb.transaction;

import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.indexer.model.Filter;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OffChainInputIterator extends AbstractInputIterator {
  private AbstractInputIterator iterator;
  private OffChainInputCollector offChainInputCollector;
  private boolean consumeOffChainCellsFirstly = false;

  public OffChainInputIterator(AbstractInputIterator iterator, OffChainInputCollector offChainInputCollector, boolean consumeOffChainCellsFirstly) {
    this.iterator = iterator;
    this.offChainInputCollector = offChainInputCollector;
    this.consumeOffChainCellsFirstly = consumeOffChainCellsFirstly;
  }

  public OffChainInputIterator(AbstractInputIterator iterator, OffChainInputCollector offChainInputCollector) {
    this.iterator = iterator;
    this.offChainInputCollector = offChainInputCollector;
  }

  public OffChainInputIterator(AbstractInputIterator iterator) {
    this(iterator, OffChainInputCollector.getGlobalInstance());
  }

  public AbstractInputIterator getIterator() {
    return iterator;
  }

  public void setIterator(AbstractInputIterator iterator) {
    this.iterator = iterator;
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

  protected void fetchTransactionInputs(SearchKey searchKey) throws IOException {
    CellsResponse response = getLiveCells(searchKey, order, limit, afterCursor);
    List<TransactionInput> newTransactionInputs = new ArrayList<>();
    if (consumeOffChainCellsFirstly) {
      newTransactionInputs = consumeOffChainCells();
    }

    for (CellResponse liveCell: response.objects) {
      if (offChainInputCollector != null && offChainInputCollector.getUsedLiveCells().stream().anyMatch(
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
    for (OffChainInputCollector.TransactionInputWithBlockNumber input: offChainInputCollector.consumeOffChainCells()) {
      // Add output to offChainLiveCells if matched with searchKeys
      if (isTransactionInputForSearchKey(input, iterator.getSearchKeys())) {
        inputs.add(input);
      }
    }
    return inputs;
  }

  public static boolean isTransactionInputForSearchKey(OffChainInputCollector.TransactionInputWithBlockNumber transactionInputWithBlockNumber, List<SearchKey> searchKeys) {
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
