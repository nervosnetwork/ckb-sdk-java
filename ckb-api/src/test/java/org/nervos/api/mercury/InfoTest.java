package org.nervos.api.mercury;

import constant.ApiFactory;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;
import org.nervos.mercury.model.resp.info.MercurySyncState;

public class InfoTest {

  @Test
  public void testDbInfo() {
    DBInfo dbInfo = null;
    try {
      dbInfo = ApiFactory.getApi().getDbInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMercuryInfo() {
    MercuryInfo mercuryInfo = null;
    try {
      mercuryInfo = ApiFactory.getApi().getMercuryInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSyncState() {
    MercurySyncState mercurySyncState = null;
    try {
      mercurySyncState = ApiFactory.getApi().getSyncState();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
