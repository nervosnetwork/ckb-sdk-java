package org.nervos.ckb.utils;

import java.math.BigInteger;

/** Copyright © 2021 Nervos Foundation. All rights reserved. */
public class Const {
  public static final String NODE_URL = "http://localhost:8114";
  public static final String CKB_INDEXER_URL = "http://localhost:8116";

  public static final BigInteger UnitCKB = new BigInteger("100000000");
  public static final BigInteger MIN_CKB = new BigInteger("6100000000");
  public static final BigInteger MIN_SUDT_CKB = new BigInteger("14200000000");

  public static final String SUDT_CODE_HASH =
      "0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4";
  public static final String SUDT_TX_HASH =
      "0xe12877ebd2c3c364dc46c5c992bcfaf4fee33fa13eebdf82c591fc9825aab769";

  public static final String ACP_CODE_HASH =
      "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356";
  public static final String ACP_TX_HASH =
      "0xec26b0f85ed839ece5f11c4c4e837ec359f5adc4420410f6453b1f6b60fb96a6";
}
