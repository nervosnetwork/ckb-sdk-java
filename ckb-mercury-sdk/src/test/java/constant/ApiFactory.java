package constant;

import org.nervos.mercury.DefaultMercuryApi;
import org.nervos.mercury.MercuryApi;

public class ApiFactory {

  private static String MERCURY_URL = "https://mercury-testnet.ckbapp.dev/";

  private static MercuryApi API = new DefaultMercuryApi(MERCURY_URL, false);

  public static MercuryApi getApi() {
    return API;
  }
}
