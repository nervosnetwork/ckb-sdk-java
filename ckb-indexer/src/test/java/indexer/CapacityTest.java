package indexer;

import com.google.gson.Gson;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.model.ScriptType;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellCapacityResponse;

public class CapacityTest {

  @Test
  void testCapacity() {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
                Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
            Numeric.hexStringToByteArray("0x0c24d18f16e3c43272695e5db006a22cb9ddde51"),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.lock);

    System.out.println(new Gson().toJson(key.build()));

    try {
      CellCapacityResponse capacity = CkbIndexerFactory.getApi().getCellsCapacity(key.build());
      System.out.println(new Gson().toJson(capacity));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
