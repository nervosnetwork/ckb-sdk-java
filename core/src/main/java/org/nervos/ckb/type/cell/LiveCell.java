package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.transaction.TransactionPoint;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class LiveCell {

  @SerializedName("created_by")
  public TransactionPoint createdBy;

  public CellOutput cellOutput;
}
