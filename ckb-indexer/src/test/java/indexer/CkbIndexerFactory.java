package indexer;

import org.nervos.ckb.Network;
import org.nervos.indexer.CkbIndexerApi;
import org.nervos.indexer.Configuration;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.IndexerType;

import java.util.HashMap;

public class CkbIndexerFactory {
  HashMap<String, CkbIndexerApi> apiDict = new HashMap<>();
  private static final class InstanceHolder {
    static final CkbIndexerFactory instance = new CkbIndexerFactory();
  }

  public static CkbIndexerApi getApi() {
    String url = Configuration.getInstance().getUrl(Network.TESTNET);
    return InstanceHolder.instance.apiDict.computeIfAbsent(url, (key) -> new DefaultIndexerApi(key, false));
  }
}
