package mercury.constant;

import org.nervos.ckb.service.Api;

public class CkbNodeFactory {

  private static final String NODE_URL = "http://127.0.0.1:8114";

  private static Api API = new Api(NODE_URL, false);

  public static Api getApi() {
    return API;
  }
}
