import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import mercury.constant.MercuryApiFactory;
import org.junit.jupiter.api.Test;

/** @author zjh @Created Date: 2021/7/24 @Description: @Modify by: */
public class RegisterAddressesTest {

  @Test
  void testRegisterAddresses() {
    try {
      List<String> scriptHashes =
          MercuryApiFactory.getApi()
              .registerAddresses(
                  Arrays.asList(
                      "ckt1q3sdtuu7lnjqn3v8ew02xkwwlh4dv5x2z28shkwt8p2nfruccux4kz2t6nrqr8v3yqhnpak7yu3zd6uvync5av9sqv4zdx47egrwel8c2jmk0g6xhr3awhf84cp"));
      System.out.println(scriptHashes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
