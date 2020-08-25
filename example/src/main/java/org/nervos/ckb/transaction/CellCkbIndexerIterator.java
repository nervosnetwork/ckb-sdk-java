package org.nervos.ckb.transaction;

import org.nervos.ckb.indexer.SearchKey;
import org.nervos.ckb.service.CkbIndexerApi;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.indexer.CkbIndexerCell;
import org.nervos.ckb.indexer.CkbIndexerCellResponse;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Strings;
import org.nervos.ckb.utils.address.AddressParser;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CellCkbIndexerIterator implements Iterator<TransactionInput> {
  private static final int PAGE_SIZE = 50;

  private List<TransactionInput> transactionInputs = new ArrayList<>();
  private int addressIndex;
  private int inputIndex;
  private String afterCursor;

  private final List<String> addresses;
  private final CkbIndexerApi api;
  private final boolean skipDataAndType;
  private final String order;
  private final BigInteger limit;

  CellCkbIndexerIterator(CkbIndexerApi api, List<String> addresses, boolean skipDataAndType, String order, BigInteger limit, String afterCursor) {
    this.api = api;
    this.addresses = addresses;
    this.skipDataAndType = skipDataAndType;
    this.afterCursor = afterCursor;
    this.order = order;
    this.limit = limit;

    addressIndex = 0;
    inputIndex = 0;
  }

  CellCkbIndexerIterator(CkbIndexerApi api, List<String> addresses) {
    this(api, addresses, true, "asc", BigInteger.valueOf(100), "0x");
  }

  CellCkbIndexerIterator(CkbIndexerApi api, List<String> addresses, boolean skipDataAndType) {
    this(api, addresses, skipDataAndType, "asc", BigInteger.valueOf(100), "0x");
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
        Script script = AddressParser.parse(addresses.get(addressIndex)).script;
        transactionInputs = fetchTransactionInputsByLock(new SearchKey(script));
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
    List<CkbIndexerCell> liveCells = new ArrayList<>();
    try {
      CkbIndexerCellResponse response = api.getCells(searchKey, order, limit, afterCursor);
      liveCells = response.objects;
      afterCursor = response.lastCursor;
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (CkbIndexerCell liveCell : liveCells) {
      if (skipDataAndType) {
        CellOutput cellOutput = liveCell.output;
        if ((!Strings.isEmpty(liveCell.outputData) && !"0x".equals(liveCell.outputData))
            || cellOutput.type != null) {
          continue;
        }
      }
      CellInput cellInput =
          new CellInput(liveCell.outPoint, "0x0");
      BigInteger capacity = Numeric.toBigInt(liveCell.output.capacity);
      transactionInputs.add(new TransactionInput(cellInput, capacity, searchKey.script.computeHash()));
    }
    if (liveCells.size() == 0) {
      transactionInputs.clear();
    }
    return transactionInputs;
  }
}
