package indexer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nervos.indexer.Configuration;
import org.nervos.indexer.IndexerType;
import org.nervos.indexer.model.resp.TipResponse;

import java.io.IOException;

public class TipTest {
  @Test
  void getTip() throws IOException {
    Configuration.getInstance().setIndexerUrl(null);
    TipResponse tip = CkbIndexerFactory.getApi().getTip();
    Assertions.assertNotNull(tip.blockHash);
    Assertions.assertNotEquals(0, tip.blockNumber);

    Configuration.getInstance().setIndexType(IndexerType.CkbModule);
    TipResponse tip2 = CkbIndexerFactory.getApi().getTip();
    Assertions.assertNotNull(tip2.blockHash);
    Assertions.assertNotEquals(0, tip2.blockNumber);

    Assertions.assertTrue(tip.blockNumber <= tip2.blockNumber);
  }

  @Test
  void getTipStandAlone() throws IOException {
    Configuration.getInstance().setIndexType(IndexerType.StandAlone);
    Configuration.getInstance().setIndexerUrl(null);
    TipResponse tip = CkbIndexerFactory.getApi().getTip();
    Assertions.assertNotNull(tip.blockHash);
    Assertions.assertNotEquals(0, tip.blockNumber);
  }

  @Disabled
  @Test
  void getCkbModuleLocal() throws IOException {
    Configuration.getInstance().setIndexType(IndexerType.CkbModule);
    Configuration.getInstance().setIndexerUrl("http://127.0.0.1:8114");
    TipResponse tip = CkbIndexerFactory.getApi().getTip();
    Assertions.assertNotNull(tip.blockHash);
    Assertions.assertNotEquals(0, tip.blockNumber);
  }

  @Disabled
  @Test
  void getStandAloneLocal() throws IOException {
    Configuration.getInstance().setIndexType(IndexerType.StandAlone);
    Configuration.getInstance().setIndexerUrl("http://127.0.0.1:8116");
    TipResponse tip = CkbIndexerFactory.getApi().getTip();
    Assertions.assertNotNull(tip.blockHash);
    Assertions.assertNotEquals(0, tip.blockNumber);
  }

  @AfterAll
  public static void cleanUp() {
    Configuration.getInstance().setIndexType(IndexerType.CkbModule);
    Configuration.getInstance().setIndexerUrl(null);
  }
}
