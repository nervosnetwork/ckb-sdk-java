package indexer;

public class CkbIndexerFactory {

  private static final String NODE_URL = "http://8.210.169.63:8116";

  private static CkbIndexerApi API = new DefaultIndexerApi(NODE_URL, false);

  public static CkbIndexerApi getApi() {
    return API;
  }
}
