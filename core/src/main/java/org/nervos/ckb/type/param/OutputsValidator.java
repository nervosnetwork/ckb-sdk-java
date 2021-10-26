package org.nervos.ckb.type.param;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public enum OutputsValidator {
  WELL_KNOWN_SCRIPTS_ONLY("well_known_scripts_only"),
  PASSTHROUGH("passthrough");

  private final String value;

  OutputsValidator(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
