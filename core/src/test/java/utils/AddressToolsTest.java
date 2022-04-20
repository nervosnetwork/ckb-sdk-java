package utils;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressTools;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

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
  void testConvertToBech32mFullAddress() {
    String shortAddress = "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g";
    String address = AddressTools.convertToBech32mFullAddress(shortAddress);
    Assertions.assertEquals(
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtyqlpwlx7ed68pftzv69wcvr5nxxqzzus2zxwa6",
        address);

    String bech32Address =
        "ckt1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwseq8cthehktw3c22cnx3tkrqaye3sqshy0qkcmf";
    address = AddressTools.convertToBech32mFullAddress(bech32Address);
    Assertions.assertEquals(
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtyqlpwlx7ed68pftzv69wcvr5nxxqzzus2zxwa6",
        address);
  }

  @Test
  void testConvertToShortAddress() {
    String bech32mAddress =
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtyqlpwlx7ed68pftzv69wcvr5nxxqzzus2zxwa6";
    String address = AddressTools.convertToShortAddress(bech32mAddress);
    Assertions.assertEquals("ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g", address);

    String bech32Address =
        "ckt1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwseq8cthehktw3c22cnx3tkrqaye3sqshy0qkcmf";
    address = AddressTools.convertToShortAddress(bech32Address);
    Assertions.assertEquals("ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g", address);
  }

  @Test
  void testConvertToBech32FullAddress() {
    String shortAddress = "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g";
    String address = AddressTools.convertToBech32FullAddress(shortAddress);
    Assertions.assertEquals(
        "ckt1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwseq8cthehktw3c22cnx3tkrqaye3sqshy0qkcmf",
        address);

    String bech32mAddress =
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtyqlpwlx7ed68pftzv69wcvr5nxxqzzus2zxwa6";
    address = AddressTools.convertToBech32FullAddress(bech32mAddress);
    Assertions.assertEquals(
        "ckt1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwseq8cthehktw3c22cnx3tkrqaye3sqshy0qkcmf",
        address);
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
    String address =
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf";

    String actual = AddressTools.generateAcpAddress(address);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testGenerateChequeAddress() {
    String expected =
        "ckt1qpsdtuu7lnjqn3v8ew02xkwwlh4dv5x2z28shkwt8p2nfruccux4kq2je6sm0zczgrepc8y547zvuu6zpshfvvjh7h2ln2w035d2lnh32ylk5ydmjq5ypwq24ftzt";
    String senderAddress =
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqd0pdquvfuq077aemn447shf4d8u5f4a0glzz2g4";
    String receiverAddress =
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf";

    String actual = AddressTools.generateChequeAddress(senderAddress, receiverAddress);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testParseAddress() {
    String address =
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf";
    AddressParseResult script = AddressTools.parse(address);
    System.out.println(new Gson().toJson(script));
  }

  @Test
  void testParseNetwork() {
    String address =
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf";
    Assertions.assertEquals(Network.TESTNET, AddressTools.parseNetwork(address));
  }
}
