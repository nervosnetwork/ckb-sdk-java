package indexer;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import indexer.model.Script;
import indexer.model.ScriptType;
import indexer.model.SearchKeyBuilder;
import indexer.model.resp.CellCapacityResponse;
import mercury.DefaultMercuryApi;
import mercury.MercuryApi;

/** @author zjh @Created Date: 2021/7/25 @Description: @Modify by: */
public class CkbIndexerTest {

  @Test
  void testCkbIndexer() {
    MercuryApi api = new DefaultMercuryApi("http://127.0.0.1:8116", false);
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "type",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51"));
    key.scriptType(ScriptType.lock);

    System.out.println(new Gson().toJson(key.build()));

    try {
      CellCapacityResponse capacity = api.getCellsCapacity(key.build());
      System.out.println(new Gson().toJson(capacity));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
