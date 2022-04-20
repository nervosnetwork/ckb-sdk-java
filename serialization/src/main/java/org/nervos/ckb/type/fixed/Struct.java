package org.nervos.ckb.type.fixed;

import java.util.Arrays;
import java.util.List;
import org.nervos.ckb.type.base.FixedType;
import org.nervos.ckb.type.base.Type;

public class Struct extends FixedType<List<Type>> {

  private List<Type> value;

  public Struct(List<Type> value) {
    this.value = value;
  }

  public Struct(Type... value) {
    this.value = Arrays.asList(value);
  }

  @Override
  public byte[] toBytes() {
    int length = getLength();
    byte[] dest = new byte[length];
    int offset = 0;
    for (Type type : value) {
      System.arraycopy(type.toBytes(), 0, dest, offset, type.getLength());
      offset += type.getLength();
    }
    return dest;
  }

  @Override
  public List<Type> getValue() {
    return value;
  }

  @Override
  public int getLength() {
    int length = 0;
    for (Type type : value) {
      length += type.getLength();
    }
    return length;
  }
}
