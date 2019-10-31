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
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionBuilder {

  private static final BigInteger MIN_CAPACITY = new BigInteger("6000000000");

  private SystemScriptCell systemSecpCell;
  private List<CellInput> cellInputs = new ArrayList<>();
  private List<CellOutput> cellOutputs = new ArrayList<>();
  private List<String> cellOutputsData = new ArrayList<>();
  private List witnesses = new ArrayList<>();
  private Transaction transaction;

  public TransactionBuilder(Api api) {
    try {
      this.systemSecpCell = Utils.getSystemScriptCell(api);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addInput(CellInput input) {
    cellInputs.add(input);
  }

  public void addInputs(List<CellInput> inputs) {
    cellInputs.addAll(inputs);
  }

  public void addOutput(CellOutput output) {
    cellOutputs.add(output);
  }

  public void addOutputs(List<CellOutput> outputs) {
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
    }
    for (int i = 0; i < cellInputs.size(); i++) {
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

  public void sign(String privateKey) {
    transaction.sign(Numeric.toBigInt(privateKey));
  }

  private String signWitness(String witness, String privateKey) {
    Table witnessTable = Serializer.serializeWitnessArgs(new Witness());
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(transaction.computeHash()));
    blake2b.update(BigInteger.valueOf(witnessTable.getLength()).toByteArray());
    blake2b.update(witnessTable.toBytes());
    String message = blake2b.doFinalString();
    String signature =
        Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());
    return signature + Numeric.cleanHexPrefix(witness);
  }

  public Transaction getTransaction() {
    return transaction;
  }
}
