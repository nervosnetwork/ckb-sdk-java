package org.nervos.mercury.model.resp;

import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.resp.signature.SignatureAction;

public class MercuryScriptGroup {

  public SignatureAction action;

  public Transaction transaction;

  private String originalwitness;

  public MercuryScriptGroup(SignatureAction action, Transaction transaction) {
    this.action = action;
    this.transaction = transaction;
    this.originalwitness =
        this.transaction.witnesses.get(this.action.signatureLocation.index).toString();
  }

  public Integer getOffset() {
    return this.action.signatureLocation.offset;
  }

  public String getWitness() {
    return originalwitness;
  }

  public Integer getWitnessIndex() {
    return this.action.signatureLocation.index;
  }

  public String getAddress() {
    return this.action.signatureInfo.address;
  }

  public List<String> getGroupWitnesses() {
    List<String> groupWitnesses = new ArrayList<>(this.action.otherIndexesInGroup.size() + 1);
    groupWitnesses.add(originalwitness);
    for (Integer index : this.action.otherIndexesInGroup) {
      groupWitnesses.add(this.transaction.witnesses.get(index).toString());
    }
    return groupWitnesses;
  }
}
