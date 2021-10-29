package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.resp.signature.SignatureAction;

public class TransactionCompletionResponse {

  @SerializedName("tx_view")
  public Transaction txView;

  @SerializedName("signature_actions")
  public List<SignatureAction> signatureActions;

  public List<MercuryScriptGroup> getScriptGroup() {
    List<MercuryScriptGroup> scriptGroups = new ArrayList<>(this.signatureActions.size());
    for (int i = 0; i < this.signatureActions.size(); i++) {
      SignatureAction signatureAction = this.signatureActions.get(i);
      scriptGroups.add(
          new MercuryScriptGroup(
              signatureAction.signatureInfo.address, this.getWitnessGroupIndexes(signatureAction)));

      this.txView.witnesses.set(
          signatureAction.signatureLocation.index, converWitness(signatureAction));
    }

    return scriptGroups;
  }

  @NotNull
  private Witness converWitness(SignatureAction signatureAction) {
    return new Witness(
        this.txView.witnesses.get(signatureAction.signatureLocation.index).toString());
  }

  @NotNull
  private List<Integer> getWitnessGroupIndexes(SignatureAction signatureAction) {
    List<Integer> witnessGroupIndexes =
        new ArrayList<>(signatureAction.otherIndexesInGroup.size() + 1);
    witnessGroupIndexes.add(signatureAction.signatureLocation.index);
    for (Integer index : signatureAction.otherIndexesInGroup) {
      witnessGroupIndexes.add(index);
    }
    return witnessGroupIndexes;
  }
}
