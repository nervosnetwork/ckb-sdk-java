package org.nervos.ckb.type;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public interface Type<T> {

  byte[] toBytes();

  T getValue();
}
