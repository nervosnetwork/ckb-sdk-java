package org.nervos.ckb.utils;

import java.math.BigInteger;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.transaction.Transaction;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Calculator {
  private static final int SERIALIZED_TX_OFFSET_BYTE_SIZE = 4;

  public static int calculateSerializedSizeInBlock(Transaction transaction) {
    Table serializedTx = Serializer.serializeTransaction(transaction);
    return serializedTx.getLength() + SERIALIZED_TX_OFFSET_BYTE_SIZE;
  }

  private static final BigInteger RADIO = BigInteger.valueOf(1000);

  public static BigInteger calculateMinTransactionFee(
      BigInteger transactionSize, BigInteger minFeeRate) {
    BigInteger base = transactionSize.multiply(minFeeRate);
    BigInteger fee = base.divide(RADIO);
    if (fee.multiply(RADIO).compareTo(base) < 0) {
      return fee.add(BigInteger.ONE);
    }
    return fee;
  }
}
