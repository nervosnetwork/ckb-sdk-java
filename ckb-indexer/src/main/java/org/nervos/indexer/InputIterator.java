package org.nervos.indexer;

import org.nervos.ckb.Network;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.model.Filter;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.*;

public class InputIterator implements Iterator<TransactionInput> {
  private List<TransactionInput> transactionInputs = new ArrayList<>();
  private TransactionInput current;
  private int addressIndex;
  private int inputIndex;
  private byte[] afterCursor;

  private final List<String> addresses;
  private CkbIndexerApi indexerApi;
  private final Order order;
  private final Integer limit;
  private final Script type;

  InputIterator(
      CkbIndexerApi indexerApi,
      List<String> addresses,
      Order order,
      Integer limit,
      Script type) {
    this.indexerApi = indexerApi;
    this.addresses = addresses;
    this.order = order;
    this.limit = limit;
    this.type = type;

    addressIndex = 0;
    inputIndex = 0;
  }


  public InputIterator(String address) {
    this(getDefaultIndexerApi(Address.decode(address).getNetwork()), Arrays.asList(address));
  }
  
  public InputIterator(CkbIndexerApi api, String address) {
    this(api, Arrays.asList(address));
  }

  public InputIterator(CkbIndexerApi api, List<String> addresses) {
    this(api, addresses, null);
  }

  InputIterator(CkbIndexerApi api, List<String> addresses, Script type) {
    this(api, addresses, Order.ASC, 100, type);
  }
  
  private static CkbIndexerApi getDefaultIndexerApi(Network network) {
    String url = null;
    switch (network) {
      case MAINNET:
        url = "https://mainnet.ckb.dev/indexer";
        break;
      case TESTNET:
        url = "https://testnet.ckb.dev/indexer";
        break;
      default:
        throw new IllegalArgumentException("Unsupported network");
    }
    return new DefaultIndexerApi(url, false);
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

      SearchKey searchKey = new SearchKey();
      searchKey.scriptType = ScriptType.LOCK;
      searchKey.script = lockScript;
      if (type != null) {
        Filter filter = new Filter();
        filter.script = type;
        searchKey.filter = filter;
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
    CellsResponse response = indexerApi.getCells(searchKey, order, limit, afterCursor);
    List<TransactionInput> newTransactionInputs = new ArrayList<>();
    for (CellResponse liveCell : response.objects) {
      CellInput cellInput = new CellInput(liveCell.outPoint);
      newTransactionInputs.add(new TransactionInput(cellInput, liveCell.output));
    }
    transactionInputs = newTransactionInputs;
    afterCursor = response.lastCursor;
  }
}
