package org.nervos.mercury.model.common;

import java.math.BigInteger;

public class Range {
  public BigInteger from;
  public BigInteger to;

  public Range(BigInteger from, BigInteger to) {
    this.from = from;
    this.to = to;
  }
}
