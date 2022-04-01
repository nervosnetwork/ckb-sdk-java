package org.nervos.ckb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.indexer.*;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class CkbIndexerApiTest {
  CkbIndexerApi service = new CkbIndexerApi("https://testnet.ckb.dev/indexer");

  @Test
  public void testGetCells() throws IOException {
    SearchKey searchKey = newSearchKey();
    CkbIndexerCells cells = service.getCells(searchKey, Order.ASC, 100, null);
    Assertions.assertEquals(4, cells.objects.size());

    cells = service.getCells(searchKey, Order.ASC, 1, null);
    Assertions.assertEquals(1, cells.objects.size());
    byte[] lastCursor = cells.lastCursor;
    cells = service.getCells(searchKey, Order.ASC, 100, lastCursor);
    Assertions.assertEquals(3, cells.objects.size());
  }

  @Test
  public void testGetCellCapacity() throws IOException {
    SearchKey searchKey = newSearchKey();
    CkbIndexerCellsCapacity cellsCapacity = service.getCellsCapacity(searchKey);
    Assertions.assertTrue(cellsCapacity.capacity.compareTo(BigInteger.ZERO) > 0);
  }

  private SearchKey newSearchKey() {
    Script script = new Script();
    script.codeHash = Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8");
    script.args = Numeric.hexStringToByteArray("0x64257f00b6b63e987609fa9be2d0c86d351020fb");
    script.hashType = Script.HashType.TYPE;
    SearchKey searchKey = new SearchKey(script);
    searchKey.script = script;
    searchKey.scriptType = ScriptType.LOCK;
    return searchKey;
  }
}