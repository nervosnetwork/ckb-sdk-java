package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.cell.CellOutputWithOutPoint;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Strings;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class CellBlockIterator implements Iterator<TransactionInput> {

  private List<TransactionInput> transactionInputs = new ArrayList<>();
  private long toBlockNumber;
  private long fromBlockNumber;
  private int addressIndex;
  private int inputIndex;

  private List<String> addresses;
  private Api api;
  private boolean skipDataAndType;

  CellBlockIterator(
      Api api, List<String> addresses, boolean skipDataAndType, long fromBlockNumber) {
    this.api = api;
    this.addresses = addresses;
    this.skipDataAndType = skipDataAndType;
    this.fromBlockNumber = fromBlockNumber;

    toBlockNumber = 0;
    addressIndex = 0;
    inputIndex = 0;

    try {
      toBlockNumber = api.getTipBlockNumber().longValue();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  CellBlockIterator(Api api, List<String> addresses, boolean skipDataAndType) {
    this(api, addresses, skipDataAndType, 0);
  }

  CellBlockIterator(Api api, List<String> addresses) {
    this(api, addresses, true);
  }

  @Override
  public boolean hasNext() {
    return transactionInputs.size() > 0
        || fromBlockNumber <= toBlockNumber
        || addressIndex < addresses.size();
  }

  @Override
  public TransactionInput next() {
    if (transactionInputs.size() > 0 && inputIndex < transactionInputs.size()) {
      return transactionInputs.get(inputIndex++);
    } else {
      transactionInputs.clear();
      inputIndex = 0;
      transactionInputs = fetchTransactionInputsForAddresses();
      if (transactionInputs == null || transactionInputs.size() == 0) {
        return null;
      }
      return transactionInputs.get(inputIndex++);
    }
  }

  private List<TransactionInput> fetchTransactionInputsForAddresses() {
    List<TransactionInput> transactionInputs;
    do {
      String lockHash = AddressParser.parse(addresses.get(addressIndex)).script.computeHash();
      transactionInputs = fetchTransactionInputsByLockHash(lockHash);
      if (transactionInputs == null || transactionInputs.size() == 0) {
        fromBlockNumber = 1;
        addressIndex++;
      }
      if (addressIndex >= addresses.size()) {
        return null;
      }
    } while (transactionInputs == null || transactionInputs.size() == 0);
    return transactionInputs;
  }

  private List<TransactionInput> fetchTransactionInputsByLockHash(String lockHash) {
    List<TransactionInput> transactionInputs = new ArrayList<>();
    List<CellOutputWithOutPoint> cellOutputList = new ArrayList<>();
    do {
      long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
      if (fromBlockNumber > currentToBlockNumber) {
        transactionInputs.clear();
        break;
      }
      try {
        cellOutputList =
            api.getCellsByLockHash(
                lockHash,
                BigInteger.valueOf(fromBlockNumber).toString(),
                BigInteger.valueOf(currentToBlockNumber).toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
      for (CellOutputWithOutPoint cellOutputWithOutPoint : cellOutputList) {
        if (skipDataAndType) {
          try {
            CellWithStatus cellWithStatus = api.getLiveCell(cellOutputWithOutPoint.outPoint, true);
            String outputsDataContent = cellWithStatus.cell.data.content;
            CellOutput cellOutput = cellWithStatus.cell.output;
            if ((!Strings.isEmpty(outputsDataContent) && !"0x".equals(outputsDataContent))
                || cellOutput.type != null) {
              continue;
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        CellInput cellInput = new CellInput(cellOutputWithOutPoint.outPoint, "0x0");
        BigInteger capacity = Numeric.toBigInt(cellOutputWithOutPoint.capacity);
        transactionInputs.add(new TransactionInput(cellInput, capacity, lockHash));
      }
      fromBlockNumber = currentToBlockNumber + 1;
      if (fromBlockNumber > toBlockNumber) {
        transactionInputs.clear();
        break;
      }
    } while (cellOutputList.size() == 0);
    return transactionInputs;
  }
}
