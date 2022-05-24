package org.nervos.mercury.model.resp;

import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.type.Transaction;

import java.util.List;

public class TransactionCompletionResponse {
  public Transaction txView;
  public List<ScriptGroup> scriptGroups;
}
