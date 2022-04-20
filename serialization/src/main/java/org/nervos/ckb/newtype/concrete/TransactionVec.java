package org.nervos.ckb.newtype.concrete;

import org.nervos.ckb.newtype.base.DynamicVector;
import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public final class TransactionVec extends DynamicVector {
  public static Class ITEM_TYPE = Transaction.class;

  private Transaction[] items;

  private TransactionVec() {
  }

  @Nonnull
  public Transaction get(int i) {
    return items[i];
  }

  @Nullable
  public Transaction[] getItems() {
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
    private Transaction[] items;

    private Builder() {
      items = new Transaction[0];
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, TransactionVec.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      items = new Transaction[offsets.length - 1];
      for (int i = 0; i < items.length; i++) {
        byte[] itemBuf = Arrays.copyOfRange(buf, offsets[i], offsets[i + 1]);
        items[i] = Transaction.builder(itemBuf).build();
      }
    }

    public Builder add(@Nonnull Transaction item) {
      Objects.requireNonNull(item);
      Transaction[] originalItems = items;
      items = new Transaction[originalItems.length + 1];
      System.arraycopy(originalItems, 0, items, 0, originalItems.length);
      items[items.length - 1] = item;
      ;
      return this;
    }

    public Builder add(@Nonnull Transaction[] items) {
      Objects.requireNonNull(items);
      Transaction[] originalItems = this.items;
      this.items = new Transaction[originalItems.length + items.length];
      System.arraycopy(originalItems, 0, this.items, 0, originalItems.length);
      System.arraycopy(items, 0, this.items, originalItems.length, items.length);
      return this;
    }

    public Builder set(int i, @Nonnull Transaction item) {
      Objects.requireNonNull(item);
      items[i] = item;
      return this;
    }

    public Builder set(@Nonnull Transaction[] items) {
      Objects.requireNonNull(items);
      this.items = items;
      return this;
    }

    public Builder remove(int i) {
      if (i < 0 || i >= items.length) {
        throw new ArrayIndexOutOfBoundsException(i);
      }
      Transaction[] originalItems = items;
      items = new Transaction[originalItems.length - 1];
      System.arraycopy(originalItems, 0, items, 0, i);
      System.arraycopy(originalItems, i + 1, items, i, originalItems.length - i - 1);
      return this;
    }

    public TransactionVec build() {
      int size = 4 + 4 * items.length;
      for (int i = 0; i < items.length; i++) {
        size += items[i].getSize();
      }
      byte[] buf = new byte[size];
      MoleculeUtils.setInt(size, buf, 0);
      ;
      int offset = 4 + 4 * items.length;
      int start = 4;
      for (int i = 0; i < items.length; i++) {
        MoleculeUtils.setInt(offset, buf, start);
        ;
        offset += items[i].getSize();
        start += 4;
      }
      for (int i = 0; i < items.length; i++) {
        MoleculeUtils.setBytes(items[i].toByteArray(), buf, start);
        ;
        start += items[i].getSize();
      }
      TransactionVec v = new TransactionVec();
      v.buf = buf;
      v.items = items;
      return v;
    }
  }
}
