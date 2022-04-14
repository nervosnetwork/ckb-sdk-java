package org.nervos.ckb.newtype.concrete;

import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.nervos.ckb.newtype.base.MoleculeException;
import org.nervos.ckb.newtype.base.MoleculeUtils;
import org.nervos.ckb.newtype.base.Table;

public final class RawTransaction extends Table {
  public static int FIELD_COUNT = 6;

  private Uint32 version;

  private CellDepVec cellDeps;

  private Byte32Vec headerDeps;

  private CellInputVec inputs;

  private CellOutputVec outputs;

  private BytesVec outputsData;

  private RawTransaction() {}

  @Nonnull
  public Uint32 getVersion() {
    return version;
  }

  @Nonnull
  public CellDepVec getCellDeps() {
    return cellDeps;
  }

  @Nonnull
  public Byte32Vec getHeaderDeps() {
    return headerDeps;
  }

  @Nonnull
  public CellInputVec getInputs() {
    return inputs;
  }

  @Nonnull
  public CellOutputVec getOutputs() {
    return outputs;
  }

  @Nonnull
  public BytesVec getOutputsData() {
    return outputsData;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(@Nonnull byte[] buf) {
    return new Builder(buf);
  }

  public static final class Builder {
    private Uint32 version;

    private CellDepVec cellDeps;

    private Byte32Vec headerDeps;

    private CellInputVec inputs;

    private CellOutputVec outputs;

    private BytesVec outputsData;

    private Builder() {
      version = Uint32.builder().build();
      cellDeps = CellDepVec.builder().build();
      headerDeps = Byte32Vec.builder().build();
      inputs = CellInputVec.builder().build();
      outputs = CellOutputVec.builder().build();
      outputsData = BytesVec.builder().build();
    }

    private Builder(@Nonnull byte[] buf) {
      Objects.requireNonNull(buf);
      int size = MoleculeUtils.littleEndianBytes4ToInt(buf, 0);
      if (buf.length != size) {
        throw MoleculeException.invalidByteSize(size, buf.length, RawTransaction.class);
      }
      int[] offsets = MoleculeUtils.getOffsets(buf);
      if (offsets.length - 1 != FIELD_COUNT) {
        throw MoleculeException.invalidFieldCount(
            FIELD_COUNT, offsets.length - 1, RawTransaction.class);
      }
      byte[] itemBuf;
      itemBuf = Arrays.copyOfRange(buf, offsets[0], offsets[1]);
      version = Uint32.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[1], offsets[2]);
      cellDeps = CellDepVec.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[2], offsets[3]);
      headerDeps = Byte32Vec.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[3], offsets[4]);
      inputs = CellInputVec.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[4], offsets[5]);
      outputs = CellOutputVec.builder(itemBuf).build();
      itemBuf = Arrays.copyOfRange(buf, offsets[5], offsets[6]);
      outputsData = BytesVec.builder(itemBuf).build();
    }

    public Builder setVersion(@Nonnull Uint32 version) {
      Objects.requireNonNull(version);
      this.version = version;
      return this;
    }

    public Builder setCellDeps(@Nonnull CellDepVec cellDeps) {
      Objects.requireNonNull(cellDeps);
      this.cellDeps = cellDeps;
      return this;
    }

    public Builder setHeaderDeps(@Nonnull Byte32Vec headerDeps) {
      Objects.requireNonNull(headerDeps);
      this.headerDeps = headerDeps;
      return this;
    }

    public Builder setInputs(@Nonnull CellInputVec inputs) {
      Objects.requireNonNull(inputs);
      this.inputs = inputs;
      return this;
    }

    public Builder setOutputs(@Nonnull CellOutputVec outputs) {
      Objects.requireNonNull(outputs);
      this.outputs = outputs;
      return this;
    }

    public Builder setOutputsData(@Nonnull BytesVec outputsData) {
      Objects.requireNonNull(outputsData);
      this.outputsData = outputsData;
      return this;
    }

    public RawTransaction build() {
      int[] offsets = new int[FIELD_COUNT];
      offsets[0] = 4 + 4 * FIELD_COUNT;
      offsets[1] = offsets[0] + version.getSize();
      offsets[2] = offsets[1] + cellDeps.getSize();
      offsets[3] = offsets[2] + headerDeps.getSize();
      offsets[4] = offsets[3] + inputs.getSize();
      offsets[5] = offsets[4] + outputs.getSize();
      int[] fieldsSize = new int[FIELD_COUNT];
      fieldsSize[0] = version.getSize();
      fieldsSize[1] = cellDeps.getSize();
      fieldsSize[2] = headerDeps.getSize();
      fieldsSize[3] = inputs.getSize();
      fieldsSize[4] = outputs.getSize();
      fieldsSize[5] = outputsData.getSize();
      byte[][] fieldsBuf = new byte[FIELD_COUNT][];
      fieldsBuf[0] = version.toByteArray();
      fieldsBuf[1] = cellDeps.toByteArray();
      fieldsBuf[2] = headerDeps.toByteArray();
      fieldsBuf[3] = inputs.toByteArray();
      fieldsBuf[4] = outputs.toByteArray();
      fieldsBuf[5] = outputsData.toByteArray();
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
      RawTransaction t = new RawTransaction();
      t.buf = buf;
      t.version = version;
      t.cellDeps = cellDeps;
      t.headerDeps = headerDeps;
      t.inputs = inputs;
      t.outputs = outputs;
      t.outputsData = outputsData;
      return t;
    }
  }
}
