package org.nervos.ckb.udt;

import java.math.BigInteger;
import java.util.List;
import org.nervos.ckb.transaction.CellsWithAddress;
import org.nervos.ckb.transaction.CollectResult;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class UDTCollectResult extends CollectResult {
  public BigInteger changeUdtAmount;

  public UDTCollectResult(
      List<CellsWithAddress> cellsWithAddresses,
      String changeCapacity,
      BigInteger changeUdtAmount) {
    super(cellsWithAddresses, changeCapacity);
    this.changeUdtAmount = changeUdtAmount;
  }
}
