package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectUtils {

  private Api api;
  private boolean skipDataAndType;

  public CollectUtils(Api api, boolean skipDataAndType) {
    this.api = api;
    this.skipDataAndType = skipDataAndType;
  }

  public CollectUtils(Api api) {
    this.api = api;
    this.skipDataAndType = true;
  }

  public CollectResult collectInputs(
      List<String> addresses, List<CellOutput> cellOutputs, BigInteger feeRate, int initialLength)
      throws IOException {
    return new CellCollector(api)
        .collectInputs(addresses, cellOutputs, feeRate, initialLength, null, null);
  }

  public CollectResult collectInputs(
      List<String> sendAddresses,
      List<CellOutput> cellOutputs,
      BigInteger feeRate,
      int initialLength,
      List<CellDep> cellDeps,
      List<String> cellOutputsData)
      throws IOException {
    return new CellCollector(api, skipDataAndType)
        .collectInputs(
            sendAddresses, cellOutputs, feeRate, initialLength, cellDeps, cellOutputsData);
  }

  public CollectResult collectInputsWithIndexer(
      List<String> addresses, List<CellOutput> cellOutputs, BigInteger feeRate, int initialLength)
      throws IOException {
    return collectInputsWithIndexer(addresses, cellOutputs, feeRate, initialLength, null, null);
  }

  public CollectResult collectInputsWithIndexer(
      List<String> addresses,
      List<CellOutput> cellOutputs,
      BigInteger feeRate,
      int initialLength,
      List<CellDep> cellDeps,
      List<String> cellOutputsData)
      throws IOException {
    return new CellCollectorWithIndexer(api)
        .collectInputs(addresses, cellOutputs, feeRate, initialLength, cellDeps, cellOutputsData);
  }

  public List<CellOutput> generateOutputs(List<Receiver> receivers, String changeAddress) {
    List<CellOutput> cellOutputs = new ArrayList<>();
    for (Receiver receiver : receivers) {
      AddressParseResult addressParseResult = AddressParser.parse(receiver.address);
      cellOutputs.add(
          new CellOutput(
              Numeric.toHexStringWithPrefix(receiver.capacity), addressParseResult.script));
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
