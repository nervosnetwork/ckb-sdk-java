package org.nervos.ckb.type.transaction;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.Encoder;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Transaction {

  public String version;

  public String hash;

  @SerializedName("cell_deps")
  public List<CellDep> cellDeps;

  @SerializedName("header_deps")
  public List<String> headerDeps;

  public List<CellInput> inputs;
  public List<CellOutput> outputs;

  @SerializedName("outputs_data")
  public List<String> outputsData;

  public List witnesses;

  public Transaction() {}

  public Transaction(
      String version,
      List<CellDep> cellDeps,
      List<String> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<String> outputsData) {
    this.version = version;
    this.cellDeps = cellDeps;
    this.headerDeps = headerDeps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.outputsData = outputsData;
  }

  public Transaction(
      String version,
      List<CellDep> cellDeps,
      List<String> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<String> outputsData,
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
      String version,
      String hash,
      List<CellDep> cellDeps,
      List<String> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<String> outputsData,
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

  public String computeHash() {
    Blake2b blake2b = new Blake2b();
    blake2b.update(Encoder.encode(Serializer.serializeRawTransaction(this)));
    return blake2b.doFinalString();
  }

  public Transaction sign(BigInteger privateKey) {
    if (witnesses.size() < 1) {
      throw new RuntimeException("Need at least one witness!");
    }
    if (witnesses.get(0).getClass() != Witness.class) {
      throw new RuntimeException("First witness must be of Witness type!");
    }
    String txHash = computeHash();
    Witness emptiedWitness = (Witness) witnesses.get(0);
    emptiedWitness.lock = Witness.SIGNATURE_PLACEHOLDER;
    Table witnessTable = Serializer.serializeWitnessArgs(emptiedWitness);
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(txHash));
    blake2b.update(new UInt64(witnessTable.getLength()).toBytes());
    blake2b.update(witnessTable.toBytes());
    for (int i = 1; i < witnesses.size(); i++) {
      byte[] bytes;
      if (witnesses.get(i).getClass() == Witness.class) {
        bytes = Serializer.serializeWitnessArgs((Witness) witnesses.get(i)).toBytes();
      } else {
        bytes = Numeric.hexStringToByteArray((String) witnesses.get(i));
      }
      blake2b.update(new UInt64(bytes.length).toBytes());
      blake2b.update(bytes);
    }
    String message = blake2b.doFinalString();
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    ((Witness) witnesses.get(0)).lock =
        Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());

    List<String> signedWitness = new ArrayList<>();
    for (Object witness : witnesses) {
      if (witness.getClass() == Witness.class) {
        signedWitness.add(
            Numeric.toHexString(Serializer.serializeWitnessArgs((Witness) witness).toBytes()));
      } else {
        signedWitness.add((String) witness);
      }
    }

    return new Transaction(
        version, cellDeps, headerDeps, inputs, outputs, outputsData, signedWitness);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    public String version;
    public List<CellDep> cellDeps;
    public List<String> headerDeps;
    public List<CellInput> inputs;
    public List<CellOutput> outputs;
    public List<String> outputsData;
    public List witnesses;

    private Builder() {
      this.version = "0x0";
      this.cellDeps = new ArrayList<>();
      this.headerDeps = new ArrayList<>();
      this.inputs = new ArrayList<>();
      this.outputs = new ArrayList<>();
      this.outputsData = new ArrayList<>();
      this.witnesses = new ArrayList();
    }

    public Builder setVersion(String version) {
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

    public Builder addCellDep(String txHash, int index, String depType) {
      String indexInString = Numeric.toHexString(new byte[] {Integer.valueOf(index).byteValue()});

      CellDep cellDep = new CellDep();
      cellDep.outPoint = new OutPoint(txHash, indexInString);
      cellDep.depType = depType;
      return this.addCellDep(cellDep);
    }

    public Builder addCellDep(String txHash, int index) {
      return this.addCellDep(txHash, index, "dep_group");
    }

    public Builder setHeaderDeps(List<String> headerDeps) {
      this.headerDeps = headerDeps;
      return this;
    }

    public Builder addHeaderDep(String headerDep) {
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

    public Builder addInput(String txHash, int index) {
      String indexInString = Numeric.toHexString(new byte[] {Integer.valueOf(index).byteValue()});
      return this.addInput(txHash, indexInString, "0x0");
    }

    public Builder addInput(String txHash, String index, String since) {
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
        String capacity,
        String lockScriptCodeHash,
        String lockScriptArgs,
        String typeScriptCodeHash,
        String typeScriptArgs) {
      Script lockScript = new Script(lockScriptCodeHash, lockScriptArgs, "type");
      Script typeScript = new Script(typeScriptCodeHash, typeScriptArgs, "type");

      CellOutput output = new CellOutput();
      output.capacity = capacity;
      output.lock = lockScript;
      output.type = typeScript;

      return addOutput(output);
    }

    public Builder addOutput(String capacity, String lockScriptCodeHash, String lockScriptArgs) {
      Script lockScript = new Script();
      lockScript.args = lockScriptArgs;
      lockScript.codeHash = lockScriptCodeHash;
      lockScript.hashType = "type";

      CellOutput output = new CellOutput();
      output.capacity = capacity;
      output.lock = lockScript;
      output.type = null;

      return addOutput(output);
    }

    public Builder setOutputsData(List<String> outputsData) {
      this.outputsData = outputsData;
      return this;
    }

    public Builder addOutputData(String outputData) {
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
