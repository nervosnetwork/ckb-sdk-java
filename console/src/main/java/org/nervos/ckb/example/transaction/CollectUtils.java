package org.nervos.ckb.example.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.transaction.CellGatherer;
import org.nervos.ckb.transaction.CollectedCells;
import org.nervos.ckb.transaction.Utils;
import org.nervos.ckb.utils.Network;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectUtils {

  private SystemScriptCell systemSecpCell;
  private CKBService ckbService;
  private BigInteger collectedCapacity = BigInteger.ZERO;

  public CollectUtils(CKBService ckbService) {
    this.ckbService = ckbService;
    try {
      systemSecpCell = Utils.getSystemScriptCell(ckbService);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<CellsWithPrivateKey> collectInputs(List<Sender> senders) throws IOException {
    List<CellsWithPrivateKey> cellsWithPrivateKeys = new ArrayList<>();
    for (Sender sender : senders) {
      String lockHash =
          Utils.generateLockScriptWithPrivateKey(sender.privateKey, systemSecpCell.cellHash)
              .computeHash();
      CollectedCells collectedCells =
          new CellGatherer(ckbService).getCellInputs(lockHash, sender.capacity);
      if (collectedCells.capacity.compareTo(sender.capacity) < 0) {
        throw new IOException("No enough Capacities with sender private key: " + sender.privateKey);
      }
      collectedCapacity = collectedCapacity.add(collectedCells.capacity);
      cellsWithPrivateKeys.add(new CellsWithPrivateKey(collectedCells.inputs, sender.privateKey));
    }
    return cellsWithPrivateKeys;
  }

  public List<CellOutput> generateOutputs(List<Receiver> receivers, String changeAddress) {
    List<CellOutput> cellOutputs = new ArrayList<>();
    AddressUtils addressUtils = new AddressUtils(Network.TESTNET);
    for (Receiver receiver : receivers) {
      String blake160 = addressUtils.getBlake160FromAddress(receiver.address);
      cellOutputs.add(
          new CellOutput(
              receiver.capacity.toString(),
              new Script(
                  systemSecpCell.cellHash, Collections.singletonList(blake160), Script.TYPE)));
    }
    BigInteger needCapacity = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacity = needCapacity.add(receiver.capacity);
    }
    if (collectedCapacity.compareTo(needCapacity) > 0) {
      String changeAddressBlake160 = addressUtils.getBlake160FromAddress(changeAddress);
      cellOutputs.add(
          new CellOutput(
              collectedCapacity.subtract(needCapacity).toString(),
              new Script(
                  systemSecpCell.cellHash,
                  Collections.singletonList(Numeric.prependHexPrefix(changeAddressBlake160)),
                  Script.TYPE)));
    }
    return cellOutputs;
  }
}
