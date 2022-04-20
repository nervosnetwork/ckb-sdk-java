package org.nervos.ckb.newtype.concrete;

import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;
import org.nervos.ckb.newtype.base.Struct;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public final class Header extends Struct {
  public static int SIZE = 208;

  public static int FIELD_COUNT = 2;

  private RawHeader raw;

  private Uint128 nonce;

  private Header() {
  }

  @Nonnull
  public RawHeader getRaw() {
    return raw;
  }

  @Nonnull
  public Uint128 getNonce() {
    return nonce;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private RawHeader raw;

    private Uint128 nonce;

    private Builder() {
      raw = RawHeader.builder().build();
      nonce = Uint128.builder().build();
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      if (buf.length != SIZE) {
        throw MoleculeException.invalidByteSize(SIZE, buf.length, Header.class);
      }
      byte[] itemBuf;
      itemBuf = Arrays.copyOfRange(buf, 0, 192);
      raw = RawHeader.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, 192, 208);
      nonce = Uint128.builder(itemBuf).build();
    }

    public Builder setRaw(@Nonnull RawHeader raw) {
      Objects.requireNonNull(raw);
      this.raw = raw;
      return this;
    }

    public Builder setNonce(@Nonnull Uint128 nonce) {
      Objects.requireNonNull(nonce);
      this.nonce = nonce;
      return this;
    }

    public Header build() {
      int[] offsets = new int[FIELD_COUNT];
      offsets[0] = 0;
      offsets[1] = offsets[0] + RawHeader.SIZE;
      byte[] buf = new byte[SIZE];
      MoleculeUtils.setBytes(raw.toByteArray(), buf, offsets[0]);
      MoleculeUtils.setBytes(nonce.toByteArray(), buf, offsets[1]);
      Header s = new Header();
      s.buf = buf;
      s.raw = raw;
      s.nonce = nonce;
      return s;
    }
  }
}
