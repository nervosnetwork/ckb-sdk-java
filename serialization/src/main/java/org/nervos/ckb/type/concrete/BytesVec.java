package org.nervos.ckb.type.concrete;

import org.nervos.ckb.type.base.DynamicVector;
import org.nervos.ckb.type.base.MoleculeException;
import org.nervos.ckb.type.base.MoleculeUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public final class BytesVec extends DynamicVector {
  public static Class ITEM_TYPE = Bytes.class;

  private Bytes[] items;

  private BytesVec() {
  }

  @Nonnull
  public Bytes get(int i) {
    return items[i];
  }

  @Nullable
  public Bytes[] getItems() {
    return items;
  }

  @Override
  public int getItemCount() {
    return items.length;
  }

  @Override
  public Class getItemType() {
    return ITEM_TYPE;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private Bytes[] items;

    private Builder() {
      items = new Bytes[0];
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, BytesVec.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      items = new Bytes[offsets.length - 1];
      for (int i = 0; i < items.length; i++) {
        byte[] itemBuf = Arrays.copyOfRange(buf, offsets[i], offsets[i + 1]);
        items[i] = Bytes.builder(itemBuf).build();
      }
    }

    public Builder add(@Nonnull Bytes item) {
      Objects.requireNonNull(item);
      Bytes[] originalItems = items;
      items = new Bytes[originalItems.length + 1];
      System.arraycopy(originalItems, 0, items, 0, originalItems.length);
      items[items.length - 1] = item;
      return this;
    }

    public Builder add(@Nonnull Bytes[] items) {
      Objects.requireNonNull(items);
      Bytes[] originalItems = this.items;
      this.items = new Bytes[originalItems.length + items.length];
      System.arraycopy(originalItems, 0, this.items, 0, originalItems.length);
      System.arraycopy(items, 0, this.items, originalItems.length, items.length);
      return this;
    }

    public Builder set(int i, @Nonnull Bytes item) {
      Objects.requireNonNull(item);
      items[i] = item;
      return this;
    }

    public Builder set(@Nonnull Bytes[] items) {
      Objects.requireNonNull(items);
      this.items = items;
      return this;
    }

    public Builder remove(int i) {
      if (i < 0 || i >= items.length) {
        throw new ArrayIndexOutOfBoundsException(i);
      }
      Bytes[] originalItems = items;
      items = new Bytes[originalItems.length - 1];
      System.arraycopy(originalItems, 0, items, 0, i);
      System.arraycopy(originalItems, i + 1, items, i, originalItems.length - i - 1);
      return this;
    }

    public BytesVec build() {
      int size = 4 + 4 * items.length;
      for (int i = 0; i < items.length; i++) {
        size += items[i].getSize();
      }
      byte[] buf = new byte[size];
      MoleculeUtils.setInt(size, buf, 0);
      int offset = 4 + 4 * items.length;
      int start = 4;
      for (int i = 0; i < items.length; i++) {
        MoleculeUtils.setInt(offset, buf, start);
        offset += items[i].getSize();
        start += 4;
      }
      for (int i = 0; i < items.length; i++) {
        MoleculeUtils.setBytes(items[i].toByteArray(), buf, start);
        start += items[i].getSize();
      }
      BytesVec v = new BytesVec();
      v.buf = buf;
      v.items = items;
      return v;
    }
  }
}
