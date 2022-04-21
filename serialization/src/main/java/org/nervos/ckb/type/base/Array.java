package org.nervos.ckb.type.base;

public abstract class Array extends Molecule {
  public abstract int getItemCount();

  public abstract int getItemSize();

  public abstract Class getItemType();
}
