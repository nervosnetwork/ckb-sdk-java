package org.nervos.ckb.newtype.concrete;

import org.nervos.ckb.newtype.base.FixedVector;
import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class Bytes extends FixedVector {
    public static int ITEM_SIZE = 1;

    public static Class ITEM_TYPE = byte.class;

    private byte[] items;

    private Bytes() {
    }

    @Override
    public int getItemSize() {
        return ITEM_SIZE;
    }

    @Nonnull
    public byte get(int i) {
        return items[i];
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
        private byte[] items;

        private Builder() {
            this.items = new byte[0];
        }

        private Builder(@Nonnull byte[] buf) {
            Objects.requireNonNull(buf);
            int itemCount = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
            int size = 4 + itemCount * ITEM_SIZE;
            if (buf.length != size) {
                throw new MoleculeException(size, buf.length, Bytes.class);
            }
            items = Arrays.copyOfRange(buf, 4, buf.length);
        }

        public Builder add(@Nonnull byte item) {
            Objects.requireNonNull(item);
            byte[] originalItems = items;
            items = new byte[originalItems.length + 1];
            System.arraycopy(originalItems, 0, items, 0, originalItems.length);
            items[items.length - 1] = item;;
            return this;
        }

        public Builder add(@Nonnull byte[] items) {
            Objects.requireNonNull(items);
            byte[] originalItems = this.items;
            this.items = new byte[originalItems.length + items.length];
            System.arraycopy(originalItems, 0, this.items, 0, originalItems.length);
            System.arraycopy(items, 0, this.items, originalItems.length, items.length);
            return this;
        }

        public Builder set(int i, @Nonnull byte item) {
            Objects.requireNonNull(item);
            items[i] = item;
            return this;
        }

        public Builder remove(int i) {
            if (i < 0 || i >= items.length) {
                throw new ArrayIndexOutOfBoundsException(i);
            }
            byte[] originalItems = items;
            items = new byte[originalItems.length - 1];
            System.arraycopy(originalItems, 0, items, 0, i);
            System.arraycopy(originalItems, i + 1, items, i, originalItems.length - i -1);
            return this;
        }

        public Bytes build() {
            byte[] buf = new byte[4 + items.length * ITEM_SIZE];
            MoleculeUtils.setInt(items.length, buf, 0);;
            MoleculeUtils.setBytes(items, buf, 4);
            Bytes v = new Bytes();
            v.buf = buf;
            v.items = items;
            return v;
        }
    }
}
