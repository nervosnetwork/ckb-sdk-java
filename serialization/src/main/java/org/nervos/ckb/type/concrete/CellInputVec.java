package org.nervos.ckb.type.concrete;

import org.nervos.ckb.type.base.FixedVector;
import org.nervos.ckb.type.base.MoleculeException;
import org.nervos.ckb.type.base.MoleculeUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public final class CellInputVec extends FixedVector {
  public static int ITEM_SIZE = CellInput.SIZE;

  public static Class ITEM_TYPE = CellInput.class;

  private CellInput[] items;

  private CellInputVec() {
  }

  @Override
  public int getItemSize() {
    return ITEM_SIZE;
  }

  @Nonnull
  public CellInput get(int i) {
    return items[i];
  }

  @Nullable
  public CellInput[] getItems() {
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
    private CellInput[] items;

    private Builder() {
      this.items = new CellInput[0];
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int itemCount = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      int size = 4 + itemCount * ITEM_SIZE;
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, CellInputVec.class);
      }
      int start = 4;
      items = new CellInput[itemCount];
      for (int i = 0; i < itemCount; i++) {
        byte[] itemBuf = Arrays.copyOfRange(buf, start, start + ITEM_SIZE);
        items[i] = CellInput.builder(itemBuf).build();
        start += ITEM_SIZE;
      }
    }

    public Builder add(@Nonnull CellInput item) {
      Objects.requireNonNull(item);
      CellInput[] originalItems = items;
      items = new CellInput[originalItems.length + 1];
      System.arraycopy(originalItems, 0, items, 0, originalItems.length);
      items[items.length - 1] = item;
      return this;
    }

    public Builder add(@Nonnull CellInput[] items) {
      Objects.requireNonNull(items);
      CellInput[] originalItems = this.items;
      this.items = new CellInput[originalItems.length + items.length];
      System.arraycopy(originalItems, 0, this.items, 0, originalItems.length);
      System.arraycopy(items, 0, this.items, originalItems.length, items.length);
      return this;
    }

    public Builder set(int i, @Nonnull CellInput item) {
      Objects.requireNonNull(item);
      items[i] = item;
      return this;
    }

    public Builder set(@Nonnull CellInput[] items) {
      Objects.requireNonNull(items);
      this.items = items;
      return this;
    }

    public Builder remove(int i) {
      if (i < 0 || i >= items.length) {
        throw new ArrayIndexOutOfBoundsException(i);
      }
      CellInput[] originalItems = items;
      items = new CellInput[originalItems.length - 1];
      System.arraycopy(originalItems, 0, items, 0, i);
      System.arraycopy(originalItems, i + 1, items, i, originalItems.length - i - 1);
      return this;
    }

    public CellInputVec build() {
      byte[] buf = new byte[4 + items.length * ITEM_SIZE];
      MoleculeUtils.setInt(items.length, buf, 0);
      int start = 4;
      for (int i = 0; i < items.length; i++) {
        MoleculeUtils.setBytes(items[i].toByteArray(), buf, start);
        start += ITEM_SIZE;
      }
      CellInputVec v = new CellInputVec();
      v.buf = buf;
      v.items = items;
      return v;
    }
  }
}
