package org.nervos.ckb.sign.omnilock.molecule;

import org.nervos.ckb.type.base.MoleculeException;
import org.nervos.ckb.type.base.MoleculeUtils;
import org.nervos.ckb.type.base.Table;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class Identity extends Table {
    public static int FIELD_COUNT = 2;

    private Auth identity;

    private SmtProofEntryVec proofs;

    private Identity() {
    }

    @Nonnull
    public Auth getIdentity() {
        return identity;
    }

    @Nonnull
    public SmtProofEntryVec getProofs() {
        return proofs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(@Nonnull byte[] buf) {
        return new Builder(buf);
    }

    public static final class Builder {
        private Auth identity;

        private SmtProofEntryVec proofs;

        private Builder() {
            identity = Auth.builder().build();
            proofs = SmtProofEntryVec.builder().build();
        }

        private Builder(@Nonnull byte[] buf) {
            Objects.requireNonNull(buf);
            int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
            if (buf.length != size) {
                throw MoleculeException.invalidByteSize(size, buf.length, Identity.class);
            }
            int[] offsets = MoleculeUtils.getOffsets(buf);
            if (offsets.length - 1 != FIELD_COUNT) {
                throw MoleculeException.invalidFieldCount(FIELD_COUNT, offsets.length - 1, Identity.class);
            }
            byte[] itemBuf;
            itemBuf = Arrays.copyOfRange(buf, offsets[0], offsets[1]);
            identity = Auth.builder(itemBuf).build();
            itemBuf = Arrays.copyOfRange(buf, offsets[1], offsets[2]);
            proofs = SmtProofEntryVec.builder(itemBuf).build();
        }

        public Builder setIdentity(@Nonnull Auth identity) {
            Objects.requireNonNull(identity);
            this.identity = identity;
            return this;
        }

        public Builder setProofs(@Nonnull SmtProofEntryVec proofs) {
            Objects.requireNonNull(proofs);
            this.proofs = proofs;
            return this;
        }

        public Identity build() {
            int[] offsets = new int[FIELD_COUNT];
            offsets[0] = 4 + 4 * FIELD_COUNT;
            offsets[1] = offsets[0] + identity.getSize();
            int[] fieldsSize = new int[FIELD_COUNT];
            fieldsSize[0] = identity.getSize();
            fieldsSize[1] = proofs.getSize();
            byte[][] fieldsBuf = new byte[FIELD_COUNT][];
            fieldsBuf[0] = identity.toByteArray();
            fieldsBuf[1] = proofs.toByteArray();
            int size = 4 + 4 * FIELD_COUNT;
            for (int i = 0; i < FIELD_COUNT; i++) {
                size += fieldsSize[i];
            }
            byte[] buf = new byte[size];
            MoleculeUtils.setInt(size, buf, 0);
            int start = 4;
            for (int i = 0; i < FIELD_COUNT; i++) {
                MoleculeUtils.setInt(offsets[i], buf, start);
                start += 4;
            }
            for (int i = 0; i < FIELD_COUNT; i++) {
                MoleculeUtils.setBytes(fieldsBuf[i], buf, offsets[i]);
            }
            Identity t = new Identity();
            t.buf = buf;
            t.identity = identity;
            t.proofs = proofs;
            return t;
        }
    }
}
