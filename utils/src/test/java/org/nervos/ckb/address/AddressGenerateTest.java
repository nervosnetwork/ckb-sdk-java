package org.nervos.ckb.address;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.jupiter.api.Test;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class AddressGenerateTest {

  @Test
  void addressGenerate() {
    AddressGenerate ag = new AddressGenerate();
    try {
      AddressGenerate.AddressGenerateResult result = ag.generateShortAddress(Network.TESTNET);
      System.out.println(result);
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    }
  }
}
