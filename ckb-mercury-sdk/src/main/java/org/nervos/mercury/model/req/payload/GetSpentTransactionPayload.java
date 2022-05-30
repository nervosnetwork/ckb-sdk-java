package org.nervos.mercury.model.req.payload;

import org.nervos.ckb.type.OutPoint;
import org.nervos.mercury.model.common.StructureType;

public class GetSpentTransactionPayload {
  public OutPoint outpoint;
  public StructureType structureType;
}
