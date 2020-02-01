package org.nervos.ckb.type.param;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public enum OutputsValidator {
  DEFAULT("default"),
  PASSTHROUGH("passthrough");

  private final String value;

  OutputsValidator(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
