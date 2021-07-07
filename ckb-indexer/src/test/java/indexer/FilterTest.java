package indexer;

import com.google.gson.Gson;
import indexer.model.Script;
import indexer.model.ScriptType;
import indexer.model.SearchKeyBuilder;
import indexer.model.resp.CellsResp;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;

public class FilterTest {

  @Test
  void testFilterScript() {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "type",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51"));
    key.scriptType(ScriptType.lock);

    key.filterScript(
        new Script(
            "0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4",
            "type",
            "0x7c7f0ee1d582c385342367792946cff3767fe02f26fd7f07dba23ae3c65b28bc"));

    System.out.println(new Gson().toJson(key.build()));

    try {
      CellsResp cells =
          CkbIndexerFactory.getApi()
              .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);
      System.out.println(cells.objects.size());
      System.out.println(new Gson().toJson(cells));
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
            "type",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51"));
    key.scriptType(ScriptType.lock);

    key.filterOutputCapacityRange(
        "0x" + new BigInteger("0").toString(16),
        "0x" + new BigInteger("1000000000000000000").toString(16));

    System.out.println(new Gson().toJson(key.build()));

    try {
      CellsResp cells =
          CkbIndexerFactory.getApi()
              .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);
      System.out.println(cells.objects.size());
      System.out.println(new Gson().toJson(cells));
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
            "type",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51"));
    key.scriptType(ScriptType.lock);

    key.filterOutputDataLenRange(
        "0x" + new BigInteger("0").toString(16), "0x" + new BigInteger("32").toString(16));

    System.out.println(new Gson().toJson(key.build()));

    try {
      CellsResp cells =
          CkbIndexerFactory.getApi()
              .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);
      System.out.println(cells.objects.size());
      System.out.println(new Gson().toJson(cells));
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
            "type",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51"));
    key.scriptType(ScriptType.lock);

    key.filterBlockRange(
        "0x" + new BigInteger("2003365").toString(16),
        "0x" + new BigInteger("2103365").toString(16));

    System.out.println(new Gson().toJson(key.build()));

    try {
      CellsResp cells =
          CkbIndexerFactory.getApi()
              .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);
      System.out.println(cells.objects.size());
      System.out.println(new Gson().toJson(cells));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
