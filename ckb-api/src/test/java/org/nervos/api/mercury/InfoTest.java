package org.nervos.api.mercury;

import com.google.gson.Gson;
import constant.ApiFactory;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.GsonFactory;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;

public class InfoTest {
  Gson g = GsonFactory.newGson();

  @Test
  public void testDbInfo() {
    DBInfo dbInfo = null;
    try {
      dbInfo = ApiFactory.getApi().getDbInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(g.toJson(dbInfo));
  }

  @Test
  public void testMercuryInfo() {
    MercuryInfo mercuryInfo = null;
    try {
      mercuryInfo = ApiFactory.getApi().getMercuryInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(g.toJson(mercuryInfo));
  }
}
