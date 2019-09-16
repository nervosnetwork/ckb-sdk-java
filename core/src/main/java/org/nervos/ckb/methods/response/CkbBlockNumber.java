package org.nervos.ckb.methods.response;

import java.math.BigInteger;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbBlockNumber extends Response<String> {

  public BigInteger getBlockNumber() {
    return Numeric.toBigInt(result);
  }
}
