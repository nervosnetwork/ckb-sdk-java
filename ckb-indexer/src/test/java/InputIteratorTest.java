import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.InputIterator;
import org.nervos.indexer.model.Order;

import java.util.Iterator;

class InputIteratorTest {
  @Test
  public void testIterate() {
    DefaultIndexerApi api = new DefaultIndexerApi("https://testnet.ckb.dev/indexer", false);
    Iterator<TransactionInput> iterator = new InputIterator(api, Order.ASC, 4)
        .addSearchKey("ckt1qyqgrfqrklscqeutp3tlqhlcd8xrculgufqspwdp7m");

    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    Assertions.assertEquals(10, count);

    iterator = new InputIterator(api, Order.ASC, 4)
        .addSearchKey("ckt1qyqgrfqrklscqeutp3tlqhlcd8xrculgufqspwdp7m")
        .addSearchKey("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2lck0mlvl2t25sgt6z504plwqdmh46elsqrps6v");

    count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    Assertions.assertEquals(26, count);
  }
}