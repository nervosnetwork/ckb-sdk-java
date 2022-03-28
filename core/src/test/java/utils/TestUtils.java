package utils;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

public class TestUtils {
  public static Script createScript(String codeHash, String args) {
    return new Script(Numeric.hexStringToByteArray(codeHash),
            Numeric.hexStringToByteArray(args));
  }

  public static Script createScript(String codeHash, String args, Script.HashType hashtype) {
    return new Script(Numeric.hexStringToByteArray(codeHash),
            Numeric.hexStringToByteArray(args), hashtype);
  }
}
