package indexer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.indexer.model.resp.TipResponse;

import java.io.IOException;

public class TipTest {
  @Test
  void getTip() throws IOException {
    TipResponse tip = CkbIndexerFactory.getApi().getTip();
    Assertions.assertNotNull(tip.blockHash);
    Assertions.assertNotEquals(0, tip.blockNumber);
  }
}
