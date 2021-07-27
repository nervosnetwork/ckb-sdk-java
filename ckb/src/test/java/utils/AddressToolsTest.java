package utils;

import com.google.gson.Gson;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
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
  void testGenerateAcpAddress() {
    String expected = "ckt1qypqtg06h75ymw098r3w0l3u4xklsj04tnsqkm65q6";
    String address = "ckt1qyqqtg06h75ymw098r3w0l3u4xklsj04tnsqctqrmc";

    String actual = AddressTools.generateAcpAddress(address);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testGenerateChequeAddress() {
    String expected =
        "ckt1q3sdtuu7lnjqn3v8ew02xkwwlh4dv5x2z28shkwt8p2nfruccux4k5kw5xmckqjq7gwpe990sn88xssv96try4l46hu6nnudr2huau238a4prwus9pqts3uptms";
    String senderAddress = "ckt1qyq27z6pccncqlaamnh8ttapwn260egnt67ss2cwvz";
    String receiverAddress = "ckt1qyqqtg06h75ymw098r3w0l3u4xklsj04tnsqctqrmc";

    String actual = AddressTools.generateChequeAddress(senderAddress, receiverAddress);

    Assertions.assertEquals(expected, actual);
  }
}
