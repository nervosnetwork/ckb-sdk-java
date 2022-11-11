package org.nervos.ckb.transaction;

import org.nervos.ckb.type.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OffChainInputCollector {
  private static OffChainInputCollector INSTANCE;
  // store used cells for avoiding double spending.
  private List<OutPointWithBlockNumber> usedLiveCells = new ArrayList<>();
  // store newly created outpoints for offchain live cells supply
  private List<TransactionInputWithBlockNumber> offChainLiveCells = new ArrayList<>();
  private long blockNumberOffset = 13;

  public static OffChainInputCollector getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new OffChainInputCollector();
    }
    return INSTANCE;
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

  public void applyOffChainTransaction(long tipBlockNumber, Transaction transaction) throws IOException {
    byte[] transactionHash = transaction.computeHash();
    usedLiveCells = usedLiveCells.stream()
            .filter(o -> tipBlockNumber >= o.blockNumber && tipBlockNumber - o.blockNumber <= blockNumberOffset)
            .collect(Collectors.toList());

    offChainLiveCells = offChainLiveCells.stream()
            .filter(o -> tipBlockNumber >= o.blockNumber && tipBlockNumber - o.blockNumber <= blockNumberOffset)
            .collect(Collectors.toList());

    for (int i = 0; i < transaction.inputs.size(); i++) {
      OutPoint consumedOutPoint = transaction.inputs.get(i).previousOutput;
      // Add input to usedLiveCells
      usedLiveCells.add(new OutPointWithBlockNumber(consumedOutPoint, tipBlockNumber));
      // Remove input from offChainLiveCells
      Iterator<TransactionInputWithBlockNumber> it = offChainLiveCells.iterator();
      while (it.hasNext()) {
        TransactionInputWithBlockNumber offChainLiveCell = it.next();
        if (Objects.equals(consumedOutPoint, offChainLiveCell.input.previousOutput)) {
          it.remove();
        }
      }
    }
    for (int i = 0; i < transaction.outputs.size(); i++) {
      TransactionInputWithBlockNumber transactionInputWithBlockNumber = new TransactionInputWithBlockNumber(
              new CellInput(new OutPoint(transactionHash, i)),
              transaction.outputs.get(i),
              transaction.outputsData.get(i),
              tipBlockNumber);
      offChainLiveCells.add(transactionInputWithBlockNumber);
    }
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
