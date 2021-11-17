package org.nervos.api.indexer;

import com.google.gson.Gson;
import constant.ApiFactory;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.indexer.model.ScriptType;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.mercury.GsonFactory;

public class CapacityTest {
  Gson g = GsonFactory.newGson();

  @Test
  void testCapacity() {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51",
            Script.TYPE));
    key.scriptType(ScriptType.lock);

    System.out.println(g.toJson(key.build()));

    try {
      CellCapacityResponse capacity = ApiFactory.getApi().getCellsCapacity(key.build());
      System.out.println(g.toJson(capacity));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
