package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.model.Filter;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.*;

public abstract class AbstractInputIterator implements Iterator<TransactionInput> {
  protected List<TransactionInput> transactionInputs = new ArrayList<>();
  protected TransactionInput current;
  protected byte[] afterCursor;

  protected int inputIndex = 0;
  protected int searchKeysIndex = 0;
  protected List<SearchKey> searchKeys = new ArrayList<>();
  protected Order order = Order.ASC;
  protected Integer limit = 100;

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
      if (IteratorCells.usedLiveCells.stream().anyMatch(
              o -> Arrays.equals(o.txHash, liveCell.outPoint.txHash)
                      && o.index == liveCell.outPoint.index)) {
        continue;
      }
      CellInput cellInput = new CellInput(liveCell.outPoint);
      newTransactionInputs.add(new TransactionInput(cellInput, liveCell.output, liveCell.outputData));
    }
    if (newTransactionInputs.size() == 0) {
      for (IteratorCells.TransactionInputWithBlockNumber input: IteratorCells.consumeOffChainCells()) {
        newTransactionInputs.add(input);
      }
    }
    transactionInputs = newTransactionInputs;
    afterCursor = response.lastCursor;
  }

  public abstract CellsResponse getLiveCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws
          IOException;

  public abstract long getTipBlockNumber() throws IOException;
}
