package indexer;

public class CkbIndexerFactory {

  private static final String NODE_URL = "http://127.0.0.1:8116";

  private static CkbIndexerApi API = new DefaultIndexerApi(NODE_URL, false);

  public static CkbIndexerApi getApi() {
    return API;
  }
}
