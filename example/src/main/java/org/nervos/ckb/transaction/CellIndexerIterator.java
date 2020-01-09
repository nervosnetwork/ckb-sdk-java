package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.cell.LiveCell;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Strings;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class CellIndexerIterator implements Iterator<TransactionInput> {

  private static final int PAGE_SIZE = 50;

  private List<TransactionInput> transactionInputs = new ArrayList<>();
  private int addressIndex;
  private int inputIndex;
  private int pageNumber;

  private List<String> addresses;
  private Api api;
  private boolean skipDataAndType;

  CellIndexerIterator(Api api, List<String> addresses, boolean skipDataAndType) {
    this.api = api;
    this.addresses = addresses;
    this.skipDataAndType = skipDataAndType;

    addressIndex = 0;
    inputIndex = 0;
    pageNumber = 0;
  }

  CellIndexerIterator(Api api, List<String> addresses) {
    this(api, addresses, true);
  }

  @Override
  public boolean hasNext() {
    return transactionInputs.size() > 0 || addressIndex < addresses.size();
  }

  @Override
  public TransactionInput next() {
    if (transactionInputs.size() > 0 && inputIndex < transactionInputs.size()) {
      return transactionInputs.get(inputIndex++);
    } else {
      transactionInputs.clear();
      inputIndex = 0;
      do {
        String lockHash = AddressParser.parse(addresses.get(addressIndex)).script.computeHash();
        transactionInputs = fetchTransactionInputsByLockHash(lockHash);
        if (transactionInputs == null || transactionInputs.size() == 0) {
          pageNumber = 0;
          addressIndex++;
        }
        if (addressIndex >= addresses.size()) {
          return null;
        }
      } while (transactionInputs == null || transactionInputs.size() == 0);
      return transactionInputs.get(inputIndex++);
    }
  }

  private List<TransactionInput> fetchTransactionInputsByLockHash(String lockHash) {
    List<LiveCell> liveCells = new ArrayList<>();
    try {
      liveCells =
          api.getLiveCellsByLockHash(
              lockHash, String.valueOf(pageNumber++), String.valueOf(PAGE_SIZE), false);
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (LiveCell liveCell : liveCells) {
      if (skipDataAndType) {
        try {
          CellWithStatus cellWithStatus =
              api.getLiveCell(
                  new OutPoint(liveCell.createdBy.txHash, liveCell.createdBy.index), true);
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
      CellInput cellInput =
          new CellInput(new OutPoint(liveCell.createdBy.txHash, liveCell.createdBy.index), "0x0");
      BigInteger capacity = Numeric.toBigInt(liveCell.cellOutput.capacity);
      transactionInputs.add(new TransactionInput(cellInput, capacity, lockHash));
    }
    if (liveCells.size() == 0) {
      transactionInputs.clear();
    }
    return transactionInputs;
  }
}
