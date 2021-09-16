package org.nervos.api.constant;

import org.nervos.api.CkbApi;
import org.nervos.api.DefaultCkbApi;

public class ApiFactory {

  private static String MERCURY_URL = "http://127.0.0.1:8116";

  private static CkbApi API = new DefaultCkbApi(MERCURY_URL, false);

  public static CkbApi getApi() {
    return API;
  }
}
