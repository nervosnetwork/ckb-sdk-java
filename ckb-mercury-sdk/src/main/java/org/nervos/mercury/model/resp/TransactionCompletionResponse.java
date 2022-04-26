package org.nervos.mercury.model.resp;

import org.nervos.ckb.type.Transaction;
import org.nervos.mercury.model.resp.signature.SignatureAction;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TransactionCompletionResponse {
  public Transaction txView;
  public List<SignatureAction> signatureActions;

  public List<MercuryScriptGroup> getScriptGroup() {
    return this.signatureActions
        .stream()
        .map(x -> new MercuryScriptGroup(x, this.txView))
        .collect(toList());
  }
}
