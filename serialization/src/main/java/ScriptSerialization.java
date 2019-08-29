import java.util.Collections;
import org.nervos.ckb.type.Byte32;
import org.nervos.ckb.type.Byte8;
import org.nervos.ckb.type.Bytes;
import org.nervos.ckb.type.BytesVec;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class ScriptSerialization {

  Script script =
      new Script(
          new Byte32(
              Numeric.hexStringToByteArray(
                  "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88")),
          new BytesVec(
              Collections.singletonList(new Bytes("0x3954acece65096bfa81258983ddb83915fc56bd8"))),
          new Byte8(Numeric.hexStringToByteArray("01")));

  static class Script {
    public Byte32 codeHash;
    public BytesVec args;
    public Byte8 hashType;

    public Script(Byte32 codeHash, BytesVec args, Byte8 hashType) {
      this.codeHash = codeHash;
      this.args = args;
      this.hashType = hashType;
    }
  }
}
