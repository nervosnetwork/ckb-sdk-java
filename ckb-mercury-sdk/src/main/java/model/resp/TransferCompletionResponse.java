package model.resp;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransferCompletionResponse {

  @SerializedName("tx_view")
  public Transaction txView;

  @SerializedName("sigs_entry")
  public List<SignatureEntry> sigsEntry;

  public List<MercuryScriptGroup> getScriptGroup() {
    this.signaturePlaceholder();
    List<MercuryScriptGroup> scriptGroups = new ArrayList<>(this.sigsEntry.size());

    if (this.sigsEntry.size() == 1) {
      MercuryScriptGroup sg =
          new MercuryScriptGroup(
              this.sigsEntry.get(0).pubKey,
              regionToList(this.sigsEntry.get(0).index, this.txView.inputs.size()));
      scriptGroups.add(sg);
      return scriptGroups;
    }

    for (int i = 0; i < this.sigsEntry.size(); i++) {
      if (i == this.sigsEntry.size() - 1) {
        int start = this.sigsEntry.get(i).index;
        int end = this.txView.inputs.size();
        MercuryScriptGroup sg =
            new MercuryScriptGroup(this.sigsEntry.get(i).pubKey, regionToList(start, end));
        scriptGroups.add(sg);
        continue;
      }

      int start = this.sigsEntry.get(i).index;
      int end = this.sigsEntry.get(i + 1).index;
      MercuryScriptGroup sg =
          new MercuryScriptGroup(this.sigsEntry.get(i).pubKey, regionToList(start, end));
      scriptGroups.add(sg);
    }

    return scriptGroups;
  }

  private void signaturePlaceholder() {

    if (this.sigsEntry.size() == 1) {
      for (int i = 0; i < this.txView.inputs.size(); i++) {
        this.txView.witnesses.add(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
      }
      return;
    }

    for (int i = 0; i < this.sigsEntry.size(); i++) {
      if (i == this.sigsEntry.size() - 1) {
        int start = this.sigsEntry.get(i).index;
        int end = this.txView.inputs.size();
        for (int j = start; j < end; j++) {
          this.txView.witnesses.add(j == start ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
        }
        continue;
      }

      int start = this.sigsEntry.get(i).index;
      int end = this.sigsEntry.get(i + 1).index;
      for (int j = start; j < end; j++) {
        this.txView.witnesses.add(j == start ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
      }
    }
  }

  private List<Integer> regionToList(int start, int end) {
    List<Integer> integers = new ArrayList<>();
    for (int i = start; i < end; i++) {
      integers.add(i);
    }
    return integers;
  }
}
