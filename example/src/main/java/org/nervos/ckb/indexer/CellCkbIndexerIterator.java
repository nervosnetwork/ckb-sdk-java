package org.nervos.ckb.indexer;

import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.address.Address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CellCkbIndexerIterator implements Iterator<TransactionInput> {
  private List<TransactionInput> transactionInputs = new ArrayList<>();
  private TransactionInput current;
  private int addressIndex;
  private int inputIndex;
  private byte[] afterCursor;

  private final List<String> addresses;
  private final CkbIndexerApi indexerApi;
  private final Order order;
  private final Integer limit;
  private final Script type;

  CellCkbIndexerIterator(
      CkbIndexerApi indexerApi,
      List<String> addresses,
      Order order,
      Integer limit,
      byte[] afterCursor,
      Script type) {
    this.indexerApi = indexerApi;
    this.addresses = addresses;
    this.afterCursor = afterCursor;
    this.order = order;
    this.limit = limit;
    this.type = type;

    addressIndex = 0;
    inputIndex = 0;
  }

  public CellCkbIndexerIterator(CkbIndexerApi api, List<String> addresses) {
    this(api, addresses, Order.ASC, 100, null, null);
  }

  CellCkbIndexerIterator(CkbIndexerApi api, List<String> addresses, Script type) {
    this(api, addresses, Order.ASC, 100, null, type);
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

  private void updateCurrent() {
    if (inputIndex < transactionInputs.size()) {
      current = transactionInputs.get(inputIndex);
    } else {
      current = null;
    }

    while (current == null && addressIndex < addresses.size()) {
      String address = addresses.get(addressIndex);
      Script lockScript = Address.decode(address).getScript();
      SearchKey searchKey = new SearchKey(lockScript, ScriptType.LOCK);
      if (type != null) {
        searchKey.filter = new SearchKey.Filter(type);
      }
      try {
        fetchTransactionInputs(searchKey);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      if (transactionInputs.isEmpty()) {
        afterCursor = null;
        addressIndex++;
      } else {
        inputIndex = 0;
        current = transactionInputs.get(0);
      }
    }
  }

  private void fetchTransactionInputs(SearchKey searchKey) throws IOException {
    CkbIndexerCells response = indexerApi.getCells(searchKey, order, limit, afterCursor);
    List<TransactionInput> newTransactionInputs = new ArrayList<>();
    for (CkbIndexerCells.Cell liveCell : response.objects) {
      CellInput cellInput = new CellInput(liveCell.outPoint);
      newTransactionInputs.add(new TransactionInput(cellInput, liveCell.output));
    }
    transactionInputs = newTransactionInputs;
    afterCursor = response.lastCursor;
  }
}
