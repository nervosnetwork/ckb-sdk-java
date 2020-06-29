package org.nervos.ckb.utils;

import static org.nervos.ckb.utils.Numeric.asByte;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.exceptions.MessageDecodingException;

public class NumericTest {

  private static final byte[] HEX_RANGE_ARRAY =
      new byte[] {
        asByte(0x0, 0x1),
        asByte(0x2, 0x3),
        asByte(0x4, 0x5),
        asByte(0x6, 0x7),
        asByte(0x8, 0x9),
        asByte(0xa, 0xb),
        asByte(0xc, 0xd),
        asByte(0xe, 0xf)
      };

  private static final String HEX_RANGE_STRING = "0x0123456789abcdef";

  @Test
  public void testQuantityDecode() {
    Assertions.assertEquals(Numeric.decodeQuantity("0x0"), BigInteger.valueOf(0L));
    Assertions.assertEquals(Numeric.decodeQuantity("0x400"), BigInteger.valueOf((1024L)));
    Assertions.assertEquals(Numeric.decodeQuantity("0x0"), BigInteger.valueOf((0L)));
    Assertions.assertEquals(
        Numeric.decodeQuantity("0x7fffffffffffffff"), BigInteger.valueOf((Long.MAX_VALUE)));
    Assertions.assertEquals(
        Numeric.decodeQuantity("0x99dc848b94efc27edfad28def049810f"),
        new BigInteger("204516877000845695339750056077105398031"));
  }

  @Test
  public void testQuantityDecodeLeadingZero() {
    Assertions.assertEquals(Numeric.decodeQuantity("0x0400"), BigInteger.valueOf(1024L));
    Assertions.assertEquals(Numeric.decodeQuantity("0x001"), BigInteger.valueOf(1L));
  }

  @Test
  public void testQuantityDecodeMissingPrefix() {
    Assertions.assertThrows(MessageDecodingException.class, () -> Numeric.decodeQuantity("ff"));
  }

  @Test
  public void testQuantityDecodeMissingValue() {
    Assertions.assertThrows(MessageDecodingException.class, () -> Numeric.decodeQuantity("0x"));
  }

  @Test
  public void testQuantityEncode() {
    Assertions.assertEquals(Numeric.encodeQuantity(BigInteger.valueOf(0)), "0x0");
    Assertions.assertEquals(Numeric.encodeQuantity(BigInteger.valueOf(1)), "0x1");
    Assertions.assertEquals(Numeric.encodeQuantity(BigInteger.valueOf(1024)), "0x400");
    Assertions.assertEquals(
        Numeric.encodeQuantity(BigInteger.valueOf(Long.MAX_VALUE)), "0x7fffffffffffffff");
    Assertions.assertEquals(
        Numeric.encodeQuantity(new BigInteger("204516877000845695339750056077105398031")),
        "0x99dc848b94efc27edfad28def049810f");
  }

  @Test
  public void testCleanHexPrefix() {
    Assertions.assertEquals(Numeric.cleanHexPrefix(""), "");
    Assertions.assertEquals(Numeric.cleanHexPrefix("0123456789abcdef"), "0123456789abcdef");
    Assertions.assertEquals(Numeric.cleanHexPrefix("0x"), "");
    Assertions.assertEquals(Numeric.cleanHexPrefix("0x0123456789abcdef"), "0123456789abcdef");
  }

  @Test
  public void testPrependHexPrefix() {
    Assertions.assertEquals(Numeric.prependHexPrefix(""), "0x");
    Assertions.assertEquals(Numeric.prependHexPrefix("0x0123456789abcdef"), "0x0123456789abcdef");
    Assertions.assertEquals(Numeric.prependHexPrefix("0x"), "0x");
    Assertions.assertEquals(Numeric.prependHexPrefix("0123456789abcdef"), "0x0123456789abcdef");
  }

  @Test
  public void testToHexStringWithPrefix() {
    Assertions.assertEquals(Numeric.toHexStringWithPrefix(BigInteger.TEN), "0xa");
  }

  @Test
  public void testToHexStringNoPrefix() {
    Assertions.assertEquals(Numeric.toHexStringNoPrefix(BigInteger.TEN), "a");
  }

  @Test
  public void testToBytesPadded() {
    Assertions.assertArrayEquals(Numeric.toBytesPadded(BigInteger.TEN, 1), new byte[] {0xa});

    Assertions.assertArrayEquals(
        Numeric.toBytesPadded(BigInteger.TEN, 8), new byte[] {0, 0, 0, 0, 0, 0, 0, 0xa});

    Assertions.assertArrayEquals(
        Numeric.toBytesPadded(BigInteger.valueOf(Integer.MAX_VALUE), 4),
        new byte[] {0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff});
  }

  @Test
  public void testHexStringToByteArray() {
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray(""), new byte[] {});
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray("0"), new byte[] {0});
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray("1"), new byte[] {0x1});
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray(HEX_RANGE_STRING), HEX_RANGE_ARRAY);

    Assertions.assertArrayEquals(Numeric.hexStringToByteArray("0x123"), new byte[] {0x1, 0x23});
  }

  @Test
  public void testToHexString() {
    Assertions.assertEquals(Numeric.toHexString(new byte[] {}), "0x");
    Assertions.assertEquals(Numeric.toHexString(new byte[] {0x1}), "0x01");
    Assertions.assertEquals(Numeric.toHexString(HEX_RANGE_ARRAY), HEX_RANGE_STRING);
  }

  @Test
  public void testToHexStringNoPrefixZeroPadded() {
    Assertions.assertEquals(Numeric.toHexStringNoPrefixZeroPadded(BigInteger.ZERO, 5), "00000");

    Assertions.assertEquals(
        Numeric.toHexStringNoPrefixZeroPadded(
            new BigInteger("11c52b08330e05d731e38c856c1043288f7d9744", 16), 40),
        "11c52b08330e05d731e38c856c1043288f7d9744");

    Assertions.assertEquals(
        Numeric.toHexStringNoPrefixZeroPadded(
            new BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16), 40),
        "01c52b08330e05d731e38c856c1043288f7d9744");
  }

  @Test
  public void testToHexStringWithPrefixZeroPadded() {
    Assertions.assertEquals(Numeric.toHexStringWithPrefixZeroPadded(BigInteger.ZERO, 5), "0x00000");

    Assertions.assertEquals(
        Numeric.toHexStringWithPrefixZeroPadded(
            new BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16), 40),
        "0x01c52b08330e05d731e38c856c1043288f7d9744");

    Assertions.assertEquals(
        Numeric.toHexStringWithPrefixZeroPadded(
            new BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16), 40),
        "0x01c52b08330e05d731e38c856c1043288f7d9744");
  }

  @Test
  public void testToHexStringZeroPaddedNegative() {
    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(-1), 20));
  }

  @Test
  public void testToHexStringZeroPaddedTooLargs() {
    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(-1), 5));
  }

  @Test
  public void testIsIntegerValue() {
    Assertions.assertTrue(Numeric.isIntegerValue(BigDecimal.ZERO));
    Assertions.assertTrue(Numeric.isIntegerValue(BigDecimal.ZERO));
    Assertions.assertTrue(Numeric.isIntegerValue(BigDecimal.valueOf(Long.MAX_VALUE)));
    Assertions.assertTrue(Numeric.isIntegerValue(BigDecimal.valueOf(Long.MIN_VALUE)));
    Assertions.assertTrue(
        Numeric.isIntegerValue(
            new BigDecimal("9999999999999999999999999999999999999999999999999999999999999999.0")));
    Assertions.assertTrue(
        Numeric.isIntegerValue(
            new BigDecimal("-9999999999999999999999999999999999999999999999999999999999999999.0")));

    Assertions.assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(0.1)));
    Assertions.assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(-0.1)));
    Assertions.assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(1.1)));
    Assertions.assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(-1.1)));
  }

  @Test
  public void testIsIntegerValue2() {
    Assertions.assertTrue(Numeric.isIntegerValue("20"));
    Assertions.assertTrue(Numeric.isIntegerValue("-20"));
    Assertions.assertTrue(Numeric.isIntegerValue("0"));

    Assertions.assertFalse(Numeric.isIntegerValue("0x20"));
    Assertions.assertFalse(Numeric.isIntegerValue("abc"));
    Assertions.assertFalse(Numeric.isIntegerValue("#%34"));
  }

  @Test
  public void testLittleEndian() {
    String littleEndian = Numeric.littleEndian(71);
    Assertions.assertEquals("0x4700000000000000", littleEndian);
  }
}
