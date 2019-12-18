package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.cell.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Strings;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellFetcher {
  private static final int PAGE_SIZE = 50;

  private Api api;
  private boolean skipDataAndType;

  public CellFetcher(Api api, boolean skipDataAndType) {
    this.api = api;
    this.skipDataAndType = skipDataAndType;
  }

  public CellFetcher(Api api) {
    this.api = api;
    this.skipDataAndType = true;
  }

  public void fetchInputs(
      List<String> lockHashes, CollectIterator<CellInput, BigInteger, String> iterator)
      throws IOException {
    List<CellOutputWithOutPoint> cellOutputList;
    for (String lockHash : lockHashes) {
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
          if (skipDataAndType) {
            CellWithStatus cellWithStatus = api.getLiveCell(cellOutputWithOutPoint.outPoint, true);
            String outputsDataContent = cellWithStatus.cell.data.content;
            CellOutput cellOutput = cellWithStatus.cell.output;
            if ((!Strings.isEmpty(outputsDataContent) && !"0x".equals(outputsDataContent))
                || cellOutput.type != null) {
              continue;
            }
          }
          CellInput cellInput = new CellInput(cellOutputWithOutPoint.outPoint, "0x0");
          BigInteger capacity = Numeric.toBigInt(cellOutputWithOutPoint.capacity);
          if (iterator != null && !iterator.hasNext(cellInput, capacity, lockHash)) {
            break;
          }
        }
        fromBlockNumber = currentToBlockNumber + 1;
      }
    }
  }

  // Indexer Operations
  public void fetchInputsWithIndexer(
      List<String> lockHashes, CollectIterator<CellInput, BigInteger, String> iterator)
      throws IOException {
    List<LiveCell> liveCells;
    for (String lockHash : lockHashes) {
      long pageNumber = 0;
      while (true) {
        liveCells =
            api.getLiveCellsByLockHash(
                lockHash, String.valueOf(pageNumber), String.valueOf(PAGE_SIZE), false);
        if (liveCells == null || liveCells.size() == 0) break;
        for (LiveCell liveCell : liveCells) {
          if (skipDataAndType) {
            CellWithStatus cellWithStatus =
                api.getLiveCell(
                    new OutPoint(liveCell.createdBy.txHash, liveCell.createdBy.index), true);
            String outputsDataContent = cellWithStatus.cell.data.content;
            CellOutput cellOutput = cellWithStatus.cell.output;
            if ((!Strings.isEmpty(outputsDataContent) && !"0x".equals(outputsDataContent))
                || cellOutput.type != null) {
              continue;
            }
          }
          CellInput cellInput =
              new CellInput(
                  new OutPoint(liveCell.createdBy.txHash, liveCell.createdBy.index), "0x0");
          BigInteger capacity = Numeric.toBigInt(liveCell.cellOutput.capacity);
          if (iterator != null && !iterator.hasNext(cellInput, capacity, lockHash)) {
            break;
          }
        }
        pageNumber += 1;
      }
    }
  }

  public BigInteger getCapacityWithAddress(String address) throws IOException {
    AddressParseResult addressParseResult = AddressParser.parse(address);
    return getCapacityWithLockHash(addressParseResult.script.computeHash());
  }

  public BigInteger getCapacityWithAddress(String address, boolean withIndexer) throws IOException {
    AddressParseResult addressParseResult = AddressParser.parse(address);
    return getCapacityWithLockHash(addressParseResult.script.computeHash(), withIndexer);
  }

  public BigInteger getCapacityWithLockHash(String lockHash) throws IOException {
    return getCapacityWithLockHash(lockHash, false);
  }

  public BigInteger getCapacityWithLockHash(String lockHash, boolean withIndexer)
      throws IOException {
    List<BigInteger> capacityList = new ArrayList<>();
    capacityList.add(BigInteger.ZERO);
    CollectIterator<CellInput, BigInteger, String> collectIterator =
        new CollectIterator<CellInput, BigInteger, String>() {
          @Override
          public boolean hasNext(CellInput cellInput, BigInteger capacity, String lockHash) {
            capacityList.set(0, capacityList.get(0).add(capacity));
            return true;
          }
        };
    List<String> lockHashes = Collections.singletonList(lockHash);
    if (withIndexer) {
      fetchInputsWithIndexer(lockHashes, collectIterator);
    } else {
      fetchInputs(lockHashes, collectIterator);
    }
    return capacityList.get(0);
  }
}
