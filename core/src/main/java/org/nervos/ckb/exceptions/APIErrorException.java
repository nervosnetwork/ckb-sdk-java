package org.nervos.ckb.exceptions;

import java.io.IOException;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class APIErrorException extends IOException {

  public APIErrorException(String message) {
    super(message);
  }
}
