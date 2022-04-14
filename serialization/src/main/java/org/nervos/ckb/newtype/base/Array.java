package org.nervos.ckb.newtype.base;

public abstract class Array extends Molecule {
  public abstract int getItemCount();

  public abstract int getItemSize();

  public abstract Class getItemType();
}
