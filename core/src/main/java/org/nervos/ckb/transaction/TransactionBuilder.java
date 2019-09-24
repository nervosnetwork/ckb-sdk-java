package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.Witness;
import org.nervos.ckb.methods.type.cell.CellDep;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.utils.Network;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionBuilder {

  private static final BigInteger MIN_CAPACITY = new BigInteger("6000000000");

  private SystemScriptCell systemSecpCell;
  private CKBService ckbService;

  public TransactionBuilder(CKBService ckbService) {
    this.ckbService = ckbService;

    try {
      this.systemSecpCell = Utils.getSystemScriptCell(ckbService);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Transaction generateTx(String privateKey, List<Receiver> receivers) throws IOException {
    AddressUtils addressUtils = new AddressUtils(Network.TESTNET);
    String changeAddress =
        addressUtils.generate(addressUtils.blake160(ECKeyPair.publicKeyFromPrivate(privateKey)));
    return generateTx(privateKey, receivers, changeAddress);
  }

  public Transaction generateTx(String privateKey, List<Receiver> receivers, String changeAddress)
      throws IOException {
    BigInteger needCapacities = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacities = needCapacities.add(receiver.capacity);
    }
    Sender sender = new Sender(privateKey, needCapacities);
    return generateTx(Collections.singletonList(sender), receivers, changeAddress);
  }

  public Transaction generateTx(
      List<Sender> senders, List<Receiver> receivers, String changeAddress) throws IOException {
    BigInteger needCapacities = BigInteger.ZERO;
    for (Receiver receiver : receivers) {
      needCapacities = needCapacities.add(receiver.capacity);
    }
    if (needCapacities.compareTo(MIN_CAPACITY) < 0) {
      throw new IOException("Less than min capacity");
    }

    BigInteger sendCapacities = BigInteger.ZERO;
    for (Sender sender : senders) {
      sendCapacities = sendCapacities.add(sender.capacity);
    }
    if (sendCapacities.compareTo(needCapacities) < 0) {
      throw new IOException("Sender capacity amount less than receiver capacity amount");
    }

    // gather cell inputs through senders' addresses
    // and put cellInput and related private key to a list
    BigInteger collectedCapacity = BigInteger.ZERO;
    List<Transaction.CellWithPrivateKey> cellWithPrivateKeys = new ArrayList<>();
    List<CellInput> cellInputs = new ArrayList<>();
    List<Witness> witnesses = new ArrayList<>();
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
      collectedCells.privateKey = sender.privateKey;
      cellInputs.addAll(collectedCells.inputs);

      for (CellInput cellInput : collectedCells.inputs) {
        cellWithPrivateKeys.add(new Transaction.CellWithPrivateKey(cellInput, sender.privateKey));
      }

      for (int i = 0; i < collectedCells.inputs.size(); i++) {
        witnesses.add(new Witness());
      }
    }

    // generate cellOutputs with receivers
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

    // set change cell output
    if (collectedCapacity.compareTo(needCapacities) > 0) {
      String changeAddressBlake160 = addressUtils.getBlake160FromAddress(changeAddress);
      cellOutputs.add(
          new CellOutput(
              collectedCapacity.subtract(needCapacities).toString(),
              new Script(
                  systemSecpCell.cellHash,
                  Collections.singletonList(Numeric.prependHexPrefix(changeAddressBlake160)),
                  Script.TYPE)));
    }

    List<String> cellOutputsData = new ArrayList<>();
    for (int i = 0; i < cellOutputs.size(); i++) {
      cellOutputsData.add("0x");
    }

    Transaction transaction =
        new Transaction(
            "0",
            Collections.singletonList(new CellDep(systemSecpCell.outPoint, CellDep.DEP_GROUP)),
            Collections.emptyList(),
            cellInputs,
            cellOutputs,
            cellOutputsData,
            witnesses);

    return transaction.sign(cellWithPrivateKeys);
  }
}
