package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.transaction.TransactionPoint;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class LiveCell {

  public boolean cellbase;

  @SerializedName("output_data_len")
  public String outputDataLen;

  @SerializedName("created_by")
  public TransactionPoint createdBy;

  @SerializedName("cell_output")
  public CellOutput cellOutput;
}
