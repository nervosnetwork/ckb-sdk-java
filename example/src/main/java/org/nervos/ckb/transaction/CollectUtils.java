package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.nervos.ckb.indexer.CkbIndexerCellsCapacityResponse;
import org.nervos.ckb.indexer.SearchKey;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.CkbIndexerApi;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectUtils {

  private Api api;
  private CkbIndexerApi ckbIndexerApi;

  public CollectUtils(Api api) {
    this.api = api;
  }

  public CollectUtils(CkbIndexerApi api) {
    this.ckbIndexerApi = api;
  }

  public CollectResult collectInputs(
      List<String> addresses,
      Transaction transaction,
      BigInteger feeRate,
      int initialLength,
      boolean skipDataAndType,
      long fromBlockNumber)
      throws IOException {
    return new CellCollector(api)
        .collectInputs(
            addresses,
            transaction,
            feeRate,
            initialLength,
            new CellBlockIterator(api, addresses, skipDataAndType, fromBlockNumber));
  }

  public CollectResult collectInputs(
      List<String> addresses, Transaction transaction, BigInteger feeRate, int initialLength)
      throws IOException {
    return collectInputs(addresses, transaction, feeRate, initialLength, true, 0);
  }

  public CollectResult collectInputsWithIndexer(
      List<String> addresses,
      Transaction transaction,
      BigInteger feeRate,
      int initialLength,
      boolean skipDataAndType)
      throws IOException {
    return new CellCollector(api)
        .collectInputs(
            addresses,
            transaction,
            feeRate,
            initialLength,
            new CellIndexerIterator(api, addresses, skipDataAndType));
  }

  public CollectResult collectInputsWithCkbIndexer(
      List<String> addresses,
      Transaction transaction,
      BigInteger feeRate,
      int initialLength,
      boolean skipDataAndType)
      throws IOException {
    return new CellCollector(api)
        .collectInputs(
            addresses,
            transaction,
            feeRate,
            initialLength,
            new CellCkbIndexerIterator(ckbIndexerApi, addresses, skipDataAndType));
  }

  public CollectResult collectInputsWithIndexer(
      List<String> addresses, Transaction transaction, BigInteger feeRate, int initialLength)
      throws IOException {
    return collectInputsWithIndexer(addresses, transaction, feeRate, initialLength, true);
  }

  public BigInteger getCapacityWithAddressByCkbIndexer(String address) throws IOException {
    AddressParseResult rs = AddressParser.parse(address);
    CkbIndexerCellsCapacityResponse capacityInfo =
        ckbIndexerApi.getCellsCapacity(new SearchKey(rs.script));

    return Numeric.toBigInt(capacityInfo.capacity);
  }

  public BigInteger getCapacityWithAddress(String address, boolean withIndexer) {
    BigInteger capacity = BigInteger.ZERO;
    Iterator<TransactionInput> cellIterator =
        withIndexer
            ? new CellIndexerIterator(api, Collections.singletonList(address))
            : new CellBlockIterator(api, Collections.singletonList(address), true, null);
    while (cellIterator.hasNext()) {
      TransactionInput transactionInput = cellIterator.next();
      if (transactionInput == null) break;
      capacity = capacity.add(transactionInput.capacity);
    }
    return capacity;
  }

  public BigInteger getCapacityWithAddress(String address) {
    return getCapacityWithAddress(address, false);
  }

  public List<CellOutput> generateOutputs(List<Receiver> receivers, String changeAddress) {
    List<CellOutput> cellOutputs = new ArrayList<>();
    for (Receiver receiver : receivers) {
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
