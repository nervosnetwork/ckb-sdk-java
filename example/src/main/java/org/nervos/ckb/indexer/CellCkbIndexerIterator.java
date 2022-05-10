package org.nervos.ckb.indexer;

import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.address.Address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CellCkbIndexerIterator implements Iterator<TransactionInput> {
  private static final int PAGE_SIZE = 50;

  private List<TransactionInput> transactionInputs = new ArrayList<>();
  private int addressIndex;
  private int inputIndex;
  private byte[] afterCursor;

  private final List<String> addresses;
  private final CkbIndexerApi indexerApi;
  private final boolean skipDataAndType;
  private final Order order;
  private final Integer limit;
  private final Script type;

  CellCkbIndexerIterator(
      CkbIndexerApi indexerApi,
      List<String> addresses,
      boolean skipDataAndType,
      Order order,
      Integer limit,
      byte[] afterCursor,
      Script type) {
    this.indexerApi = indexerApi;
    this.addresses = addresses;
    this.skipDataAndType = skipDataAndType;
    this.afterCursor = afterCursor;
    this.order = order;
    this.limit = limit;
    this.type = type;

    addressIndex = 0;
    inputIndex = 0;
  }

  CellCkbIndexerIterator(CkbIndexerApi api, List<String> addresses, boolean skipDataAndType) {
    this(api, addresses, skipDataAndType, Order.ASC, 100, null, null);
  }

  CellCkbIndexerIterator(CkbIndexerApi api, List<String> addresses, Script type) {
    this(api, addresses, false, Order.ASC, 100, null, type);
  }

  @Override
  public boolean hasNext() {
    return transactionInputs.size() > 0 || addressIndex < addresses.size();
  }

  @Override
  public TransactionInput next() {
    if (transactionInputs.size() <= 0 || inputIndex >= transactionInputs.size()) {
      transactionInputs.clear();
      inputIndex = 0;
      do {
        if (type != null) {
          String address = addresses.get(addressIndex);
          Script lock = Address.decode(address).getScript();
          byte[] lockHash = lock.computeHash();
          transactionInputs =
              fetchTransactionInputsByType(
                  lockHash,
                  new SearchKey(lock, ScriptType.LOCK, new SearchKey.Filter(type)));
        } else {
          transactionInputs =
              fetchTransactionInputsByLock(
                  new SearchKey(Address.decode(addresses.get(addressIndex)).getScript()));
        }
        if (transactionInputs == null || transactionInputs.size() == 0) {
          afterCursor = null;
          addressIndex++;
        }
        if (addressIndex >= addresses.size()) {
          return null;
        }
      } while (transactionInputs == null || transactionInputs.size() == 0);
    }
    return transactionInputs.get(inputIndex++);
  }

  private List<TransactionInput> fetchTransactionInputsByLock(SearchKey searchKey) {
    List<CkbIndexerCells.Cell> liveCells = new ArrayList<>();
    try {
      CkbIndexerCells response = indexerApi.getCells(searchKey, order, limit, afterCursor);
      liveCells = response.objects;
      afterCursor = response.lastCursor;
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (CkbIndexerCells.Cell liveCell : liveCells) {
      if (skipDataAndType) {
        CellOutput cellOutput = liveCell.output;
        if ((liveCell.outputData != null && liveCell.outputData.length > 0)
            || cellOutput.type != null) {
          continue;
        }
      } else if (liveCell.output.type == null
          || !liveCell.output.type.computeHash().equals(type.computeHash())) {
        continue;
      }
      CellInput cellInput = new CellInput(liveCell.outPoint);
      long capacity = liveCell.output.capacity;
      transactionInputs.add(
          new TransactionInput(cellInput, capacity, searchKey.script.computeHash()));
    }
    if (liveCells.size() == 0) {
      transactionInputs.clear();
    }
    return transactionInputs;
  }

  private List<TransactionInput> fetchTransactionInputsByType(
      byte[] lockHash, SearchKey searchKey) {
    List<CkbIndexerCells.Cell> liveCells = new ArrayList<>();
    try {
      CkbIndexerCells response = indexerApi.getCells(searchKey, order, limit, afterCursor);
      liveCells = response.objects;
      afterCursor = response.lastCursor;
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (CkbIndexerCells.Cell liveCell : liveCells) {
      if (skipDataAndType) {
        CellOutput cellOutput = liveCell.output;
        if ((liveCell.outputData != null && liveCell.outputData.length > 0)
            || cellOutput.type != null) {
          continue;
        }
      } else if (liveCell.output.type == null
          || !liveCell.output.type.computeHash().equals(type.computeHash())) {
        continue;
      }
      CellInput cellInput = new CellInput(liveCell.outPoint);
      long capacity = liveCell.output.capacity;
      transactionInputs.add(new TransactionInput(cellInput, capacity, lockHash));
    }
    if (liveCells.size() == 0) {
      transactionInputs.clear();
    }
    return transactionInputs;
  }
}
