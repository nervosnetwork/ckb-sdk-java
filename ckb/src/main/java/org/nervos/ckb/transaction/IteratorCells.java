package org.nervos.ckb.transaction;

import org.nervos.ckb.type.*;
import org.nervos.indexer.model.Filter;
import org.nervos.indexer.model.SearchKey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IteratorCells {
  private static IteratorCells GLOBAL = new IteratorCells();
  // store used cells for avoiding double spending.
  private List<OutPointWithBlockNumber> usedLiveCells = new ArrayList<>();
  // store newly created outpoints for offchain live cells supply
  private List<TransactionInputWithBlockNumber> offChainLiveCells = new ArrayList<>();
  private long blockNumberOffset = 13;

  public static IteratorCells getGlobalInstance() {
    return GLOBAL;
  }

  public List<OutPointWithBlockNumber> getUsedLiveCells() {
    return usedLiveCells;
  }

  public void setUsedLiveCells(List<OutPointWithBlockNumber> usedLiveCells) {
    this.usedLiveCells = usedLiveCells;
  }

  public List<TransactionInputWithBlockNumber> getOffChainLiveCells() {
    return offChainLiveCells;
  }

  public void setOffChainLiveCells(List<TransactionInputWithBlockNumber> offChainLiveCells) {
    this.offChainLiveCells = offChainLiveCells;
  }

  public long getBlockNumberOffset() {
    return blockNumberOffset;
  }

  public void setBlockNumberOffset(long blockNumberOffset) {
    this.blockNumberOffset = blockNumberOffset;
  }

  public void applyOffChainTransaction(AbstractInputIterator iterator, Transaction transaction) throws IOException {
    byte[] transactionHash = transaction.computeHash();
    long latestBlockNumber = iterator.getTipBlockNumber();
    usedLiveCells = usedLiveCells.stream()
            .filter(o -> latestBlockNumber >= o.blockNumber && latestBlockNumber - o.blockNumber <= blockNumberOffset)
            .collect(Collectors.toList());

    offChainLiveCells = offChainLiveCells.stream()
            .filter(o -> latestBlockNumber >= o.blockNumber && latestBlockNumber - o.blockNumber <= blockNumberOffset)
            .collect(Collectors.toList());

    for (int i = 0; i < transaction.inputs.size(); i++) {
      OutPoint outPoint = transaction.inputs.get(i).previousOutput;
      // Add input to usedLiveCells
      usedLiveCells.add(new OutPointWithBlockNumber(outPoint, latestBlockNumber));
      // Remove input from offChainLiveCells
      Iterator<TransactionInputWithBlockNumber> it = offChainLiveCells.iterator();
      while (it.hasNext()) {
        TransactionInputWithBlockNumber input = it.next();
        if (Objects.equals(outPoint, input.input.previousOutput)) {
          it.remove();
        }
      }
    }
    for (int i = 0; i < transaction.outputs.size(); i++) {
      TransactionInputWithBlockNumber transactionInputWithBlockNumber = new TransactionInputWithBlockNumber(
              new CellInput(new OutPoint(transactionHash, i)),
              transaction.outputs.get(i),
              transaction.outputsData.get(i),
              latestBlockNumber);
      // Add output to offChainLiveCells if matched with searchKeys
      if (isTransactionInputForSearchKey(transactionInputWithBlockNumber, iterator.getSearchKeys())) {
        offChainLiveCells.add(transactionInputWithBlockNumber);
      }
    }
  }

  public static boolean isTransactionInputForSearchKey(TransactionInputWithBlockNumber transactionInputWithBlockNumber, List<SearchKey> searchKeys) {
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

  public List<TransactionInputWithBlockNumber> consumeOffChainCells() {
    List<TransactionInputWithBlockNumber> cellsToConsume = new ArrayList<>();
    Iterator<TransactionInputWithBlockNumber> it = offChainLiveCells.iterator();
    while (it.hasNext()) {
      TransactionInputWithBlockNumber input = it.next();
      cellsToConsume.add(input);
      it.remove();
    }
    return cellsToConsume;
  }

  public static class OutPointWithBlockNumber extends OutPoint {
    public long blockNumber;

    public OutPointWithBlockNumber(byte[] txHash, int index, long blockNumber) {
      super(txHash, index);
      this.blockNumber = blockNumber;
    }

    public OutPointWithBlockNumber(OutPoint outPoint, long blockNumber) {
      this.index = outPoint.index;
      this.txHash = outPoint.txHash;
      this.blockNumber = blockNumber;
    }
  }

  public static class TransactionInputWithBlockNumber extends TransactionInput {
    public long blockNumber;

    public TransactionInputWithBlockNumber(CellInput input, CellOutput output, byte[] outputData, long blockNumber) {
      super(input, output, outputData);
      this.blockNumber = blockNumber;
    }
  }
}
