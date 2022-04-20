package org.nervos.ckb.newtype.concrete;

import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;
import org.nervos.ckb.newtype.base.Table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public final class WitnessArgs extends Table {
  public static int FIELD_COUNT = 3;

  private Bytes lock;

  private Bytes inputType;

  private Bytes outputType;

  private WitnessArgs() {
  }

  @Nullable
  public Bytes getLock() {
    return lock;
  }

  @Nullable
  public Bytes getInputType() {
    return inputType;
  }

  @Nullable
  public Bytes getOutputType() {
    return outputType;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private Bytes lock;

    private Bytes inputType;

    private Bytes outputType;

    private Builder() {
      lock = null;
      inputType = null;
      outputType = null;
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, WitnessArgs.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      if (offsets.length - 1 != FIELD_COUNT) {
        throw MoleculeException.invalidFieldCount(
            FIELD_COUNT, offsets.length - 1, WitnessArgs.class);
      }
      byte[] itemBuf;
      if (offsets[0] != offsets[1]) {
        itemBuf = Arrays.copyOfRange(buf, offsets[0], offsets[1]);
        lock = Bytes.builder(itemBuf).build();
      }
      if (offsets[1] != offsets[2]) {
        itemBuf = Arrays.copyOfRange(buf, offsets[1], offsets[2]);
        inputType = Bytes.builder(itemBuf).build();
      }
      if (offsets[2] != offsets[3]) {
        itemBuf = Arrays.copyOfRange(buf, offsets[2], offsets[3]);
        outputType = Bytes.builder(itemBuf).build();
      }
    }

    public Builder setLock(@Nullable Bytes lock) {
      this.lock = lock;
      return this;
    }

    public Builder setInputType(@Nullable Bytes inputType) {
      this.inputType = inputType;
      return this;
    }

    public Builder setOutputType(@Nullable Bytes outputType) {
      this.outputType = outputType;
      return this;
    }

    public WitnessArgs build() {
      int[] offsets = new int[FIELD_COUNT];
      offsets[0] = 4 + 4 * FIELD_COUNT;
      offsets[1] = offsets[0] + (lock == null ? 0 : lock.getSize());
      offsets[2] = offsets[1] + (inputType == null ? 0 : inputType.getSize());
      int[] fieldsSize = new int[FIELD_COUNT];
      fieldsSize[0] = (lock == null ? 0 : lock.getSize());
      fieldsSize[1] = (inputType == null ? 0 : inputType.getSize());
      fieldsSize[2] = (outputType == null ? 0 : outputType.getSize());
      byte[][] fieldsBuf = new byte[FIELD_COUNT][];
      fieldsBuf[0] = (lock == null ? new byte[]{} : lock.toByteArray());
      fieldsBuf[1] = (inputType == null ? new byte[]{} : inputType.toByteArray());
      fieldsBuf[2] = (outputType == null ? new byte[]{} : outputType.toByteArray());
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
      WitnessArgs t = new WitnessArgs();
      t.buf = buf;
      t.lock = lock;
      t.inputType = inputType;
      t.outputType = outputType;
      return t;
    }
  }
}
