package indexer;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.indexer.model.resp.TipResponse;

public class TipTest {
  @Test
  void getTip() throws IOException {
    TipResponse tip = CkbIndexerFactory.getApi().getTip();
    Assertions.assertNotNull(tip.blockHash);
    Assertions.assertNotEquals(0, tip.blockNumber);
  }
}
