package org.nervos.ckb.acp;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.cell.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class AnyoneCanPayCellCollector {

  private Api api;

  public AnyoneCanPayCellCollector(Api api) {
    this.api = api;
  }

  public AnyoneCanPayCollectResult collectCell(String address) throws IOException {

    String lockHash = AddressParser.parse(address).script.computeHash();

    List<CellOutputWithOutPoint> cellOutputList;
    long toBlockNumber = api.getTipBlockNumber().longValue();
    long fromBlockNumber = 1;

    while (fromBlockNumber <= toBlockNumber) {
      long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
      cellOutputList =
          api.getCellsByLockHash(
              lockHash,
              BigInteger.valueOf(fromBlockNumber).toString(),
              BigInteger.valueOf(currentToBlockNumber).toString());
      for (CellOutputWithOutPoint cellOutputWithOutPoint : cellOutputList) {
        CellWithStatus cellWithStatus = api.getLiveCell(cellOutputWithOutPoint.outPoint, false);
        if (cellWithStatus != null && cellOutputWithOutPoint.outPoint != null) {
          CellInput cellInput = new CellInput(cellOutputWithOutPoint.outPoint, "0x0");
          return new AnyoneCanPayCollectResult(
              cellInput, Numeric.toBigInt(cellOutputWithOutPoint.capacity));
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    throw new IOException("Anyone can pay cell not found!");
  }
}
