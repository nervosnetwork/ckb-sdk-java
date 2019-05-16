package org.nervos.ckb.example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.methods.response.CkbTransactionHash;
import org.nervos.ckb.methods.type.*;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.bean.CkbSystemContract;
import org.nervos.ckb.utils.Network;
import org.nervos.ckb.utils.Numeric;

class TransactionExample {

  private static String MIN_CAPACITY = "6000000000";
  private static final String NODE_URL = "http://localhost:8114";

  private Script inputLockScript;
  private String privateKeyHex;
  private CKBService ckbService;

  public TransactionExample(String privateKeyHex, Script inputLockScript) {
    this.privateKeyHex = privateKeyHex;
    this.inputLockScript = inputLockScript;
    ckbService = CKBService.build(new HttpService(NODE_URL));
  }

  public String sendCapacity(List<Receiver> receiverList) throws Exception {
    Transaction transaction = generateTx(receiverList);
    CkbTransactionHash temp = ckbService.sendTransaction(transaction).send();
    if (temp.result == null) {
      System.out.println(temp.error.message);
    }
    return temp.getTransactionHash();
  }

  private Transaction generateTx(List<Receiver> receiverList) throws Exception {
    BigInteger needCapacities = BigInteger.ZERO;
    for (Receiver receiver : receiverList) {
      BigDecimal bigDecimal = new BigDecimal(receiver.capacity);
      needCapacities = needCapacities.add(bigDecimal.toBigInteger());
    }
    if (needCapacities.compareTo(new BigDecimal(MIN_CAPACITY).toBigInteger()) < 0) {
      throw new Exception("Less than min capacity");
    }

    CkbSystemContract systemContract =
        SystemContract.getSystemContract(ckbService, Network.TESTNET);
    CellInputsAndBalanceSum cellInputsAndBalanceSum =
        generateInputs(inputLockScript.scriptHash(), needCapacities);
    if (cellInputsAndBalanceSum.capacity.compareTo(needCapacities) < 0) {
      throw new Exception("No enough Capacities");
    }
    List<CellOutput> cellOutputs = new ArrayList<>();
    receiverList.forEach(
        (receiver -> {
          AddressUtils addressUtils = new AddressUtils(Network.TESTNET);
          String blake2b = addressUtils.getBlake160FromAddress(receiver.address);
          cellOutputs.add(
              new CellOutput(
                  receiver.capacity,
                  "0x",
                  new Script(systemContract.systemScriptCellHash, Arrays.asList(blake2b))));
        }));
    if (cellInputsAndBalanceSum.capacity.compareTo(needCapacities) > 0) {
      cellOutputs.add(
          new CellOutput(
              cellInputsAndBalanceSum.capacity.subtract(needCapacities).toString(10),
              "0x",
              inputLockScript));
    }
    Transaction transaction =
        new Transaction(
            "0",
            Arrays.asList(new OutPoint(null, systemContract.systemScriptOutPoint)),
            cellInputsAndBalanceSum.inputs,
            cellOutputs,
            new ArrayList<>());
    String txHash = ckbService.computeTransactionHash(transaction).send().getTransactionHash();
    Witness witness = new Witness(Numeric.toBigInt(privateKeyHex), txHash);
    cellInputsAndBalanceSum.inputs.forEach(
        cellInput -> {
          transaction.witnesses.add(witness);
        });
    return transaction;
  }

  private CellInputsAndBalanceSum generateInputs(String lockHash, BigInteger needCapacities)
      throws Exception {
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
          CellInput cellInput =
              new CellInput(cellOutputWithOutPoint.outPoint, Collections.emptyList(), "0");
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
    return new CellInputsAndBalanceSum(cellInputs, new BigDecimal(inputsCapacities).toBigInteger());
  }

  class CellInputsAndBalanceSum {
    List<CellInput> inputs;
    BigInteger capacity;

    public CellInputsAndBalanceSum(List<CellInput> inputs, BigInteger capacity) {
      this.inputs = inputs;
      this.capacity = capacity;
    }
  }

  public static class Receiver {
    String address;
    String capacity;

    public Receiver(String address, String capacity) {
      this.address = address;
      this.capacity = capacity;
    }
  }
}
