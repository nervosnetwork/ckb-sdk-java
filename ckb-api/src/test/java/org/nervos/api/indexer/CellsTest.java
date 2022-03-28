package org.nervos.api.indexer;

import com.google.gson.Gson;
import constant.ApiFactory;
import java.io.IOException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.model.ScriptType;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellsResponse;
import org.nervos.mercury.GsonFactory;

public class CellsTest {
  Gson g = GsonFactory.newGson();

  @Test
  void testCells() {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
                Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
                        Numeric.hexStringToByteArray("0x0c24d18f16e3c43272695e5db006a22cb9ddde51"),
                Script.HashType.TYPE));
    key.scriptType(ScriptType.lock);

    System.out.println(g.toJson(key.build()));

    try {
      CellsResponse cells =
          ApiFactory.getApi()
              .getCells(key.build(), "asc", "0x" + new BigInteger("2").toString(16), null);

      System.out.println(g.toJson(cells));

      if (cells.lastCursor != null) {
        CellsResponse cells2 =
            ApiFactory.getApi()
                .getCells(
                    key.build(), "asc", "0x" + new BigInteger("2").toString(16), cells.lastCursor);
        System.out.println(g.toJson(cells2));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
