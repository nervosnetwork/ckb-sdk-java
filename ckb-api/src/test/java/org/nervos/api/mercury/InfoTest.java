package org.nervos.api.mercury;

import com.google.gson.Gson;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.api.constant.ApiFactory;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;

public class InfoTest {

  @Test
  public void testDbInfo() {
    DBInfo dbInfo = null;
    try {
      dbInfo = ApiFactory.getApi().getDbInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(new Gson().toJson(dbInfo));
  }

  @Test
  public void testMercuryInfo() {
    MercuryInfo mercuryInfo = null;
    try {
      mercuryInfo = ApiFactory.getApi().getMercuryInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(new Gson().toJson(mercuryInfo));
  }
}
