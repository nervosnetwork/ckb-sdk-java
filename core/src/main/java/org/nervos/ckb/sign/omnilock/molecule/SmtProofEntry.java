package org.nervos.ckb.sign.omnilock.molecule;

import org.nervos.ckb.type.base.MoleculeException;
import org.nervos.ckb.type.base.MoleculeUtils;
import org.nervos.ckb.type.base.Table;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class SmtProofEntry extends Table {
  public static int FIELD_COUNT = 2;

  private byte mask;

  private SmtProof proof;

  private SmtProofEntry() {
  }

  @Nonnull
  public byte getMask() {
    return mask;
  }

  @Nonnull
  public SmtProof getProof() {
    return proof;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private byte mask;

    private SmtProof proof;

    private Builder() {
      proof = SmtProof.builder().build();
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, SmtProofEntry.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      if (offsets.length - 1 != FIELD_COUNT) {
        throw MoleculeException.invalidFieldCount(FIELD_COUNT, offsets.length - 1, SmtProofEntry.class);
      }
      byte[] itemBuf;
      mask = buf[offsets[0]];
      itemBuf = Arrays.copyOfRange(buf, offsets[1], offsets[2]);
      proof = SmtProof.builder(itemBuf).build();
    }

    public Builder setMask(@Nonnull byte mask) {
      Objects.requireNonNull(mask);
      this.mask = mask;
      return this;
    }

    public Builder setProof(@Nonnull SmtProof proof) {
      Objects.requireNonNull(proof);
      this.proof = proof;
      return this;
    }

    public SmtProofEntry build() {
      int[] offsets = new int[FIELD_COUNT];
      offsets[0] = 4 + 4 * FIELD_COUNT;
      offsets[1] = offsets[0] + 1;
      int[] fieldsSize = new int[FIELD_COUNT];
      fieldsSize[0] = 1;
      fieldsSize[1] = proof.getSize();
      byte[][] fieldsBuf = new byte[FIELD_COUNT][];
      fieldsBuf[0] = new byte[]{mask};
      fieldsBuf[1] = proof.toByteArray();
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
      SmtProofEntry t = new SmtProofEntry();
      t.buf = buf;
      t.mask = mask;
      t.proof = proof;
      return t;
    }
  }
}
