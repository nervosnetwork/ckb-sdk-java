package org.nervos.api.mercury;

import com.google.gson.Gson;
import constant.ApiFactory;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.OutPoint;
import org.nervos.mercury.GsonFactory;
import org.nervos.mercury.model.GetSpentTransactionPayloadBuilder;
import org.nervos.mercury.model.resp.TransactionInfo;
import org.nervos.mercury.model.resp.TransactionView;

public class GetSpentTransactionTest {
  Gson g = GsonFactory.newGson();

  @Test
  public void testGetSpentTransactionView() {
    GetSpentTransactionPayloadBuilder builder = new GetSpentTransactionPayloadBuilder();
    builder.outpoint(
        new OutPoint("0xb2e952a30656b68044e1d5eed69f1967347248967785449260e3942443cbeece", "0x1"));

    System.out.println(g.toJson(builder.build()));

    try {
      TransactionView tx =
          ApiFactory.getApi().getSpentTransactionWithTransactionView(builder.build());
      System.out.println(g.toJson(tx));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetSpentTransactionInfo() {
    GetSpentTransactionPayloadBuilder builder = new GetSpentTransactionPayloadBuilder();
    builder.outpoint(
        new OutPoint("0xb2e952a30656b68044e1d5eed69f1967347248967785449260e3942443cbeece", "0x1"));

    System.out.println(g.toJson(builder.build()));

    try {
      TransactionInfo tx =
          ApiFactory.getApi().getSpentTransactionWithTransactionInfo(builder.build());
      System.out.println(g.toJson(tx));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
