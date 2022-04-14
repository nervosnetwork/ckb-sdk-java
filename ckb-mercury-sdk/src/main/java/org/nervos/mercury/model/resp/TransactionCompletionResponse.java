package org.nervos.mercury.model.resp;

import static java.util.stream.Collectors.toList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.resp.signature.SignatureAction;

public class TransactionCompletionResponse {

  @SerializedName("tx_view")
  public Transaction txView;

  @SerializedName("signature_actions")
  public List<SignatureAction> signatureActions;

  public List<MercuryScriptGroup> getScriptGroup() {
    return this.signatureActions
        .stream()
        .map(x -> new MercuryScriptGroup(x, this.txView))
        .collect(toList());
  }
}
