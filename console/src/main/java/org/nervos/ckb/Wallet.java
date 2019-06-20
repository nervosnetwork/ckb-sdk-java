package org.nervos.ckb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.methods.response.CkbTransactionHash;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.methods.type.cell.CellOutputWithOutPoint;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.utils.Network;
import org.nervos.ckb.utils.Numeric;

public class Wallet {

  private static final String MIN_CAPACITY = "6000000000";

  private Script lockScript;
  private String privateKey;
  private CKBService ckbService;

  public Wallet(String privateKey, Script lockScript, String nodeUrl) {
    this.privateKey = privateKey;
    this.lockScript = lockScript;
    ckbService = CKBService.build(new HttpService(nodeUrl));
  }

  public String sendCapacity(List<Receiver> receivers) throws Exception {
    Transaction transaction = generateTx(receivers);
    CkbTransactionHash ckbTransactionHash = ckbService.sendTransaction(transaction).send();
    if (ckbTransactionHash.result == null) {
      System.out.println(ckbTransactionHash.error.message);
    }
    return ckbTransactionHash.getTransactionHash();
  }

  private Transaction generateTx(List<Receiver> receivers) throws Exception {
    BigInteger needCapacities = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacities = needCapacities.add(receiver.capacity);
    }
    if (needCapacities.compareTo(new BigInteger(MIN_CAPACITY)) < 0) {
      throw new Exception("Less than min capacity");
    }

    CellInputs cellInputs = getCellInputs(lockScript.scriptHash(), needCapacities);
    if (cellInputs.capacity.compareTo(needCapacities) < 0) {
      throw new Exception("No enough Capacities");
    }

    SystemScriptCell systemScriptCell =
        SystemContract.getSystemScriptCell(ckbService, Network.TESTNET);
    List<CellOutput> cellOutputs = new ArrayList<>();
    AddressUtils addressUtils = new AddressUtils(Network.TESTNET);
    for (Receiver receiver : receivers) {
      String blake2b = addressUtils.getBlake160FromAddress(receiver.address);
      cellOutputs.add(
          new CellOutput(
              receiver.capacity.toString(),
              "0x",
              new Script(systemScriptCell.cellHash, Arrays.asList(blake2b))));
    }

    if (cellInputs.capacity.compareTo(needCapacities) > 0) {
      cellOutputs.add(
          new CellOutput(
              cellInputs.capacity.subtract(needCapacities).toString(10), "0x", lockScript));
    }

    Transaction transaction =
        new Transaction(
            "0",
            Collections.singletonList(new OutPoint(null, systemScriptCell.outPoint)),
            cellInputs.inputs,
            cellOutputs,
            new ArrayList<>());

    String txHash = ckbService.computeTransactionHash(transaction).send().getTransactionHash();
    return transaction.sign(Numeric.toBigInt(privateKey), txHash);
  }

  private CellInputs getCellInputs(String lockHash, BigInteger needCapacities) throws Exception {
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
    return new CellInputs(cellInputs, new BigDecimal(inputsCapacities).toBigInteger());
  }

  class CellInputs {
    List<CellInput> inputs;
    BigInteger capacity;

    public CellInputs(List<CellInput> inputs, BigInteger capacity) {
      this.inputs = inputs;
      this.capacity = capacity;
    }
  }

  public static class Receiver {
    String address;
    BigInteger capacity;

    public Receiver(String address, BigInteger capacity) {
      this.address = address;
      this.capacity = capacity;
    }
  }
}
