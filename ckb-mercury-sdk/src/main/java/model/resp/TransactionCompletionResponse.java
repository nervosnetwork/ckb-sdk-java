package model.resp;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionCompletionResponse {

  @SerializedName("tx_view")
  public Transaction txView;

  @SerializedName("sigs_entry")
  public List<SignatureEntry> sigsEntry;

  public List<MercuryScriptGroup> getScriptGroup() {
    this.signaturePlaceholder();
    List<MercuryScriptGroup> scriptGroups = new ArrayList<>(this.sigsEntry.size());

    for (int i = 0; i < this.sigsEntry.size(); i++) {
      MercuryScriptGroup sg =
          new MercuryScriptGroup(this.sigsEntry.get(i).pubKey, this.getInputIndexes(this.sigsEntry.get(i).index, this.sigsEntry.get(i).groupLen));
      scriptGroups.add(sg);
    }

    return scriptGroups;
  }

  private void signaturePlaceholder() {
    for (int i = 0; i < this.txView.inputs.size(); i++) {
      this.txView.witnesses.add(this.is_sig_entry(i) ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
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
    for (int i = 0; i < this.sigsEntry.size(); i++) {
      if (this.sigsEntry.get(i).index == index) {
        return true;
      }
    }
    return false;
  }
}
