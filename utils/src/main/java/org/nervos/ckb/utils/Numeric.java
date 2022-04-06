package org.nervos.ckb.utils;

import com.google.common.primitives.Bytes;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import org.nervos.ckb.exceptions.MessageDecodingException;
import org.nervos.ckb.exceptions.MessageEncodingException;

/** Message codec functions. */
public final class Numeric {

  private static final String HEX_PREFIX = "0x";

  private Numeric() {}

  public static String encodeQuantity(BigInteger value) {
    if (value.signum() != -1) {
      return HEX_PREFIX + value.toString(16);
    } else {
      throw new MessageEncodingException("Negative values are not supported");
    }
  }

  public static BigInteger decodeQuantity(String value) {
    if (!isValidHexQuantity(value)) {
      throw new MessageDecodingException("Value must be in format 0x[1-9]+[0-9]* or 0x0");
    }
    try {
      return new BigInteger(value.substring(2), 16);
    } catch (NumberFormatException e) {
      throw new MessageDecodingException("Negative ", e);
    }
  }

  private static boolean isValidHexQuantity(String value) {
    if (value == null) {
      return false;
    }

    if (value.length() < 3) {
      return false;
    }

    return value.startsWith(HEX_PREFIX);
  }

  public static String cleanHexPrefix(String input) {
    if (containsHexPrefix(input)) {
      return input.substring(2);
    } else {
      return input;
    }
  }

  public static String prependHexPrefix(String input) {
    if (!containsHexPrefix(input)) {
      return HEX_PREFIX + input;
    } else {
      return input;
    }
  }

  public static boolean containsHexPrefix(String input) {
    return input.length() > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x';
  }

  public static BigInteger toBigInt(byte[] value, int offset, int length) {
    return toBigInt((Arrays.copyOfRange(value, offset, offset + length)));
  }

  public static BigInteger toBigInt(byte[] value) {
    return new BigInteger(1, value);
  }

  public static BigInteger toBigInt(String hexValue) {
    String cleanValue = cleanHexPrefix(hexValue);
    return toBigIntNoPrefix(cleanValue);
  }

  public static BigInteger toBigIntNoPrefix(String hexValue) {
    return new BigInteger(hexValue, 16);
  }

  public static String toHexStringWithPrefix(BigInteger value) {
    return HEX_PREFIX + value.toString(16);
  }

  public static String toHexStringNoPrefix(BigInteger value) {
    return value.toString(16);
  }

  public static String toHexStringNoPrefix(byte[] input) {
    return toHexString(input, 0, input.length, false);
  }

  public static String toHexStringWithPrefixZeroPadded(BigInteger value, int size) {
    return toHexStringZeroPadded(value, size, true);
  }

  public static String toHexStringNoPrefixZeroPadded(BigInteger value, int size) {
    return toHexStringZeroPadded(value, size, false);
  }

  private static String toHexStringZeroPadded(BigInteger value, int size, boolean withPrefix) {
    String result = toHexStringNoPrefix(value);

    int length = result.length();
    if (length > size) {
      throw new UnsupportedOperationException("Value " + result + "is larger then length " + size);
    } else if (value.signum() < 0) {
      throw new UnsupportedOperationException("Value cannot be negative");
    }

    if (length < size) {
      result = Strings.zeros(size - length) + result;
    }

    if (withPrefix) {
      return HEX_PREFIX + result;
    } else {
      return result;
    }
  }

  public static byte[] toBytesPadded(BigInteger value, int length) {
    byte[] result = new byte[length];
    byte[] bytes = value.toByteArray();

    int bytesLength;
    int srcOffset;
    if (bytes[0] == 0) {
      bytesLength = bytes.length - 1;
      srcOffset = 1;
    } else {
      bytesLength = bytes.length;
      srcOffset = 0;
    }

    if (bytesLength > length) {
      throw new RuntimeException("Input is too large to put in byte array of size " + length);
    }

    int destOffset = length - bytesLength;
    System.arraycopy(bytes, srcOffset, result, destOffset, bytesLength);
    return result;
  }

  public static String littleEndian(long number) {
    byte[] bytes = Numeric.toBytesPadded(BigInteger.valueOf(number), 8);
    for (int i = 0; i < bytes.length / 2; i++) {
      byte temp = bytes[i];
      bytes[i] = bytes[bytes.length - 1 - i];
      bytes[bytes.length - 1 - i] = temp;
    }
    return toHexString(bytes);
  }

  public static byte[] hexStringToByteArray(String input) {
    String cleanInput = cleanHexPrefix(input);

    int len = cleanInput.length();

    if (len == 0) {
      return new byte[] {};
    }

    byte[] data;
    int startIdx;
    if (len % 2 != 0) {
      data = new byte[(len / 2) + 1];
      data[0] = (byte) Character.digit(cleanInput.charAt(0), 16);
      startIdx = 1;
    } else {
      data = new byte[len / 2];
      startIdx = 0;
    }

    for (int i = startIdx; i < len; i += 2) {
      data[(i + 1) / 2] =
          (byte)
              ((Character.digit(cleanInput.charAt(i), 16) << 4)
                  + Character.digit(cleanInput.charAt(i + 1), 16));
    }
    return data;
  }

  public static String toHexString(byte[] input, int offset, int length, boolean withPrefix) {
    StringBuilder stringBuilder = new StringBuilder();
    if (withPrefix) {
      stringBuilder.append("0x");
    }
    for (int i = offset; i < offset + length; i++) {
      stringBuilder.append(String.format("%02x", input[i] & 0xFF));
    }

    return stringBuilder.toString();
  }

  public static String toHexString(byte[] input) {
    return toHexString(input, 0, input.length, true);
  }

  public static String toHexString(String input) {
    try {
      return Numeric.containsHexPrefix(input)
          ? input
          : Numeric.toHexStringWithPrefix(new BigInteger(input));
    } catch (NumberFormatException e) {
      throw new NumberFormatException(
          "Input parameter format error, please input integer or hex string");
    }
  }

  public static byte asByte(int m, int n) {
    return (byte) ((m << 4) | n);
  }

  public static boolean isIntegerValue(BigDecimal value) {
    return value.signum() == 0 || value.scale() <= 0 || value.stripTrailingZeros().scale() <= 0;
  }

  public static boolean isIntegerValue(String value) {
    try {
      Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public static List<Byte> intToBytes(int value) {
    return Bytes.asList(Numeric.hexStringToByteArray(Integer.toHexString(value)));
  }

  public static byte[] concatBytes(byte[] a, byte[] b) {
    byte[] c = new byte[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }
}
