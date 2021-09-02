package mercury.constant;

import org.nervos.mercury.DefaultMercuryApi;
import org.nervos.mercury.MercuryApi;

public class MercuryApiFactory {

  private static String MERCURY_URL = "http://127.0.0.1:8116";

  private static MercuryApi API = new DefaultMercuryApi(MERCURY_URL, false);

  public static MercuryApi getApi() {
    return API;
  }
}
