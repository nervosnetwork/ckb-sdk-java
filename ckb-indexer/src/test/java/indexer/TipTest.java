package indexer;

import indexer.model.resp.TipResp;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TipTest {
  @Test
  void getTip() {
    try {
      TipResp tip = CkbIndexerFactory.getApi().getTip();
      System.out.println(tip.blockHash);
      System.out.println(tip.blockNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
