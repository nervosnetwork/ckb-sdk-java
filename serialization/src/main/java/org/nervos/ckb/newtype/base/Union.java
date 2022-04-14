package org.nervos.ckb.newtype.base;

public abstract class Union extends Molecule {
  protected int typeId;

  protected Object item;

  public int getTypeId() {
    return this.typeId;
  }

  public Object getItem() {
    return this.item;
  }
}
