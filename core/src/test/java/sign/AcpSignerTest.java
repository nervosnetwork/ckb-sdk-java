package sign;

import org.junit.jupiter.api.Test;

public class AcpSignerTest {

  @Test
  void testPwOneGroup() {
    SignerChecker.signAndCheck("acp_one_input");
  }
}
