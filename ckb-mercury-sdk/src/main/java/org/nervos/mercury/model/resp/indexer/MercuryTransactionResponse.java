package org.nervos.mercury.model.resp.indexer;

import static java.util.stream.Collectors.toList;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.nervos.indexer.model.resp.TransactionInfoResponse;
import org.nervos.indexer.model.resp.TransactionResponse;

public class MercuryTransactionResponse {

  @SerializedName("last_cursor")
  public List<Integer> cursor;

  public List<TransactionInfoResponse> objects;

  public TransactionResponse toTransactionResponse() {
    TransactionResponse transactionInfoResponse = new TransactionResponse();
    transactionInfoResponse.objects = this.objects;
    transactionInfoResponse.lastCursor =
        this.cursor == null
            ? null
            : String.join(",", this.cursor.stream().map(x -> x.toString()).collect(toList()));

    return transactionInfoResponse;
  }
}
