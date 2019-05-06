package org.nervos.ckb.methods.response;

import java.math.BigInteger;
import org.nervos.ckb.methods.Response;

/** Created by duanyytop on 2018-12-21. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbBlockNumber extends Response<String> {

  public BigInteger getBlockNumber() {
    return new BigInteger(result);
  }
}
