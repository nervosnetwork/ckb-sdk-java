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

public class FilterTest {
  Gson g = GsonFactory.newGson();

  @Test
  void testFilterScript() {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51",
            Script.TYPE));
    key.scriptType(ScriptType.lock);

    key.filterScript(
        new Script(
            "0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4",
            "0x7c7f0ee1d582c385342367792946cff3767fe02f26fd7f07dba23ae3c65b28bc",
            Script.TYPE));

    System.out.println(g.toJson(key.build()));

    try {
      CellsResponse cells =
          ApiFactory.getApi()
              .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);

      System.out.println(cells.objects.size());
      System.out.println(g.toJson(cells));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testFilterOutputCapacityRange() {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51",
            Script.TYPE));
    key.scriptType(ScriptType.lock);

    key.filterOutputCapacityRange(
        Numeric.toHexString("0"), Numeric.toHexString("1000000000000000000"));

    System.out.println(g.toJson(key.build()));

    try {

      CellsResponse cells =
          ApiFactory.getApi()
              .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);

      System.out.println(cells.objects.size());
      System.out.println(g.toJson(cells));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testFilterOutputDataLenRange() {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51",
            Script.TYPE));
    key.scriptType(ScriptType.lock);

    key.filterOutputDataLenRange(Numeric.toHexString("0"), Numeric.toHexString("32"));

    System.out.println(g.toJson(key.build()));

    try {
      CellsResponse cells =
          ApiFactory.getApi()
              .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);
      System.out.println(cells.objects.size());
      System.out.println(g.toJson(cells));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testFilterBlockRange() {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51",
            Script.TYPE));
    key.scriptType(ScriptType.lock);

    key.filterBlockRange(Numeric.toHexString("2003365"), Numeric.toHexString("2103365"));

    System.out.println(g.toJson(key.build()));

    try {

      CellsResponse cells =
          ApiFactory.getApi()
              .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);

      System.out.println(cells.objects.size());
      System.out.println(g.toJson(cells));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
