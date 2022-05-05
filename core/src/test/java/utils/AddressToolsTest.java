package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.address.Address;
import org.nervos.ckb.utils.address.AddressTools;

public class AddressToolsTest {
  @Test
  void testGenerateAcpAddress() {
    String expected = "ckt1qypqtg06h75ymw098r3w0l3u4xklsj04tnsqkm65q6";
    String address =
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf";

    String actual = AddressTools.generateAcpAddress(address).encodeShort();
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

    Address address = AddressTools.generateChequeAddress(senderAddress, receiverAddress);
    Assertions.assertEquals(expected, address.encode());
  }
}
