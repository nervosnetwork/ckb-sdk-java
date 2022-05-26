package mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class InfoTest {

  @Test
  public void testDbInfo() throws IOException {
    ApiFactory.getApi().getDbInfo();
  }

  @Test
  public void testMercuryInfo() throws IOException {
    ApiFactory.getApi().getMercuryInfo();
  }

  @Test
  public void testSyncState() throws IOException {
    ApiFactory.getApi().getSyncState();
  }
}
