package type;

import java.io.IOException;
import java.math.BigInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class ScriptTest {

  @Test
  public void testScriptHashWithCodeHash() throws IOException {
    byte[] codeHash =
        Numeric.hexStringToByteArray(
            Hash.blake2b(
                "0x1400000000000e00100000000c000800000004000e0000000c00000014000000740100000000000000000600080004000600000004000000580100007f454c460201010000000000000000000200f3000100000078000100000000004000000000000000980000000000000005000000400038000100400003000200010000000500000000000000000000000000010000000000000001000000000082000000000000008200000000000000001000000000000001459308d00573000000002e7368737472746162002e74657874000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000b000000010000000600000000000000780001000000000078000000000000000a000000000000000000000000000000020000000000000000000000000000000100000003000000000000000000000000000000000000008200000000000000110000000000000000000000000000000100000000000000000000000000000000000000"));
    Script script = new Script(codeHash, new byte[] {}, Script.HashType.DATA);
    System.out.println(script.computeHash());
    Assertions.assertEquals(
        "0x63c5fece50cdeb3978b02b43d3cff43809310b7ac89519bc717309ecc58904d9",
        Numeric.toHexString(script.computeHash()));
  }

  @Test
  public void testOccupiedCapacity() {
    Script script =
        new Script(
            Numeric.hexStringToByteArray(
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
            Numeric.hexStringToByteArray("0x36c329ed630d6ce750712a477543672adab57f4c"),
            Script.HashType.TYPE);
    Assertions.assertEquals(BigInteger.valueOf(5300000000L), script.occupiedCapacity());
  }
}
