package org.nervos.ckb.utils;

import static org.nervos.ckb.methods.type.cell.CellDep.CODE;

import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.cell.CellDep;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.type.dynamic.Bytes;
import org.nervos.ckb.type.dynamic.Dynamic;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.fixed.*;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Serializer {

  public static Struct serializeOutPoint(OutPoint outPoint) {
    Byte32 txHash = new Byte32(outPoint.txHash);
    UInt32 index = new UInt32(outPoint.index);
    return new Struct(txHash, index);
  }

  public static Table serializeScript(Script script) {
    List<Bytes> argList = new ArrayList<>();
    for (String arg : script.args) {
      argList.add(new Bytes(arg));
    }
    return new Table(
        new Byte32(script.codeHash),
        new Byte1(Script.DATA.equals(script.hashType) ? "00" : "01"),
        new Dynamic<>(argList));
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
    List<Bytes> outputsDataList = new ArrayList<>();
    for (String data : bytes) {
      outputsDataList.add(new Bytes(data));
    }
    return new Dynamic<>(outputsDataList);
  }

  public static Fixed<Byte32> serializeByte32(List<String> bytes) {
    List<Byte32> outputsDataList = new ArrayList<>();
    for (String data : bytes) {
      outputsDataList.add(new Byte32(data));
    }
    return new Fixed<>(outputsDataList);
  }

  public static Table serializeTransaction(Transaction transaction) {
    UInt32 versionUInt32 = new UInt32(transaction.version);
    Fixed<Struct> cellDepFixed = Serializer.serializeCellDeps(transaction.cellDeps);
    Fixed<Byte32> headerDepFixed = Serializer.serializeByte32(transaction.headerDeps);
    Fixed<Struct> inputsFixed = Serializer.serializeCellInputs(transaction.inputs);
    Dynamic<Table> outputsVec = Serializer.serializeCellOutputs(transaction.outputs);
    Dynamic<Bytes> dataVec = Serializer.serializeBytes(transaction.outputsData);
    return new Table(versionUInt32, cellDepFixed, headerDepFixed, inputsFixed, outputsVec, dataVec);
  }
}
