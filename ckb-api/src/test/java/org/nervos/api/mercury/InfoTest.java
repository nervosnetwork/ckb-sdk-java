package org.nervos.api.mercury;

import com.google.gson.Gson;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.api.constant.ApiFactory;
import org.nervos.mercury.model.resp.info.DBInfo;

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
}
