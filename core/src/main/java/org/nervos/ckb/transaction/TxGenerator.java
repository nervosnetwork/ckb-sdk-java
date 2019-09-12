package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.Witness;
import org.nervos.ckb.methods.type.cell.CellDep;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.utils.Network;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TxGenerator {

  private static final String MIN_CAPACITY = "6000000000";

  private String privateKey;
  private Script lockScript;
  private SystemScriptCell systemScriptCell;
  private CKBService ckbService;

  public TxGenerator(String privateKey, CKBService ckbService) {
    this.privateKey = privateKey;
    this.ckbService = ckbService;

    try {
      this.systemScriptCell = Utils.getSystemScriptCell(ckbService);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.lockScript = Utils.generateLockScriptWithPrivateKey(privateKey, systemScriptCell.cellHash);
  }

  public Transaction generateTx(List<Receiver> receivers) throws IOException {
    BigInteger needCapacities = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacities = needCapacities.add(receiver.capacity);
    }
    if (needCapacities.compareTo(new BigInteger(MIN_CAPACITY)) < 0) {
      throw new IOException("Less than min capacity");
    }

    Cells cellInputs =
        new CellGatherer(ckbService).getCellInputs(lockScript.computeHash(), needCapacities);
    if (cellInputs.capacity.compareTo(needCapacities) < 0) {
      throw new IOException("No enough Capacities");
    }

    List<CellOutput> cellOutputs = new ArrayList<>();
    AddressUtils addressUtils = new AddressUtils(Network.TESTNET);
    for (Receiver receiver : receivers) {
      String blake2b = addressUtils.getBlake160FromAddress(receiver.address);
      cellOutputs.add(
          new CellOutput(
              Numeric.toHexStringWithPrefix(receiver.capacity),
              new Script(
                  systemScriptCell.cellHash, Collections.singletonList(blake2b), Script.TYPE)));
    }

    if (cellInputs.capacity.compareTo(needCapacities) > 0) {
      cellOutputs.add(
          new CellOutput(
              Numeric.toHexStringWithPrefix(cellInputs.capacity.subtract(needCapacities)),
              lockScript));
    }

    List<Witness> witnesses = new ArrayList<>();
    int len = cellInputs.inputs.size();
    for (int i = 0; i < len; i++) {
      witnesses.add(new Witness());
    }

    List<String> cellOutputsData = new ArrayList<>();
    for (int i = 0; i < cellOutputs.size(); i++) {
      cellOutputsData.add("0x");
    }

    Transaction transaction =
        new Transaction(
            "0x0",
            Collections.singletonList(new CellDep(systemScriptCell.outPoint, CellDep.DEP_GROUP)),
            Collections.emptyList(),
            cellInputs.inputs,
            cellOutputs,
            cellOutputsData,
            witnesses);

    return transaction.sign(Numeric.toBigInt(privateKey));
  }
}
