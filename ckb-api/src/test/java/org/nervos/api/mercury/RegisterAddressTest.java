package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.address.AddressTools;

public class RegisterAddressTest {

  @Test
  void testRegisterAddress() {
    try {
      List<byte[]> scriptHashes =
          ApiFactory.getApi()
              .registerAddresses(
                  Arrays.asList(
                      AddressTools.generateAcpAddress(AddressWithKeyHolder.testAddress0())));
      System.out.println(scriptHashes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
