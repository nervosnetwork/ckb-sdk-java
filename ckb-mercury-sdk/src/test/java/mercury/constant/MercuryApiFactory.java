package mercury.constant;

import mercury.DefaultMercuryApi;
import mercury.MercuryApi;

public class MercuryApiFactory {

  private static String MERCURY_URL = "http://127.0.0.1:8116";

  private static MercuryApi API = new DefaultMercuryApi(MERCURY_URL, false);

  public static MercuryApi getApi() {
    return API;
  }
}
