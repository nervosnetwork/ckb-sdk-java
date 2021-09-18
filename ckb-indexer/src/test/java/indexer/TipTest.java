package indexer;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.indexer.model.resp.TipResponse;

public class TipTest {
  @Test
  void getTip() {
    try {
      TipResponse tip = CkbIndexerFactory.getApi().getTip();
      System.out.println(tip.blockHash);
      System.out.println(tip.blockNumber);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
