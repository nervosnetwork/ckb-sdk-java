package org.nervos.ckb.type.base;

public interface Type<T> {

  byte[] toBytes();

  T getValue();

  int getLength();
}
