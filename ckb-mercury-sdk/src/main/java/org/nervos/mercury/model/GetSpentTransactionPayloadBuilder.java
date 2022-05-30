package org.nervos.mercury.model;

import org.nervos.ckb.type.OutPoint;
import org.nervos.mercury.model.common.StructureType;
import org.nervos.mercury.model.req.payload.GetSpentTransactionPayload;

public class GetSpentTransactionPayloadBuilder extends GetSpentTransactionPayload {

  public void outpoint(OutPoint outpoint) {
    this.outpoint = outpoint;
  }

  public void structureType(StructureType structureType) {
    this.structureType = structureType;
  }

  public GetSpentTransactionPayload build() {
    return this;
  }
}
