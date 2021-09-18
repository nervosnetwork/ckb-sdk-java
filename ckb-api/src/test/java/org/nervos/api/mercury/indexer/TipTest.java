package org.nervos.api.mercury.indexer;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.api.constant.ApiFactory;
import org.nervos.indexer.resp.TipResponse;

public class TipTest {
  @Test
  void getTip() {
    try {
      TipResponse tip = ApiFactory.getApi().getTip();
      System.out.println(tip.blockHash);
      System.out.println(tip.blockNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
