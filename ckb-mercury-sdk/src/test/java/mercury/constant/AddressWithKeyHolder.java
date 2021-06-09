package mercury.constant;

import java.util.HashMap;
import java.util.Map;

public class AddressWithKeyHolder {

  private static Map<String, String> addressWithKey = new HashMap<>(4);

  private static String TEST_ADDRESS1 = "ckt1qyqzse99vquwj6t32xyt6s7p25ymjlslam7s583h63";

  private static String TEST_ADDRESS2 = "ckt1qyqd3ygn34kjgkh59tlzygdke3nulp6856msqnkzet";

  private static String TEST_ADDRESS3 = "ckt1qyq0g3352qz3wvy64t6j4t5xph7p8gdcv7xsnn2sve";

  private static String TEST_ADDRESS4 = "ckt1qyqqyfm4m7ag092xmg2mu7l72zcq8xw9fa2sfw7eve";

  static {
    addressWithKey.put(
        TEST_ADDRESS1, "5997dcc69ae4949508adfd40e179e8d35209f33e47be9f162c023e7fb0a12c26");
    addressWithKey.put(
        TEST_ADDRESS2, "3c04883b003824c965d6779141b3b8cc5681e7f205a453fa36ad2d6f698518a1");
    addressWithKey.put(
        TEST_ADDRESS3, "9e8ba28b74c7ceeb4dbad33869e18dde393c4e563540bc80dbdd9fc854e9e0af");
    addressWithKey.put(
        TEST_ADDRESS4, "78728b7530989527a0a3a296317025654f2f0ca5e65b53139828ee78db23be40");
  }

  public static String getKey(String address) {
    return addressWithKey.get(address);
  }

  public static String testAddress1() {
    return TEST_ADDRESS1;
  }

  public static String testAddress2() {
    return TEST_ADDRESS2;
  }

  public static String testAddress3() {
    return TEST_ADDRESS3;
  }

  public static String testAddress4() {
    return TEST_ADDRESS4;
  }
}
