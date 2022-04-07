package org.nervos.ckb.newtype.concrete;

import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;
import org.nervos.ckb.newtype.base.Struct;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class RawHeader extends Struct {
    public static int SIZE = 192;

    public static int FIELD_COUNT = 10;

    private Uint32 version;

    private Uint32 compactTarget;

    private Uint64 timestamp;

    private Uint64 number;

    private Uint64 epoch;

    private Byte32 parentHash;

    private Byte32 transactionsRoot;

    private Byte32 proposalsHash;

    private Byte32 extraHash;

    private Byte32 dao;

    private RawHeader() {
    }

    @Nonnull
    public Uint32 getVersion() {
        return version;
    }

    @Nonnull
    public Uint32 getCompactTarget() {
        return compactTarget;
    }

    @Nonnull
    public Uint64 getTimestamp() {
        return timestamp;
    }

    @Nonnull
    public Uint64 getNumber() {
        return number;
    }

    @Nonnull
    public Uint64 getEpoch() {
        return epoch;
    }

    @Nonnull
    public Byte32 getParentHash() {
        return parentHash;
    }

    @Nonnull
    public Byte32 getTransactionsRoot() {
        return transactionsRoot;
    }

    @Nonnull
    public Byte32 getProposalsHash() {
        return proposalsHash;
    }

    @Nonnull
    public Byte32 getExtraHash() {
        return extraHash;
    }

    @Nonnull
    public Byte32 getDao() {
        return dao;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(@Nonnull byte[] buf) {
        return new Builder(buf);
    }

    public static final class Builder {
        private Uint32 version;

        private Uint32 compactTarget;

        private Uint64 timestamp;

        private Uint64 number;

        private Uint64 epoch;

        private Byte32 parentHash;

        private Byte32 transactionsRoot;

        private Byte32 proposalsHash;

        private Byte32 extraHash;

        private Byte32 dao;

        private Builder() {
            version = Uint32.builder().build();
            compactTarget = Uint32.builder().build();
            timestamp = Uint64.builder().build();
            number = Uint64.builder().build();
            epoch = Uint64.builder().build();
            parentHash = Byte32.builder().build();
            transactionsRoot = Byte32.builder().build();
            proposalsHash = Byte32.builder().build();
            extraHash = Byte32.builder().build();
            dao = Byte32.builder().build();
        }

        private Builder(@Nonnull byte[] buf) {
            Objects.requireNonNull(buf);
            if (buf.length != SIZE) {
                throw new MoleculeException(SIZE, buf.length, RawHeader.class);
            }
            byte[] itemBuf;
            itemBuf = Arrays.copyOfRange(buf, 0, 4);
            version = Uint32.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 4, 8);
            compactTarget = Uint32.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 8, 16);
            timestamp = Uint64.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 16, 24);
            number = Uint64.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 24, 32);
            epoch = Uint64.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 32, 64);
            parentHash = Byte32.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 64, 96);
            transactionsRoot = Byte32.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 96, 128);
            proposalsHash = Byte32.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 128, 160);
            extraHash = Byte32.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 160, 192);
            dao = Byte32.builder(itemBuf).build();
        }

        public Builder setVersion(@Nonnull Uint32 version) {
            Objects.requireNonNull(version);
            this.version = version;
            return this;
        }

        public Builder setCompactTarget(@Nonnull Uint32 compactTarget) {
            Objects.requireNonNull(compactTarget);
            this.compactTarget = compactTarget;
            return this;
        }

        public Builder setTimestamp(@Nonnull Uint64 timestamp) {
            Objects.requireNonNull(timestamp);
            this.timestamp = timestamp;
            return this;
        }

        public Builder setNumber(@Nonnull Uint64 number) {
            Objects.requireNonNull(number);
            this.number = number;
            return this;
        }

        public Builder setEpoch(@Nonnull Uint64 epoch) {
            Objects.requireNonNull(epoch);
            this.epoch = epoch;
            return this;
        }

        public Builder setParentHash(@Nonnull Byte32 parentHash) {
            Objects.requireNonNull(parentHash);
            this.parentHash = parentHash;
            return this;
        }

        public Builder setTransactionsRoot(@Nonnull Byte32 transactionsRoot) {
            Objects.requireNonNull(transactionsRoot);
            this.transactionsRoot = transactionsRoot;
            return this;
        }

        public Builder setProposalsHash(@Nonnull Byte32 proposalsHash) {
            Objects.requireNonNull(proposalsHash);
            this.proposalsHash = proposalsHash;
            return this;
        }

        public Builder setExtraHash(@Nonnull Byte32 extraHash) {
            Objects.requireNonNull(extraHash);
            this.extraHash = extraHash;
            return this;
        }

        public Builder setDao(@Nonnull Byte32 dao) {
            Objects.requireNonNull(dao);
            this.dao = dao;
            return this;
        }

        public RawHeader build() {
            int[] offsets = new int[FIELD_COUNT];
            offsets[0] = 0;
            offsets[1] = offsets[0] + Uint32.SIZE;
            offsets[2] = offsets[1] + Uint32.SIZE;
            offsets[3] = offsets[2] + Uint64.SIZE;
            offsets[4] = offsets[3] + Uint64.SIZE;
            offsets[5] = offsets[4] + Uint64.SIZE;
            offsets[6] = offsets[5] + Byte32.SIZE;
            offsets[7] = offsets[6] + Byte32.SIZE;
            offsets[8] = offsets[7] + Byte32.SIZE;
            offsets[9] = offsets[8] + Byte32.SIZE;
            byte[] buf = new byte[SIZE];
            MoleculeUtils.setBytes(version.toByteArray(), buf, offsets[0]);
            MoleculeUtils.setBytes(compactTarget.toByteArray(), buf, offsets[1]);
            MoleculeUtils.setBytes(timestamp.toByteArray(), buf, offsets[2]);
            MoleculeUtils.setBytes(number.toByteArray(), buf, offsets[3]);
            MoleculeUtils.setBytes(epoch.toByteArray(), buf, offsets[4]);
            MoleculeUtils.setBytes(parentHash.toByteArray(), buf, offsets[5]);
            MoleculeUtils.setBytes(transactionsRoot.toByteArray(), buf, offsets[6]);
            MoleculeUtils.setBytes(proposalsHash.toByteArray(), buf, offsets[7]);
            MoleculeUtils.setBytes(extraHash.toByteArray(), buf, offsets[8]);
            MoleculeUtils.setBytes(dao.toByteArray(), buf, offsets[9]);
            RawHeader s = new RawHeader();
            s.buf = buf;
            s.version = version;
            s.compactTarget = compactTarget;
            s.timestamp = timestamp;
            s.number = number;
            s.epoch = epoch;
            s.parentHash = parentHash;
            s.transactionsRoot = transactionsRoot;
            s.proposalsHash = proposalsHash;
            s.extraHash = extraHash;
            s.dao = dao;
            return s;
        }
    }
}
