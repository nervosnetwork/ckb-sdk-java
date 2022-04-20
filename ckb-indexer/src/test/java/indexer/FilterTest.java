package indexer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;

public class FilterTest {
  @Test
  void testFilterScript() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            Numeric.hexStringToByteArray(
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
            Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);
    key.filterScript(
        new Script(
            Numeric.hexStringToByteArray(
                "0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4"),
            Numeric.hexStringToByteArray(
                "0x13d640a864c7e84d60afd8ca9c6689d345a18f63e2e426c9623a2811776cf211"),
            Script.HashType.TYPE));

    CellsResponse cells = CkbIndexerFactory.getApi().getCells(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(cells.objects.size() > 0);
  }

  @Test
  void testFilterOutputCapacityRange() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            Numeric.hexStringToByteArray(
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
            Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);
    key.filterOutputCapacityRange(0, 1000000000000000000L);

    CellsResponse cells = CkbIndexerFactory.getApi().getCells(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(cells.objects.size() > 0);
  }

  @Test
  void testFilterOutputDataLenRange() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            Numeric.hexStringToByteArray(
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
            Numeric.hexStringToByteArray("0x0c24d18f16e3c43272695e5db006a22cb9ddde51"),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);
    key.filterOutputDataLenRange(0, 32);

    CellsResponse cells = CkbIndexerFactory.getApi().getCells(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(cells.objects.size() > 0);
  }

  @Test
  void testFilterBlockRange() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            Numeric.hexStringToByteArray(
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
            Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);
    key.filterBlockRange(2676900, 2677000);

    CellsResponse cells = CkbIndexerFactory.getApi().getCells(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(cells.objects.size() > 0);
  }
}
