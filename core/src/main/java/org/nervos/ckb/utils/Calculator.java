package org.nervos.ckb.utils;

import java.math.BigInteger;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.transaction.Transaction;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Calculator {
  // 4 bytes for the tx offset cost with molecule vector (transactions)
  private static final int SERIALIZED_TX_OFFSET_BYTE_SIZE = 4;

  public static int calculateTransactionSize(Transaction transaction) {
    Table serializedTx = Serializer.serializeTransaction(transaction);
    return serializedTx.getLength() + SERIALIZED_TX_OFFSET_BYTE_SIZE;
  }

  private static final BigInteger RADIO = BigInteger.valueOf(1000);

  private static BigInteger calculateTransactionFee(
      BigInteger transactionSize, BigInteger feeRate) {
    BigInteger base = transactionSize.multiply(feeRate);
    BigInteger fee = base.divide(RADIO);
    if (fee.multiply(RADIO).compareTo(base) < 0) {
      return fee.add(BigInteger.ONE);
    }
    return fee;
  }

  public static BigInteger calculateTransactionFee(Transaction transaction, BigInteger feeRate) {
    BigInteger txSize = BigInteger.valueOf(calculateTransactionSize(transaction));
    return calculateTransactionFee(txSize, feeRate);
  }
}
