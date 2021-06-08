package mercury.constant;

import org.nervos.ckb.service.Api;

public class CkbHolder {

    private static final String NODE_URL = "http://8.210.169.63:8114";

    private static Api API = new Api(NODE_URL, false);;

    public static Api getApi() {
        return API;
    }

}
