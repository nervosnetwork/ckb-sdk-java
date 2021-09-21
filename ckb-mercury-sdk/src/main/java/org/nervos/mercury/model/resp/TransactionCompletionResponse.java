package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.transaction.Transaction;

public class TransactionCompletionResponse {

  @SerializedName("tx_view")
  public Transaction txView;

  @SerializedName("signature_entries")
  public List<SignatureEntry> signatureEntries;

  public List<MercuryScriptGroup> getScriptGroup() {
    this.signaturePlaceholder();
    List<MercuryScriptGroup> scriptGroups = new ArrayList<>(this.signatureEntries.size());

    for (int i = 0; i < this.signatureEntries.size(); i++) {
      MercuryScriptGroup sg =
          new MercuryScriptGroup(
              this.signatureEntries.get(i).pubKey,
              this.getInputIndexes(
                  this.signatureEntries.get(i).index, this.signatureEntries.get(i).groupLen));
      scriptGroups.add(sg);
    }

    return scriptGroups;
  }

  private void signaturePlaceholder() {
    for (int i = 0; i < this.txView.inputs.size(); i++) {
      this.txView.witnesses.add(
          this.is_sig_entry(i) ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
    }
  }

  private List<Integer> getInputIndexes(int index, int len) {
    List<Integer> integers = new ArrayList<>();

    integers.add(index);
    int cnt = 1;

    for (int i = index + 1; i < this.txView.inputs.size(); i++) {
      if (cnt == len) {
        break;
      }

      if (!this.is_sig_entry(i)) {
        integers.add(i);
        cnt += 1;
      }
    }
    return integers;
  }

  private boolean is_sig_entry(int index) {
    for (int i = 0; i < this.signatureEntries.size(); i++) {
      if (this.signatureEntries.get(i).index == index) {
        return true;
      }
    }
    return false;
  }
}
