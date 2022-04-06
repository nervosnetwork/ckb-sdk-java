package indexer;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.TransactionResponse;

public class TransactionTest {

  @Test
  void testTransaction() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            Numeric.hexStringToByteArray(
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
            Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);

    TransactionResponse txs =
        CkbIndexerFactory.getApi().getTransactions(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(txs.objects.size() > 0);
  }
}
