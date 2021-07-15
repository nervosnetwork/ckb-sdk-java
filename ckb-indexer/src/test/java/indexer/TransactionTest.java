package indexer;

import com.google.gson.Gson;
import indexer.model.Script;
import indexer.model.ScriptType;
import indexer.model.SearchKeyBuilder;
import indexer.model.resp.TransactionResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.Numeric;

public class TransactionTest {

  @Test
  void testTransanction() {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "type",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51"));
    key.scriptType(ScriptType.lock);

    System.out.println(new Gson().toJson(key.build()));

    try {
      TransactionResponse txs =
          CkbIndexerFactory.getApi()
              .getTransactions(key.build(), "asc", Numeric.toHexString("10"), null);
      System.out.println(new Gson().toJson(txs));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
