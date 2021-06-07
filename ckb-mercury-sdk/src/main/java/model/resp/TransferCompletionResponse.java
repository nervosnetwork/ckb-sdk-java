package model.resp;

import lombok.Data;
import org.nervos.ckb.type.transaction.Transaction;

import java.util.List;

@Data
public class TransferCompletionResponse {
    private Transaction tx_view;
    private List<SignatureEntry> sigs_entry;
}
