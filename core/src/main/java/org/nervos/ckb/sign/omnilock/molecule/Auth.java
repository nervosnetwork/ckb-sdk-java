package org.nervos.ckb.sign.omnilock.molecule;

import org.nervos.ckb.type.base.Array;
import org.nervos.ckb.type.base.MoleculeException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class Auth extends Array {
    public static Class ITEM_TYPE = byte.class;

    public static int ITEM_SIZE = 1;

    public static int ITEM_COUNT = 21;

    public static int SIZE = ITEM_SIZE * ITEM_COUNT;

    private byte[] items;

    private Auth() {
    }

    @Nonnull
    public byte get(int i) {
        return items[i];
    }

    @Nullable
    public byte[] getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    @Override
    public int getItemSize() {
        return ITEM_SIZE;
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
            items = new byte[ITEM_COUNT];
        }

        private Builder(@Nonnull byte[] buf) {
            Objects.requireNonNull(buf);
            if (buf.length != SIZE) {
                throw MoleculeException.invalidByteSize(SIZE, buf.length, Auth.class);
            }
            items = buf;
        }

        public Builder set(int i, @Nonnull byte item) {
            Objects.requireNonNull(item);
            items[i] = item;
            return this;
        }

        public Builder set(@Nonnull byte[] items) {
            Objects.requireNonNull(items);
            if (items.length != ITEM_COUNT) {
                throw MoleculeException.invalidItemCount(ITEM_COUNT, items.length, Auth.class);
            }
            this.items = items;
            return this;
        }

        public Auth build() {
            Auth a = new Auth();
            a.buf = items;
            a.items = items;
            return a;
        }
    }
}
