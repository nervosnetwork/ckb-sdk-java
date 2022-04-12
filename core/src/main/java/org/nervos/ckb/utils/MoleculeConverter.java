package org.nervos.ckb.utils;

import org.nervos.ckb.newtype.concrete.*;

import java.math.BigInteger;
import java.util.List;

class MoleculeConverter {
    public static Uint32 createUint32(long in) {
        return createUint32(BigInteger.valueOf(in));
    }

    public static byte[] littleEndianBigInteger(BigInteger in, int length) {
        byte[] arr = Numeric.toBytesPadded(in, length);
        byte[] out = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            out[i] = arr[arr.length - i - 1];
        }
        return out;
    }

    public static Uint32 createUint32(BigInteger in) {
        byte[] arr = littleEndianBigInteger(in, Uint32.SIZE);
        return Uint32
                .builder(arr)
                .build();
    }

    public static Uint64 createUint64(BigInteger in) {
        byte[] arr = littleEndianBigInteger(in, Uint64.SIZE);
        return Uint64
                .builder(arr)
                .build();
    }

    public static Uint64 createUint64(long in) {
        return createUint64(BigInteger.valueOf(in));
    }

    public static Uint128 createUint128(BigInteger in) {
        byte[] arr = littleEndianBigInteger(in, Uint128.SIZE);
        return Uint128
                .builder(arr)
                .build();
    }

    public static Byte32 createByte32(byte[] in) {
        return Byte32.builder(in).build();
    }

    public static Bytes createBytes(byte[] in) {
        if (in == null) {
            return null;
        }
        return Bytes.builder()
                .add(in)
                .build();
    }

    public static BytesVec createBytesVec(List<byte[]> in) {
        Bytes[] arr = new Bytes[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createBytes(in.get(i));
        }
        return BytesVec.builder()
                .add(arr)
                .build();
    }

    public static Byte32Vec createByte32Vec(List<byte[]> in) {
        Byte32[] arr = new Byte32[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createByte32(in.get(i));
        }
        return Byte32Vec.builder()
                .add(arr)
                .build();
    }

    public static OutPoint createOutPoint(org.nervos.ckb.type.OutPoint in) {
        return OutPoint.builder()
                .setIndex(createUint32(in.index))
                .setTxHash(createByte32(in.txHash))
                .build();
    }

    public static CellInput createCellInput(org.nervos.ckb.type.cell.CellInput in) {
        return CellInput.builder()
                .setSince(createUint64(in.since))
                .setPreviousOutput(createOutPoint(in.previousOutput))
                .build();
    }

    public static CellInputVec createCellInputVec(List<org.nervos.ckb.type.cell.CellInput> in) {
        CellInput[] arr = new CellInput[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createCellInput(in.get(i));
        }
        return CellInputVec.builder()
                .add(arr)
                .build();
    }

    public static CellOutput createCellOutput(org.nervos.ckb.type.cell.CellOutput in) {
        return CellOutput.builder()
                .setLock(createScript(in.lock))
                .setType(createScript(in.type))
                .setCapacity(createUint64(in.capacity))
                .build();
    }

    public static CellOutputVec createCellOutputVec(List<org.nervos.ckb.type.cell.CellOutput> in) {
        CellOutput[] arr = new CellOutput[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createCellOutput(in.get(i));
        }
        return CellOutputVec.builder()
                .add(arr)
                .build();
    }

    public static CellDep createCellDep(org.nervos.ckb.type.cell.CellDep in) {
        return CellDep.builder()
                .setOutPoint(createOutPoint(in.outPoint))
                .setDepType(in.depType.toByte())
                .build();
    }

    public static CellDepVec createCellDepVec(List<org.nervos.ckb.type.cell.CellDep> in) {
        CellDep[] arr = new CellDep[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createCellDep(in.get(i));
        }
        return CellDepVec.builder()
                .add(arr)
                .build();
    }

    public static Script createScript(org.nervos.ckb.type.Script in) {
        if (in == null) {
            return null;
        }
        return Script.builder()
                .setCodeHash(createByte32(in.codeHash))
                .setArgs(createBytes(in.args))
                .setHashType(in.hashType.toByte())
                .build();
    }

    public static RawTransaction createRawTransaction(org.nervos.ckb.type.transaction.Transaction in) {
        return RawTransaction.builder()
                .setVersion(createUint32(in.version))
                .setCellDeps(createCellDepVec(in.cellDeps))
                .setHeaderDeps(createByte32Vec(in.headerDeps))
                .setInputs(createCellInputVec(in.inputs))
                .setOutputs(createCellOutputVec(in.outputs))
                .setOutputsData(createBytesVec(in.outputsData))
                .build();
    }

    public static Transaction createTransaction(org.nervos.ckb.type.transaction.Transaction in) {
        return Transaction.builder()
                .setRaw(createRawTransaction(in))
                .setWitnesses(createBytesVec(in.witnesses))
                .build();
    }

    public static RawHeader createRawHeader(org.nervos.ckb.type.Header in) {
        RawHeader header = RawHeader.builder()
                .setVersion(createUint32(in.version))
                .setCompactTarget(createUint32(in.compactTarget))
                .setTimestamp(createUint64(in.timestamp))
                .setNumber(createUint64(in.number))
                .setEpoch(createUint64(in.epoch))
                .setParentHash(createByte32(in.parentHash))
                .setTransactionsRoot(createByte32(in.transactionsRoot))
                .setProposalsHash(createByte32(in.proposalsHash))
                .setExtraHash(createByte32(in.extraHash))
                .setDao(createByte32(in.dao))
                .build();
        return header;
    }

    public static Header createHeader(org.nervos.ckb.type.Header in) {
        return Header.builder()
                .setRaw(createRawHeader(in))
                .setNonce(createUint128(in.nonce))
                .build();
    }

    public static WitnessArgs createWitnessArgs(org.nervos.ckb.type.WitnessArgs in) {
        return WitnessArgs.builder()
                .setLock(createBytes(in.getLock()))
                .setInputType(createBytes(in.getInputType()))
                .setOutputType(createBytes(in.getOutputType()))
                .build();
    }
}
