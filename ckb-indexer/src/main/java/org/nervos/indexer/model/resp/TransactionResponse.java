package org.nervos.indexer.model.resp;

import java.util.List;

public class TransactionResponse {
  public byte[] lastCursor;
  public List<TransactionInfoResponse> objects;
}
