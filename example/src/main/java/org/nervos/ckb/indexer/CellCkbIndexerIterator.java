package org.nervos.ckb.indexer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Strings;
import org.nervos.ckb.utils.address.AddressParser;

public class CellCkbIndexerIterator implements Iterator<TransactionInput> {
  private static final int PAGE_SIZE = 50;

  private List<TransactionInput> transactionInputs = new ArrayList<>();
  private int addressIndex;
  private int inputIndex;
  private String afterCursor;

  private final List<String> addresses;
  private final CkbIndexerApi indexerApi;
  private final boolean skipDataAndType;
  private final String order;
  private final BigInteger limit;
  private final Script type;

  CellCkbIndexerIterator(
      CkbIndexerApi indexerApi,
      List<String> addresses,
      boolean skipDataAndType,
      String order,
      BigInteger limit,
      String afterCursor,
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
    this(api, addresses, skipDataAndType, "asc", BigInteger.valueOf(100), "0x", null);
  }

  CellCkbIndexerIterator(CkbIndexerApi api, List<String> addresses, Script type) {
    this(api, addresses, false, "asc", BigInteger.valueOf(100), "0x", type);
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
          Script lock = AddressParser.parse(address).script;
          String lockHash = lock.computeHash();
          transactionInputs =
              fetchTransactionInputsByType(
                  lockHash, new SearchKey(lock, "lock", new SearchKey.Filter(type)));
        } else {
          transactionInputs =
              fetchTransactionInputsByLock(
                  new SearchKey(AddressParser.parse(addresses.get(addressIndex)).script));
        }
        if (transactionInputs == null || transactionInputs.size() == 0) {
          afterCursor = "0x";
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
        if ((!Strings.isEmpty(liveCell.outputData) && !"0x".equals(liveCell.outputData))
            || cellOutput.type != null) {
          continue;
        }
      } else if (liveCell.output.type == null
          || !liveCell.output.type.computeHash().equals(type.computeHash())) {
        continue;
      }
      CellInput cellInput = new CellInput(liveCell.outPoint, "0x0");
      BigInteger capacity = Numeric.toBigInt(liveCell.output.capacity);
      transactionInputs.add(
          new TransactionInput(cellInput, capacity, searchKey.script.computeHash()));
    }
    if (liveCells.size() == 0) {
      transactionInputs.clear();
    }
    return transactionInputs;
  }

  private List<TransactionInput> fetchTransactionInputsByType(
      String lockHash, SearchKey searchKey) {
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
        if ((!Strings.isEmpty(liveCell.outputData) && !"0x".equals(liveCell.outputData))
            || cellOutput.type != null) {
          continue;
        }
      } else if (liveCell.output.type == null
          || !liveCell.output.type.computeHash().equals(type.computeHash())) {
        continue;
      }
      CellInput cellInput = new CellInput(liveCell.outPoint, "0x0");
      BigInteger capacity = Numeric.toBigInt(liveCell.output.capacity);
      transactionInputs.add(new TransactionInput(cellInput, capacity, lockHash));
    }
    if (liveCells.size() == 0) {
      transactionInputs.clear();
    }
    return transactionInputs;
  }
}
