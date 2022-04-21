package org.nervos.ckb.type.concrete;

import org.nervos.ckb.type.base.MoleculeException;
import org.nervos.ckb.type.base.MoleculeUtils;
import org.nervos.ckb.type.base.Table;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class Script extends Table {
  public static int FIELD_COUNT = 3;

  private Byte32 codeHash;

  private byte hashType;

  private Bytes args;

  private Script() {
  }

  @Nonnull
  public Byte32 getCodeHash() {
    return codeHash;
  }

  @Nonnull
  public byte getHashType() {
    return hashType;
  }

  @Nonnull
  public Bytes getArgs() {
    return args;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private Byte32 codeHash;

    private byte hashType;

    private Bytes args;

    private Builder() {
      codeHash = Byte32.builder().build();
      args = Bytes.builder().build();
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, Script.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      if (offsets.length - 1 != FIELD_COUNT) {
        throw MoleculeException.invalidFieldCount(FIELD_COUNT, offsets.length - 1, Script.class);
      }
      byte[] itemBuf;
      itemBuf = Arrays.copyOfRange(buf, offsets[0], offsets[1]);
      codeHash = Byte32.builder(itemBuf).build();
      hashType = buf[offsets[1]];
      itemBuf = Arrays.copyOfRange(buf, offsets[2], offsets[3]);
      args = Bytes.builder(itemBuf).build();
    }

    public Builder setCodeHash(@Nonnull Byte32 codeHash) {
      Objects.requireNonNull(codeHash);
      this.codeHash = codeHash;
      return this;
    }

    public Builder setHashType(@Nonnull byte hashType) {
      Objects.requireNonNull(hashType);
      this.hashType = hashType;
      return this;
    }

    public Builder setArgs(@Nonnull Bytes args) {
      Objects.requireNonNull(args);
      this.args = args;
      return this;
    }

    public Script build() {
      int[] offsets = new int[FIELD_COUNT];
      offsets[0] = 4 + 4 * FIELD_COUNT;
      offsets[1] = offsets[0] + codeHash.getSize();
      offsets[2] = offsets[1] + 1;
      int[] fieldsSize = new int[FIELD_COUNT];
      fieldsSize[0] = codeHash.getSize();
      fieldsSize[1] = 1;
      fieldsSize[2] = args.getSize();
      byte[][] fieldsBuf = new byte[FIELD_COUNT][];
      fieldsBuf[0] = codeHash.toByteArray();
      fieldsBuf[1] = new byte[]{hashType};
      fieldsBuf[2] = args.toByteArray();
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
      Script t = new Script();
      t.buf = buf;
      t.codeHash = codeHash;
      t.hashType = hashType;
      t.args = args;
      return t;
    }
  }
}
