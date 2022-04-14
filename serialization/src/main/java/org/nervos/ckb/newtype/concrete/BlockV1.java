package org.nervos.ckb.newtype.concrete;

import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;
import org.nervos.ckb.newtype.base.Table;

public final class BlockV1 extends Table {
  public static int FIELD_COUNT = 5;

  private Header header;

  private UncleBlockVec uncles;

  private TransactionVec transactions;

  private ProposalShortIdVec proposals;

  private Bytes extension;

  private BlockV1() {}

  @Nonnull
  public Header getHeader() {
    return header;
  }

  @Nonnull
  public UncleBlockVec getUncles() {
    return uncles;
  }

  @Nonnull
  public TransactionVec getTransactions() {
    return transactions;
  }

  @Nonnull
  public ProposalShortIdVec getProposals() {
    return proposals;
  }

  @Nonnull
  public Bytes getExtension() {
    return extension;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private Header header;

    private UncleBlockVec uncles;

    private TransactionVec transactions;

    private ProposalShortIdVec proposals;

    private Bytes extension;

    private Builder() {
      header = Header.builder().build();
      uncles = UncleBlockVec.builder().build();
      transactions = TransactionVec.builder().build();
      proposals = ProposalShortIdVec.builder().build();
      extension = Bytes.builder().build();
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, BlockV1.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      if (offsets.length - 1 != FIELD_COUNT) {
        throw MoleculeException.invalidFieldCount(FIELD_COUNT, offsets.length - 1, BlockV1.class);
      }
      byte[] itemBuf;
      itemBuf = Arrays.copyOfRange(buf, offsets[0], offsets[1]);
      header = Header.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[1], offsets[2]);
      uncles = UncleBlockVec.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[2], offsets[3]);
      transactions = TransactionVec.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[3], offsets[4]);
      proposals = ProposalShortIdVec.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[4], offsets[5]);
      extension = Bytes.builder(itemBuf).build();
    }

    public Builder setHeader(@Nonnull Header header) {
      Objects.requireNonNull(header);
      this.header = header;
      return this;
    }

    public Builder setUncles(@Nonnull UncleBlockVec uncles) {
      Objects.requireNonNull(uncles);
      this.uncles = uncles;
      return this;
    }

    public Builder setTransactions(@Nonnull TransactionVec transactions) {
      Objects.requireNonNull(transactions);
      this.transactions = transactions;
      return this;
    }

    public Builder setProposals(@Nonnull ProposalShortIdVec proposals) {
      Objects.requireNonNull(proposals);
      this.proposals = proposals;
      return this;
    }

    public Builder setExtension(@Nonnull Bytes extension) {
      Objects.requireNonNull(extension);
      this.extension = extension;
      return this;
    }

    public BlockV1 build() {
      int[] offsets = new int[FIELD_COUNT];
      offsets[0] = 4 + 4 * FIELD_COUNT;
      offsets[1] = offsets[0] + header.getSize();
      offsets[2] = offsets[1] + uncles.getSize();
      offsets[3] = offsets[2] + transactions.getSize();
      offsets[4] = offsets[3] + proposals.getSize();
      int[] fieldsSize = new int[FIELD_COUNT];
      fieldsSize[0] = header.getSize();
      fieldsSize[1] = uncles.getSize();
      fieldsSize[2] = transactions.getSize();
      fieldsSize[3] = proposals.getSize();
      fieldsSize[4] = extension.getSize();
      byte[][] fieldsBuf = new byte[FIELD_COUNT][];
      fieldsBuf[0] = header.toByteArray();
      fieldsBuf[1] = uncles.toByteArray();
      fieldsBuf[2] = transactions.toByteArray();
      fieldsBuf[3] = proposals.toByteArray();
      fieldsBuf[4] = extension.toByteArray();
      int size = 4 + 4 * FIELD_COUNT;
      for (int i = 0; i < FIELD_COUNT; i++) {
        size += fieldsSize[i];
      }
      byte[] buf = new byte[size];
      ;
      MoleculeUtils.setInt(size, buf, 0);
      int start = 4;
      for (int i = 0; i < FIELD_COUNT; i++) {
        MoleculeUtils.setInt(offsets[i], buf, start);
        start += 4;
      }
      for (int i = 0; i < FIELD_COUNT; i++) {
        MoleculeUtils.setBytes(fieldsBuf[i], buf, offsets[i]);
      }
      BlockV1 t = new BlockV1();
      t.buf = buf;
      t.header = header;
      t.uncles = uncles;
      t.transactions = transactions;
      t.proposals = proposals;
      t.extension = extension;
      return t;
    }
  }
}
