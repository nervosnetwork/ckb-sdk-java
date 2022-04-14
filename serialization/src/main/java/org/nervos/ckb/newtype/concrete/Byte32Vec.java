package org.nervos.ckb.newtype.concrete;

import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.nervos.ckb.newtype.base.FixedVector;
import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;

public final class Byte32Vec extends FixedVector {
  public static int ITEM_SIZE = Byte32.SIZE;

  public static Class ITEM_TYPE = Byte32.class;

  private Byte32[] items;

  private Byte32Vec() {}

  @Override
  public int getItemSize() {
    return ITEM_SIZE;
  }

  @Nonnull
  public Byte32 get(int i) {
    return items[i];
  }

  @Nullable
  public Byte32[] getItems() {
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
    private Byte32[] items;

    private Builder() {
      this.items = new Byte32[0];
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int itemCount = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      int size = 4 + itemCount * ITEM_SIZE;
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, Byte32Vec.class);
      }
      int start = 4;
      items = new Byte32[itemCount];
      for (int i = 0; i < itemCount; i++) {
        byte[] itemBuf = Arrays.copyOfRange(buf, start, start + ITEM_SIZE);
        items[i] = Byte32.builder(itemBuf).build();
        start += ITEM_SIZE;
      }
    }

    public Builder add(@Nonnull Byte32 item) {
      Objects.requireNonNull(item);
      Byte32[] originalItems = items;
      items = new Byte32[originalItems.length + 1];
      System.arraycopy(originalItems, 0, items, 0, originalItems.length);
      items[items.length - 1] = item;
      ;
      return this;
    }

    public Builder add(@Nonnull Byte32[] items) {
      Objects.requireNonNull(items);
      Byte32[] originalItems = this.items;
      this.items = new Byte32[originalItems.length + items.length];
      System.arraycopy(originalItems, 0, this.items, 0, originalItems.length);
      System.arraycopy(items, 0, this.items, originalItems.length, items.length);
      return this;
    }

    public Builder set(int i, @Nonnull Byte32 item) {
      Objects.requireNonNull(item);
      items[i] = item;
      return this;
    }

    public Builder set(@Nonnull Byte32[] items) {
      Objects.requireNonNull(items);
      this.items = items;
      return this;
    }

    public Builder remove(int i) {
      if (i < 0 || i >= items.length) {
        throw new ArrayIndexOutOfBoundsException(i);
      }
      Byte32[] originalItems = items;
      items = new Byte32[originalItems.length - 1];
      System.arraycopy(originalItems, 0, items, 0, i);
      System.arraycopy(originalItems, i + 1, items, i, originalItems.length - i - 1);
      return this;
    }

    public Byte32Vec build() {
      byte[] buf = new byte[4 + items.length * ITEM_SIZE];
      MoleculeUtils.setInt(items.length, buf, 0);
      ;
      int start = 4;
      for (int i = 0; i < items.length; i++) {
        MoleculeUtils.setBytes(items[i].toByteArray(), buf, start);
        start += ITEM_SIZE;
      }
      Byte32Vec v = new Byte32Vec();
      v.buf = buf;
      v.items = items;
      return v;
    }
  }
}
