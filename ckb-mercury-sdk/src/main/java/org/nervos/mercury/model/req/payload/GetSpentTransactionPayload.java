package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;
import org.nervos.mercury.model.common.ViewType;

public class GetSpentTransactionPayload {
  public OutPoint outpoint;

  @SerializedName("structure_type")
  public ViewType structureType;
}
