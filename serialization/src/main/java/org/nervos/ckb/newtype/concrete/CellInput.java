package org.nervos.ckb.newtype.concrete;

import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;
import org.nervos.ckb.newtype.base.Struct;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class CellInput extends Struct {
    public static int SIZE = 44;

    public static int FIELD_COUNT = 2;

    private Uint64 since;

    private OutPoint previousOutput;

    private CellInput() {
    }

    @Nonnull
    public Uint64 getSince() {
        return since;
    }

    @Nonnull
    public OutPoint getPreviousOutput() {
        return previousOutput;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(@Nonnull byte[] buf) {
        return new Builder(buf);
    }

    public static final class Builder {
        private Uint64 since;

        private OutPoint previousOutput;

        private Builder() {
            since = Uint64.builder().build();
            previousOutput = OutPoint.builder().build();
        }

        private Builder(@Nonnull byte[] buf) {
            Objects.requireNonNull(buf);
            if (buf.length != SIZE) {
                throw new MoleculeException(SIZE, buf.length, CellInput.class);
            }
            byte[] itemBuf;
            itemBuf = Arrays.copyOfRange(buf, 0, 8);
            since = Uint64.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, 8, 44);
            previousOutput = OutPoint.builder(itemBuf).build();
        }

        public Builder setSince(@Nonnull Uint64 since) {
            Objects.requireNonNull(since);
            this.since = since;
            return this;
        }

        public Builder setPreviousOutput(@Nonnull OutPoint previousOutput) {
            Objects.requireNonNull(previousOutput);
            this.previousOutput = previousOutput;
            return this;
        }

        public CellInput build() {
            int[] offsets = new int[FIELD_COUNT];
            offsets[0] = 0;
            offsets[1] = offsets[0] + Uint64.SIZE;
            byte[] buf = new byte[SIZE];
            MoleculeUtils.setBytes(since.toByteArray(), buf, offsets[0]);
            MoleculeUtils.setBytes(previousOutput.toByteArray(), buf, offsets[1]);
            CellInput s = new CellInput();
            s.buf = buf;
            s.since = since;
            s.previousOutput = previousOutput;
            return s;
        }
    }
}
