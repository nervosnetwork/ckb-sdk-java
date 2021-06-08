package model.resp;

import lombok.Data;

import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransferCompletionResponse {
    private Transaction tx_view;
    private List<SignatureEntry> sigs_entry;

    public List<MercuryScriptGroup> getScriptGroup() {
        this.signaturePlaceholder();
        List<MercuryScriptGroup> scriptGroups = new ArrayList<>(this.sigs_entry.size());

        if(this.sigs_entry.size() == 1) {
            MercuryScriptGroup sg = new MercuryScriptGroup(this.sigs_entry.get(0).getPub_key(),regionToList(this.sigs_entry.get(0).getIndex(), this.tx_view.inputs.size()));
            scriptGroups.add(sg);
            return scriptGroups;
        }

        for (int i = 0; i < this.sigs_entry.size(); i++) {
            if(i == this.sigs_entry.size() - 1) {
                int start = this.sigs_entry.get(i).getIndex();
                int end = this.tx_view.inputs.size();
                MercuryScriptGroup sg = new MercuryScriptGroup(this.sigs_entry.get(i).getPub_key(),regionToList(start, end));
                scriptGroups.add(sg);
                continue;
            }

            int start = this.sigs_entry.get(i).getIndex();
            int end = this.sigs_entry.get(i + 1).getIndex();
            MercuryScriptGroup sg = new MercuryScriptGroup(this.sigs_entry.get(i).getPub_key(),regionToList(start, end));
            scriptGroups.add(sg);
        }

        return scriptGroups;
    }



    private void signaturePlaceholder() {

        if(this.sigs_entry.size() == 1) {
            for (int i = 0; i < this.tx_view.inputs.size(); i++) {
                this.tx_view.witnesses.add(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
            }
            return;
        }

        for (int i = 0; i < this.sigs_entry.size(); i++) {
            if(i == this.sigs_entry.size() - 1) {
                int start = this.sigs_entry.get(i).getIndex();
                int end = this.tx_view.inputs.size();
                for (int j = start; j < end; j++) {
                    this.tx_view.witnesses.add(j == start ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
                }
                continue;
            }

            int start = this.sigs_entry.get(i).getIndex();
            int end = this.sigs_entry.get(i + 1).getIndex();
            for (int j = start; j < end; j++) {
                this.tx_view.witnesses.add(j == start ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
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
