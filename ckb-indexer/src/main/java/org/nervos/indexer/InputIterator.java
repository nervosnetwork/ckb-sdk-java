package org.nervos.indexer;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class InputIterator implements Iterator<TransactionInput> {
  private List<TransactionInput> transactionInputs = new ArrayList<>();
  private TransactionInput current;
  private byte[] afterCursor;

  private CkbIndexerApi indexerApi;
  private int inputIndex = 0;
  private int searchKeysIndex = 0;
  private List<SearchKey> searchKeys = new ArrayList<>();
  private Order order = Order.ASC;
  private Integer limit = 100;

  public InputIterator(
      CkbIndexerApi indexerApi,
      Order order,
      Integer limit) {
    this.indexerApi = indexerApi;
    this.order = order;
    this.limit = limit;
  }

  public InputIterator(CkbIndexerApi api) {
    this.indexerApi = api;
  }

  public InputIterator(String address) {
    this(getDefaultIndexerApi(Address.decode(address).getNetwork()));
    addSearchKey(address);
  }

  public InputIterator addSearchKey(String address) {
    return addSearchKey(address, null);
  }

  public InputIterator addSearchKey(String address, Script type) {
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
 
  public InputIterator addSudtSearchKey(String address, byte[] sudtArgs) {
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

  private static CkbIndexerApi getDefaultIndexerApi(Network network) {
    String url;
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

  private void fetchTransactionInputs(SearchKey searchKey) throws IOException {
    CellsResponse response = indexerApi.getCells(searchKey, order, limit, afterCursor);
    List<TransactionInput> newTransactionInputs = new ArrayList<>();
    for (CellResponse liveCell : response.objects) {
      CellInput cellInput = new CellInput(liveCell.outPoint);
      newTransactionInputs.add(new TransactionInput(cellInput, liveCell.output, liveCell.outputData));
    }
    transactionInputs = newTransactionInputs;
    afterCursor = response.lastCursor;
  }
}
