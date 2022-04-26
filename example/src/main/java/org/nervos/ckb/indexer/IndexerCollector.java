package org.nervos.ckb.indexer;

import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.CellCollector;
import org.nervos.ckb.transaction.CollectResult;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexerCollector {

  private Api api;
  private CkbIndexerApi indexerApi;

  public IndexerCollector(Api api, CkbIndexerApi indexerApi) {
    this.api = api;
    this.indexerApi = indexerApi;
  }

  public CollectResult collectInputs(
      List<String> addresses, Transaction transaction, long feeRate, int initialLength)
      throws IOException {
    return new CellCollector(api)
        .collectInputs(
            addresses,
            transaction,
            feeRate,
            initialLength,
            new CellCkbIndexerIterator(indexerApi, addresses, true));
  }

  public CollectResult collectInputs(
      List<String> addresses,
      Transaction transaction,
      long feeRate,
      int initialLength,
      Script type)
      throws IOException {
    return new CellCollector(api)
        .collectInputs(
            addresses,
            transaction,
            feeRate,
            initialLength,
            new CellCkbIndexerIterator(indexerApi, addresses, type));
  }

  public long getCapacity(String address) throws IOException {
    AddressParseResult rs = AddressParser.parse(address);
    CkbIndexerCellsCapacity capacityInfo = indexerApi.getCellsCapacity(new SearchKey(rs.script));

    return capacityInfo.capacity;
  }

  public List<CellOutput> generateOutputs(List<Receiver> receivers, String changeAddress) {
    List<CellOutput> cellOutputs = new ArrayList<>();
    for (Receiver receiver : receivers) {
      AddressParseResult addressParseResult = AddressParser.parse(receiver.address);
      cellOutputs.add(new CellOutput(receiver.capacity, addressParseResult.script));
    }
    //  If change address is null or an empty string it means caller wants to transfer all balance,
    // so change output is not needed
    if (changeAddress != null && !changeAddress.trim().isEmpty()) {
      AddressParseResult addressParseResult = AddressParser.parse(changeAddress);
      cellOutputs.add(new CellOutput(0, addressParseResult.script));
    }

    return cellOutputs;
  }
}
