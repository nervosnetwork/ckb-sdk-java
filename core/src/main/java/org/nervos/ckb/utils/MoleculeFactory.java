package org.nervos.ckb.utils;

import org.nervos.ckb.newtype.concrete.*;

import java.math.BigInteger;
import java.util.List;

class MoleculeFactory {
    /**
     * Pad zero after byte array
     */
    private static byte[] padAfter(byte[] in, int length) {
        byte[] padBytes = new byte[length];
        System.arraycopy(in, 0, padBytes, 0, in.length);
        return padBytes;
    }

    private static byte[] flip(byte[] in) {
        byte[] out = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[in.length - i - 1];
        }
        return out;
    }

    protected static Uint32 createUnit32(long in) {
        byte[] arr = Numeric.toBytesPadded(BigInteger.valueOf(in), Uint32.SIZE);
        return Uint32
                .builder(padAfter(flip(arr), Uint32.SIZE))
                .build();
    }

    protected static Uint64 createUnit64(long in) {
        return createUnit64(BigInteger.valueOf(in));
    }

    protected static Uint64 createUnit64(BigInteger in) {
        return createUnit64(Numeric.toBytesPadded(in, Uint64.SIZE));
    }

    protected static Uint64 createUnit64(byte[] in) {
        return Uint64
                .builder(padAfter(flip(in), Uint64.SIZE))
                .build();
    }

    protected static Uint128 createUnit128(BigInteger in) {
        byte[] arr = Numeric.toBytesPadded(in, Uint128.SIZE);
        return Uint128
                .builder(padAfter(flip(arr), Uint128.SIZE))
                .build();
    }

    protected static Byte32 createByte32(byte[] in) {
        return Byte32.builder(in).build();
    }

    protected static Bytes createBytes(byte[] in) {
        if (in == null) {
            return null;
        }
        return Bytes.builder()
                .add(in)
                .build();
    }

    protected static BytesVec createBytesVec(List<byte[]> in) {
        Bytes[] arr = new Bytes[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createBytes(in.get(i));
        }
        return BytesVec.builder()
                .add(arr)
                .build();
    }

    protected static Byte32Vec createByte32Vec(List<byte[]> in) {
        Byte32[] arr = new Byte32[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createByte32(in.get(i));
        }
        return Byte32Vec.builder()
                .add(arr)
                .build();
    }

    protected static OutPoint createOutPoint(org.nervos.ckb.type.OutPoint in) {
        return OutPoint.builder()
                .setIndex(createUnit32(in.index))
                .setTxHash(createByte32(in.txHash))
                .build();
    }

    protected static CellInput createCellInput(org.nervos.ckb.type.cell.CellInput in) {
        return CellInput.builder()
                .setSince(createUnit64(in.since))
                .setPreviousOutput(createOutPoint(in.previousOutput))
                .build();
    }

    protected static CellInputVec createCellInputVec(List<org.nervos.ckb.type.cell.CellInput> in) {
        CellInput[] arr = new CellInput[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createCellInput(in.get(i));
        }
        return CellInputVec.builder()
                .add(arr)
                .build();
    }

    protected static CellOutput createCellOutput(org.nervos.ckb.type.cell.CellOutput in) {
        return CellOutput.builder()
                .setLock(createScript(in.lock))
                .setType(createScript(in.type))
                .setCapacity(createUnit64(in.capacity))
                .build();
    }

    protected static CellOutputVec createCellOutputVec(List<org.nervos.ckb.type.cell.CellOutput> in) {
        CellOutput[] arr = new CellOutput[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createCellOutput(in.get(i));
        }
        return CellOutputVec.builder()
                .add(arr)
                .build();
    }

    protected static CellDep createCellDep(org.nervos.ckb.type.cell.CellDep in) {
        return CellDep.builder()
                .setOutPoint(createOutPoint(in.outPoint))
                .setDepType(in.depType.toByte())
                .build();
    }

    protected static CellDepVec createCellDepVec(List<org.nervos.ckb.type.cell.CellDep> in) {
        CellDep[] arr = new CellDep[in.size()];
        for (int i = 0; i < in.size(); i++) {
            arr[i] = createCellDep(in.get(i));
        }
        return CellDepVec.builder()
                .add(arr)
                .build();
    }

    protected static Script createScript(org.nervos.ckb.type.Script in) {
        if (in == null) {
            return null;
        }
        return Script.builder()
                .setCodeHash(createByte32(in.codeHash))
                .setArgs(createBytes(in.args))
                .setHashType(in.hashType.toByte())
                .build();
    }

    protected static RawTransaction createRawTransaction(org.nervos.ckb.type.transaction.Transaction in) {
        return RawTransaction.builder()
                .setVersion(createUnit32(in.version))
                .setCellDeps(createCellDepVec(in.cellDeps))
                .setHeaderDeps(createByte32Vec(in.headerDeps))
                .setInputs(createCellInputVec(in.inputs))
                .setOutputs(createCellOutputVec(in.outputs))
                .setOutputsData(createBytesVec(in.outputsData))
                .build();
    }

    protected static Transaction createTransaction(org.nervos.ckb.type.transaction.Transaction in) {
        return Transaction.builder()
                .setRaw(createRawTransaction(in))
                .setWitnesses(createBytesVec(in.witnesses))
                .build();
    }

    protected static RawHeader createRawHeader(org.nervos.ckb.type.Header in) {
        RawHeader header = RawHeader.builder()
                .setVersion(createUnit32(in.version))
                .setCompactTarget(createUnit32(in.compactTarget))
                .setTimestamp(createUnit64(in.timestamp))
                .setNumber(createUnit64(in.number))
                .setEpoch(createUnit64(in.epoch))
                .setParentHash(createByte32(in.parentHash))
                .setTransactionsRoot(createByte32(in.transactionsRoot))
                .setProposalsHash(createByte32(in.proposalsHash))
                .setExtraHash(createByte32(in.extraHash))
                .setDao(createByte32(in.dao))
                .build();
        return header;
    }

    protected static Header createHeader(org.nervos.ckb.type.Header in) {
        return Header.builder()
                .setRaw(createRawHeader(in))
                .setNonce(createUnit128(in.nonce))
                .build();
    }

    protected static WitnessArgs createWitnessArgs(org.nervos.ckb.type.WitnessArgs in) {
        return WitnessArgs.builder()
                .setLock(createBytes(in.getLock()))
                .setInputType(createBytes(in.getInputType()))
                .setOutputType(createBytes(in.getOutputType()))
                .build();
    }
}
