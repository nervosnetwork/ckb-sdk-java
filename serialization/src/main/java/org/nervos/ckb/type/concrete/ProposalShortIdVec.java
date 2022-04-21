package org.nervos.ckb.type.concrete;

import org.nervos.ckb.type.base.FixedVector;
import org.nervos.ckb.type.base.MoleculeException;
import org.nervos.ckb.type.base.MoleculeUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public final class ProposalShortIdVec extends FixedVector {
  public static int ITEM_SIZE = ProposalShortId.SIZE;

  public static Class ITEM_TYPE = ProposalShortId.class;

  private ProposalShortId[] items;

  private ProposalShortIdVec() {
  }

  @Override
  public int getItemSize() {
    return ITEM_SIZE;
  }

  @Nonnull
  public ProposalShortId get(int i) {
    return items[i];
  }

  @Nullable
  public ProposalShortId[] getItems() {
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
    private ProposalShortId[] items;

    private Builder() {
      this.items = new ProposalShortId[0];
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int itemCount = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      int size = 4 + itemCount * ITEM_SIZE;
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, ProposalShortIdVec.class);
      }
      int start = 4;
      items = new ProposalShortId[itemCount];
      for (int i = 0; i < itemCount; i++) {
        byte[] itemBuf = Arrays.copyOfRange(buf, start, start + ITEM_SIZE);
        items[i] = ProposalShortId.builder(itemBuf).build();
        start += ITEM_SIZE;
      }
    }

    public Builder add(@Nonnull ProposalShortId item) {
      Objects.requireNonNull(item);
      ProposalShortId[] originalItems = items;
      items = new ProposalShortId[originalItems.length + 1];
      System.arraycopy(originalItems, 0, items, 0, originalItems.length);
      items[items.length - 1] = item;
      return this;
    }

    public Builder add(@Nonnull ProposalShortId[] items) {
      Objects.requireNonNull(items);
      ProposalShortId[] originalItems = this.items;
      this.items = new ProposalShortId[originalItems.length + items.length];
      System.arraycopy(originalItems, 0, this.items, 0, originalItems.length);
      System.arraycopy(items, 0, this.items, originalItems.length, items.length);
      return this;
    }

    public Builder set(int i, @Nonnull ProposalShortId item) {
      Objects.requireNonNull(item);
      items[i] = item;
      return this;
    }

    public Builder set(@Nonnull ProposalShortId[] items) {
      Objects.requireNonNull(items);
      this.items = items;
      return this;
    }

    public Builder remove(int i) {
      if (i < 0 || i >= items.length) {
        throw new ArrayIndexOutOfBoundsException(i);
      }
      ProposalShortId[] originalItems = items;
      items = new ProposalShortId[originalItems.length - 1];
      System.arraycopy(originalItems, 0, items, 0, i);
      System.arraycopy(originalItems, i + 1, items, i, originalItems.length - i - 1);
      return this;
    }

    public ProposalShortIdVec build() {
      byte[] buf = new byte[4 + items.length * ITEM_SIZE];
      MoleculeUtils.setInt(items.length, buf, 0);
      int start = 4;
      for (int i = 0; i < items.length; i++) {
        MoleculeUtils.setBytes(items[i].toByteArray(), buf, start);
        start += ITEM_SIZE;
      }
      ProposalShortIdVec v = new ProposalShortIdVec();
      v.buf = buf;
      v.items = items;
      return v;
    }
  }
}
