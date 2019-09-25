package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.exceptions.InvalidNumberOfWitnessesException;
import org.nervos.ckb.methods.type.Witness;
import org.nervos.ckb.methods.type.cell.CellDep;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionBuilder {

  private static final BigInteger MIN_CAPACITY = new BigInteger("6000000000");

  private SystemScriptCell systemSecpCell;
  private List<CellInput> cellInputs = new ArrayList<>();
  private List<CellOutput> cellOutputs = new ArrayList<>();
  private List<String> cellOutputsData = new ArrayList<>();
  private List<Witness> witnesses = new ArrayList<>();
  private Transaction transaction;

  public TransactionBuilder(CKBService ckbService) {
    try {
      this.systemSecpCell = Utils.getSystemScriptCell(ckbService);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addInput(CellInput input) {
    cellInputs.add(input);
  }

  public void addAllInputs(List<CellInput> inputs) {
    cellInputs.addAll(inputs);
  }

  public void addOutput(CellOutput output) {
    cellOutputs.add(output);
  }

  public void addAllOutputs(List<CellOutput> outputs) {
    cellOutputs.addAll(outputs);
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
      witnesses.add(new Witness());
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
  }

  public void signInput(int index, String privateKey) throws IOException {
    if (transaction == null) {
      throw new IOException("Transaction could not null");
    }
    if (witnesses.size() < cellInputs.size()) {
      throw new InvalidNumberOfWitnessesException("Invalid number of witnesses");
    }
    witnesses.set(index, signWitness(witnesses.get(index), privateKey));
  }

  public void sign(String privateKey) {
    for (Witness witness : witnesses) {
      witnesses.add(signWitness(witness, privateKey));
    }
  }

  private Witness signWitness(Witness witness, String privateKey) {
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    List<String> oldData = witness.data;
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(transaction.computeHash()));
    for (String datum : witness.data) {
      blake2b.update(Numeric.hexStringToByteArray(datum));
    }
    String message = blake2b.doFinalString();

    String signature =
        Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());
    witness.data = new ArrayList<>();
    witness.data.add(signature);
    witness.data.addAll(oldData);
    return witness;
  }

  public Transaction getTransaction() {
    return transaction;
  }
}
