package org.nervos.ckb;

import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.type.*;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Encoder {

  public static Table toTable(Object object) {
    if (object instanceof Script) {
      Script script = (Script) object;
      List<Type> types = new ArrayList<>();
      types.add(new Byte32(script.codeHash));
      types.add(Script.DATA.equals(script.hashType) ? new Byte1("00") : new Byte1("01"));
      List<Bytes> argList = new ArrayList<>();
      for (String arg : script.args) {
        argList.add(new Bytes(arg));
      }
      types.add(new DynVec(argList));
      return new Table(types);
    }
    return null;
  }

  public static byte[] encode(Table table) {
    return table.toBytes();
  }
}
