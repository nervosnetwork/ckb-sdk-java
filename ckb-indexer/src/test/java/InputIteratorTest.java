import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.InputIterator;
import org.nervos.indexer.model.Order;

import java.util.Arrays;
import java.util.Iterator;

class InputIteratorTest {
  @Test
  public void testIterate() {
    Iterator<TransactionInput> iterator = new InputIterator(
        new DefaultIndexerApi("https://testnet.ckb.dev/indexer", false),
        Arrays.asList("ckt1qyqgrfqrklscqeutp3tlqhlcd8xrculgufqspwdp7m"),
        Order.ASC,
        4,
        null);

    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    Assertions.assertEquals(10, count);

    iterator = new InputIterator(
        new DefaultIndexerApi("https://testnet.ckb.dev/indexer", false),
        Arrays.asList("ckt1qyqgrfqrklscqeutp3tlqhlcd8xrculgufqspwdp7m", "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2lck0mlvl2t25sgt6z504plwqdmh46elsqrps6v"),
        Order.ASC,
        4,
        null);

    count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    Assertions.assertEquals(26,  count);
  }

}