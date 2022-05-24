package org.nervos.api.mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.utils.Numeric;
import org.nervos.mercury.model.GetSpentTransactionPayloadBuilder;

import java.io.IOException;

public class GetSpentTransactionTest {

  @Test
  public void testGetSpentTransactionView() throws IOException {
    GetSpentTransactionPayloadBuilder builder = new GetSpentTransactionPayloadBuilder();
    builder.setOutpoint(
        new OutPoint(
            Numeric.hexStringToByteArray(
                "0xb2e952a30656b68044e1d5eed69f1967347248967785449260e3942443cbeece"),
            1));

    ApiFactory.getApi().getSpentTransactionWithTransactionView(builder.build());
  }

  @Test
  public void testGetSpentTransactionInfo() throws IOException {
    GetSpentTransactionPayloadBuilder builder = new GetSpentTransactionPayloadBuilder();
    builder.setOutpoint(
        new OutPoint(
            Numeric.hexStringToByteArray(
                "0xb2e952a30656b68044e1d5eed69f1967347248967785449260e3942443cbeece"),
            1));

    ApiFactory.getApi().getSpentTransactionWithTransactionInfo(builder.build());
  }
}
