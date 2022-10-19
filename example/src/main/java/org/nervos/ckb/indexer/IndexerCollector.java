package org.nervos.ckb.indexer;

import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.CellCollector;
import org.nervos.ckb.transaction.CollectResult;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class IndexerCollector {
  public static int maxCount = 3;
  public static int index = 0;
  public static List<List<OutPoint>> lockedCellsList = new ArrayList<>();

  static {
    for (int i = 0; i < maxCount; i++) {
      lockedCellsList.add(new ArrayList<>());
    }
  }

  private Api api;
  private CkbIndexerApi indexerApi;

  public IndexerCollector(Api api, CkbIndexerApi indexerApi) {
    this.api = api;
    this.indexerApi = indexerApi;
  }

  public CollectResult collectInputs(
      List<String> addresses, Transaction transaction, BigInteger feeRate, int initialLength)
      throws IOException {
    CollectResult collectResult = new CellCollector(api)
        .collectInputs(
            addresses,
            transaction,
            feeRate,
            initialLength,
            new CellCkbIndexerIterator(indexerApi, addresses, true), getLockedCells());
    updateLockedCellsList(transaction);
    return collectResult;
  }

  public CollectResult collectInputs(
      List<String> addresses,
      Transaction transaction,
      BigInteger feeRate,
      int initialLength,
      Script type)
      throws IOException {
    CollectResult collectResult = new CellCollector(api)
        .collectInputs(
            addresses,
            transaction,
            feeRate,
            initialLength,
            new CellCkbIndexerIterator(indexerApi, addresses, type), getLockedCells());
    updateLockedCellsList(transaction);
    return collectResult;
  }

  public void updateLockedCellsList(Transaction transaction) {
    lockedCellsList.set(index, transaction.inputs.stream().map(i -> i.previousOutput).collect(Collectors.toList()));
    index = (index + 1) % maxCount;
  }

  public List<OutPoint> getLockedCells() {
    return lockedCellsList.stream().flatMap(i -> i.stream()).collect(Collectors.toList());
  }

  public void clearLastLockedCells() {
    index = (index - 1) % maxCount;
    lockedCellsList.set(index, new ArrayList<>());
  }

  public BigInteger getCapacity(String address) throws IOException {
    AddressParseResult rs = AddressParser.parse(address);
    CkbIndexerCellsCapacity capacityInfo = indexerApi.getCellsCapacity(new SearchKey(rs.script));

    return Numeric.toBigInt(capacityInfo.capacity);
  }

  public List<CellOutput> generateOutputs(List<Receiver> receivers, String changeAddress) {
    List<CellOutput> cellOutputs = new ArrayList<>();
    for (Receiver receiver: receivers) {
      AddressParseResult addressParseResult = AddressParser.parse(receiver.address);
      cellOutputs.add(
          new CellOutput(
              Numeric.toHexStringWithPrefix(receiver.capacity), addressParseResult.script));
    }
    //  If change address is null or an empty string it means caller wants to transfer all balance,
    // so change output is not needed
    if (changeAddress != null && !changeAddress.trim().isEmpty()) {
      AddressParseResult addressParseResult = AddressParser.parse(changeAddress);
      cellOutputs.add(new CellOutput("0x0", addressParseResult.script));
    }

    return cellOutputs;
  }
}
