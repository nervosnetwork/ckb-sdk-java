package org.nervos.ckb.transaction;

import org.nervos.ckb.type.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OffChainInputCollector {
  private static OffChainInputCollector GLOBAL = new OffChainInputCollector();
  // store used cells for avoiding double spending.
  private List<OutPointWithBlockNumber> usedLiveCells = new ArrayList<>();
  // store newly created outpoints for offchain live cells supply
  private List<TransactionInputWithBlockNumber> offChainLiveCells = new ArrayList<>();
  private long blockNumberOffset = 13;

  public static OffChainInputCollector getGlobalInstance() {
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

  public void applyOffChainTransaction(long latestBlockNumber, Transaction transaction) throws IOException {
    byte[] transactionHash = transaction.computeHash();
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
      offChainLiveCells.add(transactionInputWithBlockNumber);
    }
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
