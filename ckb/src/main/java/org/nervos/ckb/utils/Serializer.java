package org.nervos.ckb.utils;

import static org.nervos.ckb.type.cell.CellDep.CODE;

import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.base.Type;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.dynamic.Bytes;
import org.nervos.ckb.type.dynamic.Dynamic;
import org.nervos.ckb.type.dynamic.Option;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.fixed.*;
import org.nervos.ckb.type.transaction.Transaction;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Serializer {

  public static Struct serializeOutPoint(OutPoint outPoint) {
    Byte32 txHash = new Byte32(outPoint.txHash);
    UInt32 index = new UInt32(outPoint.index);
    return new Struct(txHash, index);
  }

  public static Table serializeScript(Script script) {
    return new Table(
        new Byte32(script.codeHash),
        new Byte1(Script.DATA.equals(script.hashType) ? "00" : "01"),
        new Bytes(script.args));
  }

  public static Struct serializeCellInput(CellInput cellInput) {
    UInt64 sinceUInt64 = new UInt64(cellInput.since);
    Struct outPointStruct = serializeOutPoint(cellInput.previousOutput);
    return new Struct(sinceUInt64, outPointStruct);
  }

  public static Table serializeCellOutput(CellOutput cellOutput) {
    return new Table(
        new UInt64(cellOutput.capacity),
        serializeScript(cellOutput.lock),
        cellOutput.type != null ? serializeScript(cellOutput.type) : new Empty());
  }

  public static Struct serializeCellDep(CellDep cellDep) {
    Struct outPointStruct = serializeOutPoint(cellDep.outPoint);
    Byte1 depTypeBytes = CODE.equals(cellDep.depType) ? new Byte1("0") : new Byte1("1");
    return new Struct(outPointStruct, depTypeBytes);
  }

  public static Fixed<Struct> serializeCellDeps(List<CellDep> cellDeps) {
    List<Struct> cellDepList = new ArrayList<>();
    for (CellDep cellDep : cellDeps) {
      cellDepList.add(serializeCellDep(cellDep));
    }
    return new Fixed<>(cellDepList);
  }

  public static Fixed<Struct> serializeCellInputs(List<CellInput> cellInputs) {
    List<Struct> cellInputList = new ArrayList<>();
    for (CellInput cellInput : cellInputs) {
      cellInputList.add(serializeCellInput(cellInput));
    }
    return new Fixed<>(cellInputList);
  }

  public static Dynamic<Table> serializeCellOutputs(List<CellOutput> cellOutputs) {
    List<Table> cellOutputList = new ArrayList<>();
    for (CellOutput cellOutput : cellOutputs) {
      cellOutputList.add(serializeCellOutput(cellOutput));
    }
    return new Dynamic<>(cellOutputList);
  }

  public static Dynamic<Bytes> serializeBytes(List<String> bytes) {
    List<Bytes> bytesList = new ArrayList<>();
    for (String data : bytes) {
      bytesList.add(new Bytes(data));
    }
    return new Dynamic<>(bytesList);
  }

  public static Fixed<Byte32> serializeByte32(List<String> bytes) {
    List<Byte32> byte32List = new ArrayList<>();
    for (String data : bytes) {
      byte32List.add(new Byte32(data));
    }
    return new Fixed<>(byte32List);
  }

  public static Table serializeWitnessArgs(Witness witness) {
    return new Table(
        new Option(
            Strings.isEmpty(witness.lock) || "0x".equals(witness.lock)
                ? new Empty()
                : new Bytes(witness.lock)),
        new Option(
            Strings.isEmpty(witness.inputType) || "0x".equals(witness.inputType)
                ? new Empty()
                : new Bytes(witness.inputType)),
        new Option(
            Strings.isEmpty(witness.outputType) || "0x".equals(witness.outputType)
                ? new Empty()
                : new Bytes(witness.outputType)));
  }

  public static Dynamic<Type> serializeWitnesses(List witnesses) {
    List witnessList = new ArrayList<>();
    for (Object witness : witnesses) {
      if (witness.getClass() == Witness.class) {
        witnessList.add(new Bytes(serializeWitnessArgs((Witness) witness).toBytes()));
      } else {
        witnessList.add(new Bytes((String) witness));
      }
    }
    return new Dynamic<Type>(witnessList);
  }

  public static Table serializeRawTransaction(Transaction transaction) {
    Transaction tx = Convert.parseTransaction(transaction);
    UInt32 versionUInt32 = new UInt32(tx.version);
    Fixed<Struct> cellDepFixed = Serializer.serializeCellDeps(tx.cellDeps);
    Fixed<Byte32> headerDepFixed = Serializer.serializeByte32(tx.headerDeps);
    Fixed<Struct> inputsFixed = Serializer.serializeCellInputs(tx.inputs);
    Dynamic<Table> outputsVec = Serializer.serializeCellOutputs(tx.outputs);
    Dynamic<Bytes> dataVec = Serializer.serializeBytes(tx.outputsData);
    return new Table(versionUInt32, cellDepFixed, headerDepFixed, inputsFixed, outputsVec, dataVec);
  }

  public static Table serializeTransaction(Transaction transaction) {
    Table rawTransactionTable = serializeRawTransaction(transaction);
    Dynamic<Type> witnessesVec = Serializer.serializeWitnesses(transaction.witnesses);
    return new Table(rawTransactionTable, witnessesVec);
  }
}
