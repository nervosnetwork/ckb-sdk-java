package org.nervos.ckb.utils;

import org.nervos.ckb.type.Transaction;

public class Calculator {
  // 4 bytes for the tx offset cost with molecule vector (transactions)
  private static final int SERIALIZED_TX_OFFSET_BYTE_SIZE = 4;

  public static int calculateTransactionSize(Transaction transaction) {
    byte[] bytes = transaction.pack().toByteArray();
    return bytes.length + SERIALIZED_TX_OFFSET_BYTE_SIZE;
  }

  private static final long RADIO = 1000;

  // https://docs.nervos.org/docs/essays/faq/#what-is-the-min_fee_rate
  private static long calculateTransactionFee(long transactionSize, long feeRate) {
    long base = transactionSize * feeRate;
    long fee = Long.divideUnsigned(base, RADIO);
    if (Long.compareUnsigned(fee * feeRate, base) < 0) {
      return fee + 1;
    }
    return fee;
  }

  public static long calculateTransactionFee(Transaction transaction, long feeRate) {
    long txSize = calculateTransactionSize(transaction);
    return calculateTransactionFee(txSize, feeRate);
  }

  private static final double DEFAULT_BYTES_PER_CYCLES = 0.000_170_571_4;

  public static long calculateTransactionFee(Transaction transaction, long cycles, long feeRate) {
    long txSize = Math.max(calculateTransactionSize(transaction), (long) (cycles * DEFAULT_BYTES_PER_CYCLES));
    return calculateTransactionFee(txSize, feeRate);
  }
}
