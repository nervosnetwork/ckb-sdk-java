package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.transaction.LockUtils;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellOutput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class NervosDaoExample {
  private static final int DAO_LOCK_PERIOD_EPOCHS = 180;
  private static final int DAO_MATURITY_BLOCKS = 5;

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static String MinerPrivateKey =
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
  private static String MinerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";

  static {
    api = new Api(NODE_URL, false);
  }

  private static void depositToDao(BigInteger capacity, BigInteger fee) throws IOException {
    Script lock =
        LockUtils.generateLockScriptWithAddress(
            MinerAddress, SystemContract.getSystemSecpCell(api).cellHash);
    Script type =
        new Script(SystemContract.getSystemNervosDaoCell(api).cellHash, "0x", Script.TYPE);
    CellOutput cellOutput = new CellOutput(capacity.toString(16), lock, type);
  }
}
