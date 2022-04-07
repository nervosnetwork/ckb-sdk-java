package org.nervos.ckb.newtype.concrete;

import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;
import org.nervos.ckb.newtype.base.Struct;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class OutPoint extends Struct {
    public static int SIZE = 36;

    public static int FIELD_COUNT = 2;

    private Byte32 txHash;

    private Uint32 index;

    private OutPoint() {
    }

    @Nonnull
    public Byte32 getTxHash() {
        return txHash;
    }

    @Nonnull
    public Uint32 getIndex() {
        return index;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(@Nonnull byte[] buf) {
        return new Builder(buf);
    }

    public static final class Builder {
        private Byte32 txHash;

        private Uint32 index;

        private Builder() {
            txHash = Byte32.builder().build();
            index = Uint32.builder().build();
        }

        private Builder(@Nonnull byte[] buf) {
            Objects.requireNonNull(buf);
            if (buf.length != SIZE) {
                throw new MoleculeException(SIZE, buf.length, OutPoint.class);
            }
            byte[] itemBuf;
            itemBuf = Arrays.copyOfRange(buf, 0, 32);
            txHash = Byte32.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 32, 36);
            index = Uint32.builder(itemBuf).build();
        }

        public Builder setTxHash(@Nonnull Byte32 txHash) {
            Objects.requireNonNull(txHash);
            this.txHash = txHash;
            return this;
        }

        public Builder setIndex(@Nonnull Uint32 index) {
            Objects.requireNonNull(index);
            this.index = index;
            return this;
        }

        public OutPoint build() {
            int[] offsets = new int[FIELD_COUNT];
            offsets[0] = 0;
            offsets[1] = offsets[0] + Byte32.SIZE;
            byte[] buf = new byte[SIZE];
            MoleculeUtils.setBytes(txHash.toByteArray(), buf, offsets[0]);
            MoleculeUtils.setBytes(index.toByteArray(), buf, offsets[1]);
            OutPoint s = new OutPoint();
            s.buf = buf;
            s.txHash = txHash;
            s.index = index;
            return s;
        }
    }
}
