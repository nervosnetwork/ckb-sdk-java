package org.nervos.ckb.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
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

  public String sendCapacity(List<ReceiverBean> receiverBeanList) {
    try {
      Transaction transaction = generateTx(receiverBeanList);
      CkbTransactionHash temp = ckbService.sendTransaction(transaction).send();
      if (temp.result == null) {
        System.out.println(temp.error.message);
      }
      return temp.getTransactionHash();
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public Transaction generateTx(List<ReceiverBean> receiverBeanList) throws Exception {
    long needCapacities =
        receiverBeanList
            .stream()
            .mapToLong(receiverBean -> Long.valueOf(receiverBean.capacity))
            .sum();
    if (needCapacities < Long.valueOf(MIN_CAPACITY)) {
      throw new Exception("Less than min capacity");
    }

    CkbSystemContract systemContract =
        SystemContract.getSystemContract(ckbService, Network.TESTNET);
    InputsBean inputsBean = generateInputs(inputLockScript.scriptHash(), needCapacities);
    if (inputsBean.capacity < Long.valueOf(needCapacities)) {
      throw new Exception("No enough Capacities");
    }
    List<CellOutput> cellOutputs = new ArrayList<>();
    receiverBeanList.forEach(
        (receiverBean -> {
          AddressUtils addressUtils = new AddressUtils(Network.TESTNET);
          String blake2b = addressUtils.getBlake160FromAddress(receiverBean.address);
          cellOutputs.add(
              new CellOutput(
                  receiverBean.capacity,
                  "0x",
                  new Script(systemContract.systemScriptCellHash, Arrays.asList(blake2b))));
        }));
    cellOutputs.add(
        new CellOutput(
            String.valueOf(inputsBean.capacity - needCapacities), "0x", inputLockScript));
    Transaction transaction =
        new Transaction(
            "0",
            Arrays.asList(new OutPoint(null, systemContract.systemScriptOutPoint)),
            inputsBean.inputs,
            cellOutputs,
            new ArrayList<>());
    String txHash = ckbService.computeTransactionHash(transaction).send().getTransactionHash();
    Witness witness = new Witness(Numeric.toBigInt(privateKeyHex), txHash);
    inputsBean.inputs.forEach(
        cellInput -> {
          transaction.witnesses.add(witness);
        });
    return transaction;
  }

  public InputsBean generateInputs(String lockHash, long needCapacities) throws Exception {
    List<CellInput> cellInputs = new ArrayList<>();
    AtomicLong inputsCapacities = new AtomicLong();
    long toBlockNumber = ckbService.getTipBlockNumber().send().getBlockNumber().longValue();
    long fromBlockNumber = 1;
    while (fromBlockNumber <= toBlockNumber && inputsCapacities.get() < needCapacities) {
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
          inputsCapacities.addAndGet(Long.valueOf(cellOutputWithOutPoint.capacity));
          cellInputs.add(cellInput);
          if (inputsCapacities.get() > needCapacities) {
            break;
          }
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    return new InputsBean(cellInputs, inputsCapacities.get());
  }

  class InputsBean {
    List<CellInput> inputs;
    long capacity;

    public InputsBean(List<CellInput> inputs, long capacity) {
      this.inputs = inputs;
      this.capacity = capacity;
    }
  }

  public static class ReceiverBean {
    String address;
    String capacity;

    public ReceiverBean(String address, String capacity) {
      this.address = address;
      this.capacity = capacity;
    }
  }
}
