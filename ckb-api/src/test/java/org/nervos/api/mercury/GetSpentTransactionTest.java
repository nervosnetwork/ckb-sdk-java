package org.nervos.api.mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.utils.Numeric;
import org.nervos.mercury.model.GetSpentTransactionPayloadBuilder;
import org.nervos.mercury.model.resp.TransactionInfoResponse;
import org.nervos.mercury.model.resp.TransactionWithRichStatus;
import org.nervos.mercury.model.resp.TxView;

import java.io.IOException;

public class GetSpentTransactionTest {

  @Test
  public void testGetSpentTransactionView() {
    GetSpentTransactionPayloadBuilder builder = new GetSpentTransactionPayloadBuilder();
    builder.outpoint(
        new OutPoint(
            Numeric.hexStringToByteArray(
                "0xb2e952a30656b68044e1d5eed69f1967347248967785449260e3942443cbeece"),
            1));

    try {
      TxView<TransactionWithRichStatus> tx =
          ApiFactory.getApi().getSpentTransactionWithTransactionView(builder.build());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetSpentTransactionInfo() {
    GetSpentTransactionPayloadBuilder builder = new GetSpentTransactionPayloadBuilder();
    builder.outpoint(
        new OutPoint(
            Numeric.hexStringToByteArray(
                "0xb2e952a30656b68044e1d5eed69f1967347248967785449260e3942443cbeece"),
            1));

    try {
      TxView<TransactionInfoResponse> tx =
          ApiFactory.getApi().getSpentTransactionWithTransactionInfo(builder.build());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
