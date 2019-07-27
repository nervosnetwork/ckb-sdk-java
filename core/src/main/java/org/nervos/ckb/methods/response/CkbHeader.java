package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.Header;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbHeader extends Response<Header> {

  public Header getHeader() {
    return result;
  }
}
