package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionBuilder {

  private static final BigInteger MIN_CAPACITY = new BigInteger("6000000000");

  private SystemScriptCell systemSecpCell;
  private List<CellInput> cellInputs = new ArrayList<>();
  private List<CellsWithLock> cellsWithLocks = new ArrayList<>();
  private List<CellOutput> cellOutputs = new ArrayList<>();
  private List<String> cellOutputsData = new ArrayList<>();
  private List<String> witnesses = new ArrayList<>();
  private Transaction transaction;

  public TransactionBuilder(Api api) {
    try {
      this.systemSecpCell = Utils.getSystemScriptCell(api);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addInputsWithLock(CellsWithLock cellsWithLock) {
    cellsWithLocks.add(cellsWithLock);
    cellInputs.addAll(cellsWithLock.inputs);
  }

  public void addInputsWithLocks(List<CellsWithLock> cellsWithLocks) {
    this.cellsWithLocks.addAll(cellsWithLocks);
    for (CellsWithLock cellsWithLock : cellsWithLocks) {
      cellInputs.addAll(cellsWithLock.inputs);
    }
  }

  public void addOutput(CellOutput output) {
    cellOutputs.add(output);
  }

  public void addOutputs(List<CellOutput> outputs) {
    cellOutputs.addAll(outputs);
  }

  private List<String> signWitness(List witnessesWithLock, String privateKey) {
    if (witnessesWithLock.size() < 1) {
      throw new RuntimeException("Need at least one witness!");
    }
    if (witnessesWithLock.get(0).getClass() != Witness.class) {
      throw new RuntimeException("First witness must be of Witness type!");
    }
    String txHash = transaction.computeHash();
    Witness emptiedWitness = (Witness) witnessesWithLock.get(0);
    emptiedWitness.lock = Witness.EMPTY_LOCK;
    Table witnessTable = Serializer.serializeWitnessArgs(emptiedWitness);
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(txHash));
    blake2b.update(new UInt64(witnessTable.getLength()).toBytes());
    blake2b.update(witnessTable.toBytes());
    for (int i = 1; i < witnessesWithLock.size(); i++) {
      byte[] bytes;
      if (witnessesWithLock.get(i).getClass() == Witness.class) {
        bytes = Serializer.serializeWitnessArgs((Witness) witnessesWithLock.get(i)).toBytes();
      } else {
        bytes = Numeric.hexStringToByteArray((String) witnessesWithLock.get(i));
      }
      blake2b.update(new UInt64(bytes.length).toBytes());
      blake2b.update(bytes);
    }
    String message = blake2b.doFinalString();
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    ((Witness) witnessesWithLock.get(0)).lock =
        Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());

    List<String> signedWitness = new ArrayList<>();
    for (Object witness : witnessesWithLock) {
      if (witness.getClass() == Witness.class) {
        signedWitness.add(
            Numeric.toHexString(Serializer.serializeWitnessArgs((Witness) witness).toBytes()));
      } else {
        signedWitness.add((String) witness);
      }
    }
    return signedWitness;
  }

  public void buildTx() throws IOException {
    BigInteger needCapacity = BigInteger.ZERO;
    for (CellOutput output : cellOutputs) {
      needCapacity = needCapacity.add(Numeric.toBigInt(output.capacity));
    }
    if (needCapacity.compareTo(MIN_CAPACITY) < 0) {
      throw new IOException("Less than min capacity");
    }
    if (cellInputs.size() == 0) {
      throw new IOException("Cell inputs could not empty");
    }
    for (int i = 0; i < cellOutputs.size(); i++) {
      cellOutputsData.add("0x");
    }

    transaction =
        new Transaction(
            "0",
            Collections.singletonList(new CellDep(systemSecpCell.outPoint, CellDep.DEP_GROUP)),
            Collections.emptyList(),
            cellInputs,
            cellOutputs,
            cellOutputsData,
            witnesses);

    List<String> signedWitnesses = new ArrayList<>();
    for (CellsWithLock cellsWithLock : cellsWithLocks) {
      List<Witness> witnessesWithLock = new ArrayList<>();
      for (int i = 0; i < cellsWithLock.inputs.size(); i++) {
        witnessesWithLock.add(new Witness());
      }
      signedWitnesses.addAll(signWitness(witnessesWithLock, cellsWithLock.privateKey));
    }
    transaction.witnesses = signedWitnesses;
  }

  public Transaction getTransaction() {
    return transaction;
  }
}
