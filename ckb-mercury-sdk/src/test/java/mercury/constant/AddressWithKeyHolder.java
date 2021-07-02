package mercury.constant;

import java.util.HashMap;
import java.util.Map;

public class AddressWithKeyHolder {

  private static Map<String, String> addressWithKey = new HashMap<>(4);

  //// 0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b
  private static String TEST_ADDRESS0 = "ckt1qyqwfgtn8pnuf9kfeuet5gp3s7zv4z84jw5s9lyhff";
  //// 0xaf0b41c627807fbddcee75afa174d5a7e5135ebd
  private static String TEST_ADDRESS1 = "ckt1qyqqcfx33utw83pjwf54uhdsq63zewwamegs77y99l";
  //// 0x05a1fabfa84db9e538e2e7fe3ca9adf849f55ce0
  private static String TEST_ADDRESS2 = "ckt1qyqx8ujyvsqqlnt7wt5q3jpmkcl5zsu9qk3qwyq55k";
  //// 0x202647fecc5b9d8cbdb4ae7167e40f5ab1e4baaf
  private static String TEST_ADDRESS3 = "ckt1qyq00z4yqwatpzu8mwwdql7hpyp7qx0uz96sdrsftn";
  //// 0x839f1806e85b40c13d3c73866045476cc9a8c214
  private static String TEST_ADDRESS4 = "ckt1qyqyg2676jw02yzzg2f6y4tuyu59j4kdtg4qrrn42q";

  static {
    addressWithKey.put(
        TEST_ADDRESS0, "756de9025aaa5aa5e57876f5a8f02f2723579c032a7bcdd6978ac9d68388d549");
    addressWithKey.put(
        TEST_ADDRESS1, "cc38e6f1b8a49b5d09c721b1e56867dbeac88aa8585317cfca2f7b1db4426606");
    addressWithKey.put(
        TEST_ADDRESS2, "ff78e4228e49244f3e74bb36b18bb476641abc057c8b3e841ff0f96d08a6fd39");
    addressWithKey.put(
        TEST_ADDRESS3, "a7e7507ccd7a6895f8d5fac5cf295b712b515fa53730e0cd79b5bc8c612cf16e");
    addressWithKey.put(
        TEST_ADDRESS4, "b274e3a1c8ece62367c7165ec9bca18112ae1386a67ccb95e7acd384af017cbf");
  }

  public static String getKey(String address) {
    return addressWithKey.get(address);
  }

  public static String testAddress0() {
    return TEST_ADDRESS0;
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
