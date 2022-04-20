package org.nervos.mercury.model.req.payload;

import org.nervos.ckb.type.OutPoint;
import org.nervos.mercury.model.common.ViewType;

public class GetSpentTransactionPayload {
  public OutPoint outpoint;
  public ViewType structureType;
}
