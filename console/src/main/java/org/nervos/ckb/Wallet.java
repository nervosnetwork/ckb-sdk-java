package org.nervos.ckb;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.methods.response.CkbTransactionHash;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.Witness;
import org.nervos.ckb.methods.type.cell.CellDep;
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

  private String privateKey;
  private Script lockScript;
  private SystemScriptCell systemScriptCell;
  private CKBService ckbService;

  public Wallet(String privateKey, String nodeUrl) {
    this.privateKey = privateKey;
    HttpService.setDebug(false);
    ckbService = CKBService.build(new HttpService(nodeUrl));

    try {
      systemScriptCell = getSystemScriptCell(ckbService);
    } catch (IOException e) {
      e.printStackTrace();
    }
    lockScript = generateLockScript(privateKey, systemScriptCell.cellHash);
  }

  public String sendCapacity(List<Receiver> receivers) throws Exception {
    Transaction transaction = generateTx(receivers);
    CkbTransactionHash ckbTransactionHash = ckbService.sendTransaction(transaction).send();
    if (ckbTransactionHash.error != null) {
      throw new IOException(ckbTransactionHash.error.message);
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

    CellInputs cellInputs = getCellInputs(getLockHash(lockScript), needCapacities);
    if (cellInputs.capacity.compareTo(needCapacities) < 0) {
      throw new Exception("No enough Capacities");
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

  private Script generateLockScript(String privateKey, String codeHash) {
    String publicKey = Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey), true).toString(16);
    String blake160 =
        Numeric.prependHexPrefix(Numeric.cleanHexPrefix(Hash.blake2b(publicKey)).substring(0, 40));
    return new Script(codeHash, Collections.singletonList(blake160), Script.TYPE);
  }

  private String getLockHash(Script script) throws IOException {
    return ckbService.computeScriptHash(script).send().getScriptHash();
  }

  private SystemScriptCell getSystemScriptCell(CKBService ckbService) throws IOException {
    return SystemContract.getSystemScriptCell(ckbService);
  }

  static class CellInputs {
    List<CellInput> inputs;
    BigInteger capacity;

    CellInputs(List<CellInput> inputs, BigInteger capacity) {
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
