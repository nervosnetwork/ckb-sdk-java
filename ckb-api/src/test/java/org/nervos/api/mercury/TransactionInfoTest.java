package org.nervos.api.mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;

public class TransactionInfoTest {

  @Test
  void testGetTransactionInfo() throws IOException {
    ApiFactory.getApi().getTransactionInfo(
        Numeric.hexStringToByteArray(
            "0x4329e4c751c95384a51072d4cbc9911a101fd08fc32c687353d016bf38b8b22c"));
  }
}
