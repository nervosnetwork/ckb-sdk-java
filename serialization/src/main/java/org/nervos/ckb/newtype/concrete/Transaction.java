package org.nervos.ckb.newtype.concrete;

import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;
import org.nervos.ckb.newtype.base.Table;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class Transaction extends Table {
  public static int FIELD_COUNT = 2;

  private RawTransaction raw;

  private BytesVec witnesses;

  private Transaction() {
  }

  @Nonnull
  public RawTransaction getRaw() {
    return raw;
  }

  @Nonnull
  public BytesVec getWitnesses() {
    return witnesses;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private RawTransaction raw;

    private BytesVec witnesses;

    private Builder() {
      raw = RawTransaction.builder().build();
      witnesses = BytesVec.builder().build();
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, Transaction.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      if (offsets.length - 1 != FIELD_COUNT) {
        throw MoleculeException.invalidFieldCount(
            FIELD_COUNT, offsets.length - 1, Transaction.class);
      }
      byte[] itemBuf;
      itemBuf = Arrays.copyOfRange(buf, offsets[0], offsets[1]);
      raw = RawTransaction.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[1], offsets[2]);
      witnesses = BytesVec.builder(itemBuf).build();
    }

    public Builder setRaw(@Nonnull RawTransaction raw) {
      Objects.requireNonNull(raw);
      this.raw = raw;
      return this;
    }

    public Builder setWitnesses(@Nonnull BytesVec witnesses) {
      Objects.requireNonNull(witnesses);
      this.witnesses = witnesses;
      return this;
    }

    public Transaction build() {
      int[] offsets = new int[FIELD_COUNT];
      offsets[0] = 4 + 4 * FIELD_COUNT;
      offsets[1] = offsets[0] + raw.getSize();
      int[] fieldsSize = new int[FIELD_COUNT];
      fieldsSize[0] = raw.getSize();
      fieldsSize[1] = witnesses.getSize();
      byte[][] fieldsBuf = new byte[FIELD_COUNT][];
      fieldsBuf[0] = raw.toByteArray();
      fieldsBuf[1] = witnesses.toByteArray();
      int size = 4 + 4 * FIELD_COUNT;
      for (int i = 0; i < FIELD_COUNT; i++) {
        size += fieldsSize[i];
      }
      byte[] buf = new byte[size];
      ;
      MoleculeUtils.setInt(size, buf, 0);
      int start = 4;
      for (int i = 0; i < FIELD_COUNT; i++) {
        MoleculeUtils.setInt(offsets[i], buf, start);
        start += 4;
      }
      for (int i = 0; i < FIELD_COUNT; i++) {
        MoleculeUtils.setBytes(fieldsBuf[i], buf, offsets[i]);
      }
      Transaction t = new Transaction();
      t.buf = buf;
      t.raw = raw;
      t.witnesses = witnesses;
      return t;
    }
  }
}
