package org.nervos.ckb.newtype.concrete;

import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;
import org.nervos.ckb.newtype.base.Table;

public final class CellOutput extends Table {
  public static int FIELD_COUNT = 3;

  private Uint64 capacity;

  private Script lock;

  private Script type;

  private CellOutput() {}

  @Nonnull
  public Uint64 getCapacity() {
    return capacity;
  }

  @Nonnull
  public Script getLock() {
    return lock;
  }

  @Nullable
  public Script getType() {
    return type;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private Uint64 capacity;

    private Script lock;

    private Script type;

    private Builder() {
      capacity = Uint64.builder().build();
      lock = Script.builder().build();
      type = null;
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, CellOutput.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      if (offsets.length - 1 != FIELD_COUNT) {
        throw MoleculeException.invalidFieldCount(
            FIELD_COUNT, offsets.length - 1, CellOutput.class);
      }
      byte[] itemBuf;
      itemBuf = Arrays.copyOfRange(buf, offsets[0], offsets[1]);
      capacity = Uint64.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[1], offsets[2]);
      lock = Script.builder(itemBuf).build();
      if (offsets[2] != offsets[3]) {
        itemBuf = Arrays.copyOfRange(buf, offsets[2], offsets[3]);
        type = Script.builder(itemBuf).build();
      }
    }

    public Builder setCapacity(@Nonnull Uint64 capacity) {
      Objects.requireNonNull(capacity);
      this.capacity = capacity;
      return this;
    }

    public Builder setLock(@Nonnull Script lock) {
      Objects.requireNonNull(lock);
      this.lock = lock;
      return this;
    }

    public Builder setType(@Nullable Script type) {
      this.type = type;
      return this;
    }

    public CellOutput build() {
      int[] offsets = new int[FIELD_COUNT];
      offsets[0] = 4 + 4 * FIELD_COUNT;
      offsets[1] = offsets[0] + capacity.getSize();
      offsets[2] = offsets[1] + lock.getSize();
      int[] fieldsSize = new int[FIELD_COUNT];
      fieldsSize[0] = capacity.getSize();
      fieldsSize[1] = lock.getSize();
      fieldsSize[2] = (type == null ? 0 : type.getSize());
      byte[][] fieldsBuf = new byte[FIELD_COUNT][];
      fieldsBuf[0] = capacity.toByteArray();
      fieldsBuf[1] = lock.toByteArray();
      fieldsBuf[2] = (type == null ? new byte[] {} : type.toByteArray());
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
      CellOutput t = new CellOutput();
      t.buf = buf;
      t.capacity = capacity;
      t.lock = lock;
      t.type = type;
      return t;
    }
  }
}
