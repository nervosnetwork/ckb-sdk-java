package model.resp;

import lombok.Data;
import org.nervos.ckb.transaction.ScriptGroup;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.transaction.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class TransferCompletionResponse {
    private Transaction tx_view;
    private List<SignatureEntry> sigs_entry;


    public List<ScriptGroup> getScriptGroup() {
        this.signaturePlaceholder();
        List<ScriptGroup> scriptGroups = new ArrayList<>(this.sigs_entry.size());

        if(this.sigs_entry.size() == 1) {
            ScriptGroup sg = new ScriptGroup(regionToList(this.sigs_entry.get(0).getIndex(), this.tx_view.inputs.size()));
            scriptGroups.add(sg);
            return scriptGroups;
        }

        Iterator<SignatureEntry> iterator = this.sigs_entry.iterator();
        SignatureEntry start = iterator.next();
        while(iterator.hasNext()) {
            SignatureEntry current = iterator.next();
            ScriptGroup sg = new ScriptGroup(regionToList(start.getIndex(), current.getIndex()));
            scriptGroups.add(sg);
            start = current;
        }

        return scriptGroups;
    }



    private void signaturePlaceholder() {
        for (int i = 0; i < this.tx_view.inputs.size(); i++) {
            this.tx_view.witnesses.add(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
        }
    }

    private List<Integer> regionToList(int start, int length) {
        List<Integer> integers = new ArrayList<>();
        for (int i = start; i < (start + length); i++) {
            integers.add(i);
        }
        return integers;
    }
}
