package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.Witness;
import org.nervos.ckb.methods.type.cell.CellDep;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.methods.type.cell.CellOutputWithOutPoint;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.system.SystemContract;
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
      this.systemScriptCell = getSystemScriptCell(ckbService);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.lockScript = generateLockScript(privateKey, systemScriptCell.cellHash);
  }

  public Transaction generateTx(List<Receiver> receivers) throws IOException {
    BigInteger needCapacities = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacities = needCapacities.add(receiver.capacity);
    }
    if (needCapacities.compareTo(new BigInteger(MIN_CAPACITY)) < 0) {
      throw new IOException("Less than min capacity");
    }

    Cells cellInputs = getCellInputs(getLockHash(lockScript), needCapacities);
    if (cellInputs.capacity.compareTo(needCapacities) < 0) {
      throw new IOException("No enough Capacities");
    }

    List<CellOutput> cellOutputs = new ArrayList<>();
    AddressUtils addressUtils = new AddressUtils(Network.TESTNET);
    for (Receiver receiver : receivers) {
      String blake2b = addressUtils.getBlake160FromAddress(receiver.address);
      cellOutputs.add(
          new CellOutput(
              receiver.capacity.toString(),
              new Script(
                  systemScriptCell.cellHash, Collections.singletonList(blake2b), Script.TYPE)));
    }

    if (cellInputs.capacity.compareTo(needCapacities) > 0) {
      cellOutputs.add(
          new CellOutput(cellInputs.capacity.subtract(needCapacities).toString(10), lockScript));
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
            "0",
            Collections.singletonList(new CellDep(systemScriptCell.outPoint, CellDep.DEP_GROUP)),
            Collections.emptyList(),
            cellInputs.inputs,
            cellOutputs,
            cellOutputsData,
            witnesses);

    String txHash = ckbService.computeTransactionHash(transaction).send().getTransactionHash();
    return transaction.sign(Numeric.toBigInt(privateKey), txHash);
  }

  private Cells getCellInputs(String lockHash, BigInteger needCapacities) throws IOException {
    List<CellInput> cellInputs = new ArrayList<>();
    BigInteger inputsCapacities = BigInteger.ZERO;
    long toBlockNumber = ckbService.getTipBlockNumber().send().getBlockNumber().longValue();
    long fromBlockNumber = 1;

    while (fromBlockNumber <= toBlockNumber && inputsCapacities.compareTo(needCapacities) < 0) {
      long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
      List<CellOutputWithOutPoint> cellOutputs =
          ckbService
              .getCellsByLockHash(
                  lockHash, String.valueOf(fromBlockNumber), String.valueOf(currentToBlockNumber))
              .send()
              .getCells();

      if (cellOutputs != null && cellOutputs.size() > 0) {
        for (CellOutputWithOutPoint cellOutputWithOutPoint : cellOutputs) {
          CellInput cellInput = new CellInput(cellOutputWithOutPoint.outPoint, "0");
          inputsCapacities =
              inputsCapacities.add(new BigDecimal(cellOutputWithOutPoint.capacity).toBigInteger());
          cellInputs.add(cellInput);
          if (inputsCapacities.compareTo(needCapacities) > 0) {
            break;
          }
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    return new Cells(cellInputs, new BigDecimal(inputsCapacities).toBigInteger());
  }

  private Script generateLockScript(String privateKey, String codeHash) {
    String publicKey = ECKeyPair.publicKeyFromPrivate(privateKey);
    String blake160 =
        Numeric.prependHexPrefix(Numeric.cleanHexPrefix(Hash.blake2b(publicKey)).substring(0, 40));
    return new Script(codeHash, Collections.singletonList(blake160), Script.TYPE);
  }

  private String getLockHash(Script script) throws IOException {
    return ckbService.computeScriptHash(script).send().getScriptHash();
  }

  private SystemScriptCell getSystemScriptCell(CKBService ckbService) throws Exception {
    return SystemContract.getSystemScriptCell(ckbService);
  }
}
