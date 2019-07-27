package org.nervos.ckb.methods.response;

import java.math.BigInteger;
import org.nervos.ckb.methods.Response;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbBlockNumber extends Response<String> {

  public BigInteger getBlockNumber() {
    return new BigInteger(result);
  }
}
