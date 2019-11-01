package org.nervos.ckb.example.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.address.CodeHashType;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.transaction.CellCollector;
import org.nervos.ckb.transaction.CellsWithPrivateKey;
import org.nervos.ckb.transaction.CollectedCells;
import org.nervos.ckb.transaction.Utils;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectUtils {

  private SystemScriptCell systemScriptCell;
  private Api api;
  private BigInteger collectedCapacity = BigInteger.ZERO;

  public CollectUtils(Api api) {
    this.api = api;
  }

  public List<CellsWithPrivateKey> collectInputs(List<Sender> senders) throws IOException {
    return collectInputs(senders, CodeHashType.BLAKE160);
  }

  public List<CellsWithPrivateKey> collectInputs(List<Sender> senders, CodeHashType codeHashType)
      throws IOException {
    List<CellsWithPrivateKey> cellsWithPrivateKeys = new ArrayList<>();
    if (codeHashType == CodeHashType.BLAKE160) {
      systemScriptCell = SystemContract.getSystemSecpCell(api);
    } else {
      systemScriptCell = SystemContract.getSystemMultiSigCell(api);
    }
    for (Sender sender : senders) {
      String lockHash =
          Utils.generateLockScriptWithPrivateKey(sender.privateKey, systemScriptCell.cellHash)
              .computeHash();
      CollectedCells collectedCells =
          new CellCollector(api).getCellInputs(lockHash, sender.capacity);
      if (collectedCells.capacity.compareTo(sender.capacity) < 0) {
        throw new IOException("No enough capacity with sender private key: " + sender.privateKey);
      }
      collectedCapacity = collectedCapacity.add(collectedCells.capacity);
      cellsWithPrivateKeys.add(new CellsWithPrivateKey(collectedCells.inputs, sender.privateKey));
    }
    return cellsWithPrivateKeys;
  }

  public List<CellOutput> generateOutputs(
      List<Receiver> receivers, String changeAddress, BigInteger fee) throws IOException {
    return generateOutputs(receivers, changeAddress, fee, CodeHashType.BLAKE160);
  }

  public List<CellOutput> generateOutputs(
      List<Receiver> receivers, String changeAddress, BigInteger fee, CodeHashType codeHashType)
      throws IOException {
    if (fee.compareTo(BigInteger.ZERO) < 0) {
      throw new IOException("Transaction fee should not be smaller than zero");
    }
    List<CellOutput> cellOutputs = new ArrayList<>();
    AddressUtils addressUtils = new AddressUtils(Network.TESTNET, codeHashType);
    if (codeHashType == CodeHashType.BLAKE160) {
      systemScriptCell = SystemContract.getSystemSecpCell(api);
    } else {
      systemScriptCell = SystemContract.getSystemMultiSigCell(api);
    }
    for (Receiver receiver : receivers) {
      String blake160 = addressUtils.getArgsFromAddress(receiver.address);
      cellOutputs.add(
          new CellOutput(
              receiver.capacity.toString(),
              new Script(systemScriptCell.cellHash, blake160, Script.TYPE)));
    }
    BigInteger needCapacity = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacity = needCapacity.add(receiver.capacity);
    }
    needCapacity = needCapacity.add(fee);

    if (collectedCapacity.compareTo(needCapacity) > 0) {
      String changeAddressBlake160 = addressUtils.getArgsFromAddress(changeAddress);
      cellOutputs.add(
          new CellOutput(
              collectedCapacity.subtract(needCapacity).toString(),
              new Script(
                  systemScriptCell.cellHash,
                  Numeric.prependHexPrefix(changeAddressBlake160),
                  Script.TYPE)));
    }
    return cellOutputs;
  }
}
