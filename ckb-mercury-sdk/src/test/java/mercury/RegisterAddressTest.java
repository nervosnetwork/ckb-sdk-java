package mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.address.AddressTools;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RegisterAddressTest {

  @Test
  void testRegisterAddress() throws IOException {
    List<byte[]> scriptHashes =
        ApiFactory.getApi().registerAddresses(
            Arrays.asList(
                AddressTools.generateAcpAddress(AddressWithKeyHolder.testAddress0()).encode()));

    Assertions.assertTrue(scriptHashes.size() >= 1);
  }
}
