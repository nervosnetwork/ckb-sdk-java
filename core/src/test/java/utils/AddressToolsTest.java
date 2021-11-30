package utils;

import com.google.gson.Gson;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressTools;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class AddressToolsTest {

  @Test
  void testGenerateShortAddress() {

    try {
      System.out.println(new Gson().toJson(AddressTools.generateShortAddress(Network.TESTNET)));
      System.out.println(new Gson().toJson(AddressTools.generateShortAddress(Network.MAINNET)));
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGenerateFullAddress() {
    try {
      AddressTools.AddressGenerateResult address =
          AddressTools.generateFullAddress(Network.TESTNET);
      AddressTools.parse(address.address);
      address = AddressTools.generateFullAddress(Network.MAINNET);
      AddressTools.parse(address.address);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGenerateBech32FullAddress() {
    try {
      AddressTools.AddressGenerateResult address =
          AddressTools.generateBech32mFullAddress(Network.TESTNET);
      AddressTools.parse(address.address);
      address = AddressTools.generateBech32mFullAddress(Network.MAINNET);
      AddressTools.parse(address.address);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testConvertPublicKeyToAddress() {
    try {
      String shortAddress1 =
          AddressTools.convertPublicKeyToShortAddress(
              Network.TESTNET,
              "0x24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
      String shortAddress2 =
          AddressTools.convertPublicKeyToShortAddress(
              Network.TESTNET, "24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
      String fullAddress1 =
          AddressTools.convertPublicKeyToFullAddress(
              Network.TESTNET,
              "0x24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
      String fullAddress2 =
          AddressTools.convertPublicKeyToFullAddress(
              Network.TESTNET, "24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
      System.out.println(shortAddress1);
      System.out.println(fullAddress1);
      System.out.println(
          "comparation result - short address: "
              + (shortAddress1.equals(shortAddress2))
              + ", full address: "
              + (fullAddress1.equals(fullAddress2)));
      AddressTools.parse(shortAddress1);
      AddressTools.parse(fullAddress1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGenerateAcpAddress() {
    String expected = "ckt1qypqtg06h75ymw098r3w0l3u4xklsj04tnsqkm65q6";
    String address = "ckt1qyqqtg06h75ymw098r3w0l3u4xklsj04tnsqctqrmc";

    String actual = AddressTools.generateAcpAddress(address);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testGenerateChequeAddress() {
    String expected =
        "ckt1qpsdtuu7lnjqn3v8ew02xkwwlh4dv5x2z28shkwt8p2nfruccux4kq2je6sm0zczgrepc8y547zvuu6zpshfvvjh7h2ln2w035d2lnh32ylk5ydmjq5ypwq24ftzt";
    String senderAddress = "ckt1qyq27z6pccncqlaamnh8ttapwn260egnt67ss2cwvz";
    String receiverAddress = "ckt1qyqqtg06h75ymw098r3w0l3u4xklsj04tnsqctqrmc";

    String actual = AddressTools.generateChequeAddress(senderAddress, receiverAddress);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testParseAddress() {
    String address = "ckt1qyqqtg06h75ymw098r3w0l3u4xklsj04tnsqctqrmc";
    AddressParseResult script = AddressTools.parse(address);
    System.out.println(new Gson().toJson(script));
  }

  @Test
  void testParseNetwork() {
    String address = "ckt1qyqqtg06h75ymw098r3w0l3u4xklsj04tnsqctqrmc";
    Assertions.assertEquals(Network.TESTNET, AddressTools.parseNetwork(address));
  }
}
