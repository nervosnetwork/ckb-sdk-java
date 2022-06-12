package org.nervos.ckb.utils;

import java.math.BigInteger;

public class AmountUtils {
  public static long ckbToShannon(long value) {
    return Utils.ckbToShannon(value);
  }

  public static long ckbToShannon(double value) {
    return Utils.ckbToShannon(value);
  }


  public static byte[] sudtAmountToData(BigInteger sudtAmount) {
    return MoleculeConverter.packUint128(sudtAmount).toByteArray();
  }

  public static BigInteger dataToSudtAmount(byte[] data) {
    return Numeric.littleEndianBytesToBigInteger(data);
  }
}
