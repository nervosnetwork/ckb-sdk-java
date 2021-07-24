package mercury.constant;

import mercury.DefaultMercuryApi;
import mercury.MercuryApi;

public class MercuryApiFactory {

  private static String MERCURY_URL = "http://47.56.233.149:8226";

  private static MercuryApi API = new DefaultMercuryApi(MERCURY_URL, false);

  public static MercuryApi getApi() {
    return API;
  }
}
