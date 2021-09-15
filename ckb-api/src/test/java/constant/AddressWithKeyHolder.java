package constant;

import java.util.HashMap;
import java.util.Map;

public class AddressWithKeyHolder {

  private static Map<String, String> addressWithKey = new HashMap<>(8);

  private static Map<String, String> addressWithPubKey = new HashMap<>(8);

  //// 0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b
  private static String TEST_ADDRESS0 = "ckt1qyq28wze3cw48ek9az0g4jmtfs6d8td38u4s6hp2s0";
  private static String TEST_PUBKEY0 = "0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b";

  //// 0xaf0b41c627807fbddcee75afa174d5a7e5135ebd
  private static String TEST_ADDRESS1 = "ckt1qyq27z6pccncqlaamnh8ttapwn260egnt67ss2cwvz";
  private static String TEST_PUBKEY1 = "0xaf0b41c627807fbddcee75afa174d5a7e5135ebd";

  //// 0x05a1fabfa84db9e538e2e7fe3ca9adf849f55ce0
  private static String TEST_ADDRESS2 = "ckt1qyqqtg06h75ymw098r3w0l3u4xklsj04tnsqctqrmc";
  private static String TEST_PUBKEY2 = "0x05a1fabfa84db9e538e2e7fe3ca9adf849f55ce0";

  //// 0x202647fecc5b9d8cbdb4ae7167e40f5ab1e4baaf
  private static String TEST_ADDRESS3 = "ckt1qyqzqfj8lmx9h8vvhk62uut8us844v0yh2hsnqvvgc";
  private static String TEST_PUBKEY3 = "0x202647fecc5b9d8cbdb4ae7167e40f5ab1e4baaf";

  //// 0x839f1806e85b40c13d3c73866045476cc9a8c214
  private static String TEST_ADDRESS4 = "ckt1qyqg88ccqm59ksxp85788pnqg4rkejdgcg2qxcu2qf";
  private static String TEST_PUBKEY4 = "0x839f1806e85b40c13d3c73866045476cc9a8c214";

  private static String CEX_ADDRESS = "ckt1qyqz50qxup4cw2dvg0exg8y9m5e90dykuh6q72rj0h";
  private static String CEX_PUBKEY = "0x2a3c06e06b8729ac43f2641c85dd3257b496e5f4";

  private static String QUERY_TRANSACTION_ADDRESS =
      "ckt1qyqp5nlkxkvwgwhee4pryj4mwetl4pyut0psq2h92t";
  private static String QUERY_TRANSACTION_PUBKEY = "0x1a4ff63598e43af9cd42324abb7657fa849c5bc3";

  static {
    addressWithKey.put(
        TEST_ADDRESS0, "6fc935dad260867c749cf1ba6602d5f5ed7fb1131f1beb65be2d342e912eaafe");
    addressWithKey.put(
        TEST_ADDRESS1, "9d8ca87d75d150692211fa62b0d30de4d1ee6c530d5678b40b8cedacf0750d0f");
    addressWithKey.put(
        TEST_ADDRESS2, "88a09e06735d89452552e359a052315ab5130dc2e4d864ae3eed21d6505b2f67");
    addressWithKey.put(
        TEST_ADDRESS3, "2d4cf0546a1dc93092ad56f2e18fbe6e41ee477d9dec0575cf43b69740ce9f74");
    addressWithKey.put(
        TEST_ADDRESS4, "5e46fdbb6ffd86d232080dc71f24b60df2a119e0102ca45a7c165472de14c104");
    addressWithKey.put(
        CEX_ADDRESS, "b274e3a1c8ece62367c7165ec9bca18112ae1386a67ccb95e7acd384af017cbf");
    addressWithKey.put(
        QUERY_TRANSACTION_ADDRESS,
        "385b57e3fedf89e5b553a3274e7039f7be742040a5af98303de29aff61b05c2c");

    addressWithPubKey.put(TEST_ADDRESS0, TEST_PUBKEY0);
    addressWithPubKey.put(TEST_ADDRESS1, TEST_PUBKEY1);
    addressWithPubKey.put(TEST_ADDRESS2, TEST_PUBKEY2);
    addressWithPubKey.put(TEST_ADDRESS3, TEST_PUBKEY3);
    addressWithPubKey.put(TEST_ADDRESS4, TEST_PUBKEY4);
    addressWithPubKey.put(CEX_ADDRESS, CEX_PUBKEY);
    addressWithPubKey.put(QUERY_TRANSACTION_ADDRESS, QUERY_TRANSACTION_PUBKEY);
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

  public static String cexAddress() {
    return CEX_ADDRESS;
  }

  public static String queryTransactionAddress() {
    return QUERY_TRANSACTION_ADDRESS;
  }

  public static String testPubKey0() {
    return TEST_PUBKEY0;
  }

  public static String testPubKey1() {
    return TEST_PUBKEY1;
  }

  public static String testPubKey2() {
    return TEST_PUBKEY2;
  }

  public static String testPubKey3() {
    return TEST_PUBKEY3;
  }

  public static String testPubKey4() {
    return TEST_PUBKEY4;
  }

  public static String cexPubKey() {
    return CEX_PUBKEY;
  }

  public static String queryTransactionPubKey() {
    return QUERY_TRANSACTION_PUBKEY;
  }

  public static String getPubKeyByAddress(String addr) {
    return addressWithPubKey.get(addr);
  }

  public static String put(String addr, String priKey) {
    return addressWithKey.put(addr, priKey);
  }
}
