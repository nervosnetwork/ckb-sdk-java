package indexer;

import org.nervos.indexer.CkbIndexerApi;
import org.nervos.indexer.DefaultIndexerApi;

public class CkbIndexerFactory {

  private static final String NODE_URL = "https://mercury-testnet.ckbapp.dev/indexer";

  private static CkbIndexerApi API = new DefaultIndexerApi(NODE_URL, false);

  public static CkbIndexerApi getApi() {
    return API;
  }
}
