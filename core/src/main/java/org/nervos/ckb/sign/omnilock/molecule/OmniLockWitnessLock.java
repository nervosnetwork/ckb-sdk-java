package org.nervos.ckb.sign.omnilock.molecule;

import org.nervos.ckb.type.base.MoleculeException;
import org.nervos.ckb.type.base.MoleculeUtils;
import org.nervos.ckb.type.base.Table;
import org.nervos.ckb.type.concrete.Bytes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public final class OmniLockWitnessLock extends Table {
  public static int FIELD_COUNT = 3;

  private Bytes signature;

  private Identity omniIdentity;

  private Bytes preimage;

  private OmniLockWitnessLock() {
  }

  @Nullable
  public Bytes getSignature() {
    return signature;
  }

  @Nullable
  public Identity getOmniIdentity() {
    return omniIdentity;
  }

  @Nullable
  public Bytes getPreimage() {
    return preimage;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private Bytes signature;

    private Identity omniIdentity;

    private Bytes preimage;

    private Builder() {
      signature = null;
      omniIdentity = null;
      preimage = null;
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, OmniLockWitnessLock.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      if (offsets.length - 1 != FIELD_COUNT) {
        throw MoleculeException.invalidFieldCount(FIELD_COUNT, offsets.length - 1, OmniLockWitnessLock.class);
      }
      byte[] itemBuf;
      if (offsets[0] != offsets[1]) {
        itemBuf = Arrays.copyOfRange(buf, offsets[0], offsets[1]);
        signature = Bytes.builder(itemBuf).build();
      }
      if (offsets[1] != offsets[2]) {
        itemBuf = Arrays.copyOfRange(buf, offsets[1], offsets[2]);
        omniIdentity = Identity.builder(itemBuf).build();
      }
      if (offsets[2] != offsets[3]) {
        itemBuf = Arrays.copyOfRange(buf, offsets[2], offsets[3]);
        preimage = Bytes.builder(itemBuf).build();
      }
    }

    public Builder setSignature(@Nullable Bytes signature) {
      this.signature = signature;
      return this;
    }

    public Builder setOmniIdentity(@Nullable Identity omniIdentity) {
      this.omniIdentity = omniIdentity;
      return this;
    }

    public Builder setPreimage(@Nullable Bytes preimage) {
      this.preimage = preimage;
      return this;
    }

    public OmniLockWitnessLock build() {
      int[] offsets = new int[FIELD_COUNT];
      offsets[0] = 4 + 4 * FIELD_COUNT;
      offsets[1] = offsets[0] + (signature == null ? 0 : signature.getSize());
      offsets[2] = offsets[1] + (omniIdentity == null ? 0 : omniIdentity.getSize());
      int[] fieldsSize = new int[FIELD_COUNT];
      fieldsSize[0] = (signature == null ? 0 : signature.getSize());
      fieldsSize[1] = (omniIdentity == null ? 0 : omniIdentity.getSize());
      fieldsSize[2] = (preimage == null ? 0 : preimage.getSize());
      byte[][] fieldsBuf = new byte[FIELD_COUNT][];
      fieldsBuf[0] = (signature == null ? new byte[]{} : signature.toByteArray());
      fieldsBuf[1] = (omniIdentity == null ? new byte[]{} : omniIdentity.toByteArray());
      fieldsBuf[2] = (preimage == null ? new byte[]{} : preimage.toByteArray());
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
      OmniLockWitnessLock t = new OmniLockWitnessLock();
      t.buf = buf;
      t.signature = signature;
      t.omniIdentity = omniIdentity;
      t.preimage = preimage;
      return t;
    }
  }
}
