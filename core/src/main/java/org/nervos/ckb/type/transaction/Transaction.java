package org.nervos.ckb.type.transaction;

import static org.nervos.ckb.utils.MoleculeConverter.packBytesVec;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;

public class Transaction {
  public int version;
  public byte[] hash;
  public List<CellDep> cellDeps;
  public List<byte[]> headerDeps;
  public List<CellInput> inputs;
  public List<CellOutput> outputs;
  public List<byte[]> outputsData;
  public List<byte[]> witnesses;

  public Transaction() {}

  public Transaction(
      int version,
      List<CellDep> cellDeps,
      List<byte[]> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<byte[]> outputsData) {
    this.version = version;
    this.cellDeps = cellDeps;
    this.headerDeps = headerDeps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.outputsData = outputsData;
  }

  public Transaction(
      int version,
      List<CellDep> cellDeps,
      List<byte[]> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<byte[]> outputsData,
      List witnesses) {
    this.version = version;
    this.cellDeps = cellDeps;
    this.headerDeps = headerDeps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.outputsData = outputsData;
    this.witnesses = witnesses;
  }

  public Transaction(
      int version,
      byte[] hash,
      List<CellDep> cellDeps,
      List<byte[]> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<byte[]> outputsData,
      List witnesses) {
    this.version = version;
    this.hash = hash;
    this.cellDeps = cellDeps;
    this.headerDeps = headerDeps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.outputsData = outputsData;
    this.witnesses = witnesses;
  }

  public byte[] computeHash() {
    Blake2b blake2b = new Blake2b();
    blake2b.update(getRawTransaction().pack().toByteArray());
    return blake2b.doFinalBytes();
  }

  // TODO: sign with witness of byte array
  public Transaction sign(BigInteger privateKey) {
    //    if (witnesses.size() < 1) {
    //      throw new RuntimeException("Need at least one witness!");
    //    }
    //    if (witnesses.get(0).getClass() != Witness.class) {
    //      throw new RuntimeException("First witness must be of Witness type!");
    //    }
    //    byte[] txHash = computeHash();
    //    Witness emptiedWitness = (Witness) witnesses.get(0);
    //    emptiedWitness.lock = Witness.SIGNATURE_PLACEHOLDER;
    //    Table witnessTable = Serializer.serializeWitnessArgs(emptiedWitness);
    //    Blake2b blake2b = new Blake2b();
    //    blake2b.update(txHash);
    //    blake2b.update(new UInt64(witnessTable.getLength()).toBytes());
    //    blake2b.update(witnessTable.toBytes());
    //    for (int i = 1; i < witnesses.size(); i++) {
    //      byte[] bytes;
    //      if (witnesses.get(i).getClass() == Witness.class) {
    //        bytes = Serializer.serializeWitnessArgs((Witness) witnesses.get(i)).toBytes();
    //      } else {
    //        bytes = Numeric.hexStringToByteArray((String) witnesses.get(i));
    //      }
    //      blake2b.update(new UInt64(bytes.length).toBytes());
    //      blake2b.update(bytes);
    //    }
    //    String message = blake2b.doFinalString();
    //    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    //    ((Witness) witnesses.get(0)).lock =
    //        Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature();
    //
    //    List<String> signedWitness = new ArrayList<>();
    //    for (Object witness : witnesses) {
    //      if (witness.getClass() == Witness.class) {
    //        signedWitness.add(
    //            Numeric.toHexString(Serializer.serializeWitnessArgs((Witness)
    // witness).toBytes()));
    //      } else {
    //        signedWitness.add((String) witness);
    //      }
    //    }
    //
    //    return new Transaction(
    //        version, cellDeps, headerDeps, inputs, outputs, outputsData, signedWitness);
    return null;
  }

  public RawTransaction getRawTransaction() {
    RawTransaction rawTransaction = new RawTransaction();
    rawTransaction.version = version;
    rawTransaction.cellDeps = cellDeps;
    rawTransaction.headerDeps = headerDeps;
    rawTransaction.inputs = inputs;
    rawTransaction.outputs = outputs;
    rawTransaction.outputsData = outputsData;
    return rawTransaction;
  }

  public org.nervos.ckb.newtype.concrete.Transaction pack() {
    return org.nervos.ckb.newtype.concrete.Transaction.builder()
        .setRaw(getRawTransaction().pack())
        .setWitnesses(packBytesVec(witnesses))
        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    public int version;
    public List<CellDep> cellDeps;
    public List<byte[]> headerDeps;
    public List<CellInput> inputs;
    public List<CellOutput> outputs;
    public List<byte[]> outputsData;
    public List witnesses;

    private Builder() {
      this.version = 0;
      this.cellDeps = new ArrayList<>();
      this.headerDeps = new ArrayList<>();
      this.inputs = new ArrayList<>();
      this.outputs = new ArrayList<>();
      this.outputsData = new ArrayList<>();
      this.witnesses = new ArrayList();
    }

    public Builder setVersion(int version) {
      this.version = version;
      return this;
    }

    public Builder setCellDeps(List<CellDep> cellDeps) {
      this.cellDeps = cellDeps;
      return this;
    }

    public Builder addCellDep(CellDep cellDep) {
      this.cellDeps.add(cellDep);
      return this;
    }

    public Builder addCellDep(byte[] txHash, int index, CellDep.DepType depType) {
      CellDep cellDep = new CellDep();
      cellDep.outPoint = new OutPoint(txHash, index);
      cellDep.depType = depType;
      return this.addCellDep(cellDep);
    }

    public Builder addCellDep(byte[] txHash, int index) {
      return this.addCellDep(txHash, index, CellDep.DepType.DEP_GROUP);
    }

    public Builder setHeaderDeps(List<byte[]> headerDeps) {
      this.headerDeps = headerDeps;
      return this;
    }

    public Builder addHeaderDep(byte[] headerDep) {
      this.headerDeps.add(headerDep);
      return this;
    }

    public Builder setInputs(List<CellInput> inputs) {
      this.inputs = inputs;
      return this;
    }

    public Builder addInput(CellInput input) {
      this.inputs.add(input);
      return this;
    }

    public Builder addInput(byte[] txHash, int index) {
      return this.addInput(txHash, index, 0);
    }

    public Builder addInput(byte[] txHash, int index, long since) {
      CellInput input = new CellInput();
      input.previousOutput = new OutPoint(txHash, index);
      input.since = since;
      return this.addInput(input);
    }

    public Builder setOutputs(List<CellOutput> outputs) {
      this.outputs = outputs;
      return this;
    }

    public Builder addOutput(CellOutput output) {
      this.outputs.add(output);
      return this;
    }

    public Builder addOutput(
        BigInteger capacity,
        byte[] lockScriptCodeHash,
        byte[] lockScriptArgs,
        byte[] typeScriptCodeHash,
        byte[] typeScriptArgs) {
      Script lockScript = new Script(lockScriptCodeHash, lockScriptArgs, Script.HashType.TYPE);
      Script typeScript = new Script(typeScriptCodeHash, typeScriptArgs, Script.HashType.TYPE);

      CellOutput output = new CellOutput();
      output.capacity = capacity;
      output.lock = lockScript;
      output.type = typeScript;

      return addOutput(output);
    }

    public Builder addOutput(
        BigInteger capacity, byte[] lockScriptCodeHash, byte[] lockScriptArgs) {
      Script lockScript = new Script();
      lockScript.args = lockScriptArgs;
      lockScript.codeHash = lockScriptCodeHash;
      lockScript.hashType = Script.HashType.TYPE;

      CellOutput output = new CellOutput();
      output.capacity = capacity;
      output.lock = lockScript;
      output.type = null;

      return addOutput(output);
    }

    public Builder setOutputsData(List<byte[]> outputsData) {
      this.outputsData = outputsData;
      return this;
    }

    public Builder addOutputData(byte[] outputData) {
      this.outputsData.add(outputData);
      return this;
    }

    public Builder setWitnesses(List witnesses) {
      this.witnesses = witnesses;
      return this;
    }

    public Builder addWitness(String witness) {
      this.witnesses.add(witness);
      return this;
    }

    public Transaction build() {
      Transaction transaction =
          new Transaction(version, cellDeps, headerDeps, inputs, outputs, outputsData, witnesses);
      return transaction;
    }
  }
}
