package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectUtils {

  private Api api;
  private boolean skipDataAndType = true;

  public CollectUtils(Api api) {
    this.api = api;
  }

  public CollectUtils(Api api, boolean skipDataAndType) {
    this.api = api;
    this.skipDataAndType = skipDataAndType;
  }

  public List<CellsWithAddress> collectInputs(
      List<String> sendAddresses,
      List<CellOutput> cellOutputs,
      BigInteger feeRate,
      int initialLength)
      throws IOException {
    return collectInputs(sendAddresses, cellOutputs, feeRate, initialLength, null, null);
  }

  public List<CellsWithAddress> collectInputs(
      List<String> sendAddresses,
      List<CellOutput> cellOutputs,
      BigInteger feeRate,
      int initialLength,
      List<CellDep> cellDeps,
      List<String> cellOutputsData)
      throws IOException {
    List<CellsWithAddress> cellsWithAddresses = new ArrayList<>();
    List<String> lockHashes = new ArrayList<>();
    for (String address : sendAddresses) {
      AddressParseResult addressParseResult = AddressParser.parse(address);
      lockHashes.add(addressParseResult.script.computeHash());
    }
    Map<String, List<CellInput>> lockInputMap =
        new CellCollector(api, skipDataAndType)
            .collectInputs(
                lockHashes, cellOutputs, feeRate, initialLength, cellDeps, cellOutputsData);

    for (Map.Entry<String, List<CellInput>> entry : lockInputMap.entrySet()) {
      cellsWithAddresses.add(
          new CellsWithAddress(
              entry.getValue(), sendAddresses.get(lockHashes.indexOf(entry.getKey()))));
    }
    return cellsWithAddresses;
  }

  public List<CellsWithAddress> collectInputsWithIndexer(
      List<String> sendAddresses,
      List<CellOutput> cellOutputs,
      BigInteger feeRate,
      int initialLength)
      throws IOException {
    return collectInputsWithIndexer(sendAddresses, cellOutputs, feeRate, initialLength, null, null);
  }

  public List<CellsWithAddress> collectInputsWithIndexer(
      List<String> sendAddresses,
      List<CellOutput> cellOutputs,
      BigInteger feeRate,
      int initialLength,
      List<CellDep> cellDeps,
      List<String> cellOutputsData)
      throws IOException {
    List<CellsWithAddress> cellsWithAddresses = new ArrayList<>();
    List<String> lockHashes = new ArrayList<>();
    for (String address : sendAddresses) {
      AddressParseResult addressParseResult = AddressParser.parse(address);
      lockHashes.add(addressParseResult.script.computeHash());
    }
    Map<String, List<CellInput>> lockInputMap =
        new CellCollectorWithIndexer(api)
            .collectInputs(
                lockHashes, cellOutputs, feeRate, initialLength, cellDeps, cellOutputsData);

    for (Map.Entry<String, List<CellInput>> entry : lockInputMap.entrySet()) {
      cellsWithAddresses.add(
          new CellsWithAddress(
              entry.getValue(), sendAddresses.get(lockHashes.indexOf(entry.getKey()))));
    }
    return cellsWithAddresses;
  }

  public List<CellOutput> generateOutputs(List<Receiver> receivers, String changeAddress) {
    List<CellOutput> cellOutputs = new ArrayList<>();
    for (Receiver receiver : receivers) {
      AddressParseResult addressParseResult = AddressParser.parse(receiver.address);
      cellOutputs.add(
          new CellOutput(
              Numeric.prependHexPrefix(receiver.capacity.toString(16)), addressParseResult.script));
    }
    BigInteger needCapacity = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacity = needCapacity.add(receiver.capacity);
    }

    AddressParseResult addressParseResult = AddressParser.parse(changeAddress);
    cellOutputs.add(new CellOutput("0x0", addressParseResult.script));

    return cellOutputs;
  }
}
