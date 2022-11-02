package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.model.Filter;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractInputIterator implements Iterator<TransactionInput> {
  protected List<TransactionInput> transactionInputs = new ArrayList<>();
  protected TransactionInput current;
  protected byte[] afterCursor;

  protected int inputIndex = 0;
  protected int searchKeysIndex = 0;
  protected List<SearchKey> searchKeys = new ArrayList<>();
  protected Order order = Order.ASC;
  protected Integer limit = 100;

  // store used cells for avoiding double spending.
  public static List<OutPointWithBlockNumber> usedLiveCells = new ArrayList<>();
  // store newly created outpoints for offchain live cells supply
  public static List<TransactionInputWithBlockNumber> offChainLiveCells = new ArrayList<>();
  public static long BLOCK_NUMBER_OFFSET = 13;

  public AbstractInputIterator addSearchKey(String address) {
    return addSearchKey(address, null);
  }

  public AbstractInputIterator addSearchKey(String address, Script type) {
    Script lockScript = Address.decode(address).getScript();

    SearchKey searchKey = new SearchKey();
    searchKey.scriptType = ScriptType.LOCK;
    searchKey.script = lockScript;
    if (type != null) {
      Filter filter = new Filter();
      filter.script = type;
      searchKey.filter = filter;
    }
    searchKeys.add(searchKey);
    return this;
  }

  public AbstractInputIterator addSudtSearchKey(String address, byte[] sudtArgs) {
    Address addr = Address.decode(address);
    Network network = addr.getNetwork();
    byte[] codeHash;
    if (network == Network.TESTNET) {
      codeHash = Script.SUDT_CODE_HASH_TESTNET;
    } else if (network == Network.MAINNET) {
      codeHash = Script.SUDT_CODE_HASH_MAINNET;
    } else {
      throw new IllegalArgumentException("Unsupported network");
    }
    Script type = new Script(
        codeHash,
        sudtArgs,
        Script.HashType.TYPE);
    return addSearchKey(address, type);
  }

  @Override
  public boolean hasNext() {
    updateCurrent();
    return current != null;
  }

  @Override
  public TransactionInput next() {
    updateCurrent();
    if (current != null) {
      inputIndex += 1;
      return current;
    } else {
      throw new NoSuchElementException();
    }
  }

  public void applyOffChainTransaction(Transaction transaction, byte[] transactionHash) throws IOException {
    long latestBlockNumber = getTipBlockNumber();
    usedLiveCells = usedLiveCells.stream()
            .filter(o -> latestBlockNumber >= o.blockNumber && latestBlockNumber - o.blockNumber <= BLOCK_NUMBER_OFFSET)
            .collect(Collectors.toList());

    offChainLiveCells = offChainLiveCells.stream()
            .filter(o -> latestBlockNumber >= o.blockNumber && latestBlockNumber - o.blockNumber <= BLOCK_NUMBER_OFFSET)
            .collect(Collectors.toList());

    for (int i = 0; i < transaction.inputs.size(); i++) {
      usedLiveCells.add(new OutPointWithBlockNumber(transaction.inputs.get(i).previousOutput, latestBlockNumber));
    }
    for (int i = 0; i < transaction.outputs.size(); i++) {
      TransactionInputWithBlockNumber transactionInputWithBlockNumber = new TransactionInputWithBlockNumber(
              new CellInput(new OutPoint(transactionHash, i)),
              transaction.outputs.get(i),
              transaction.outputsData.get(i),
              latestBlockNumber);
      if (isTransactionInputForSearchKey(transactionInputWithBlockNumber)) {
        offChainLiveCells.add(transactionInputWithBlockNumber);
      }
    }
  }

  protected boolean isTransactionInputForSearchKey(TransactionInputWithBlockNumber transactionInputWithBlockNumber) {
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
              if (!Objects.equals(cellOutput.type, searchKey.script)) {
                continue;
              }
              break;
            case TYPE:
              if (!Objects.equals(cellOutput.lock, searchKey.script)) {
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

  protected void updateCurrent() {
    if (inputIndex < transactionInputs.size()) {
      current = transactionInputs.get(inputIndex);
    } else {
      current = null;
    }

    while (current == null && searchKeysIndex < searchKeys.size()) {
      SearchKey searchKey = searchKeys.get(searchKeysIndex);
      try {
        fetchTransactionInputs(searchKey);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      if (transactionInputs.isEmpty()) {
        afterCursor = null;
        searchKeysIndex++;
      } else {
        inputIndex = 0;
        current = transactionInputs.get(0);
      }
    }
  }

  protected void fetchTransactionInputs(SearchKey searchKey) throws IOException {
    CellsResponse response = getLiveCells(searchKey, order, limit, afterCursor);
    List<TransactionInput> newTransactionInputs = new ArrayList<>();
    for (CellResponse liveCell: response.objects) {
      if (usedLiveCells.stream().anyMatch(
              o -> Arrays.equals(o.txHash, liveCell.outPoint.txHash)
                      && o.index == liveCell.outPoint.index)) {
        continue;
      }
      CellInput cellInput = new CellInput(liveCell.outPoint);
      newTransactionInputs.add(new TransactionInput(cellInput, liveCell.output, liveCell.outputData));
    }
    if (newTransactionInputs.size() == 0) {
      for (TransactionInputWithBlockNumber input: offChainLiveCells) {
        newTransactionInputs.add(input);
      }
      offChainLiveCells = new ArrayList<>();
    }
    transactionInputs = newTransactionInputs;
    afterCursor = response.lastCursor;
  }

  public abstract CellsResponse getLiveCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws
          IOException;

  public abstract long getTipBlockNumber() throws IOException;

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
