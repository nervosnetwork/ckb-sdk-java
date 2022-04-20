package type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.base.DynType;
import org.nervos.ckb.type.base.Type;
import org.nervos.ckb.type.dynamic.Bytes;
import org.nervos.ckb.type.dynamic.Dynamic;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.fixed.Byte1;
import org.nervos.ckb.type.fixed.Byte32;
import org.nervos.ckb.type.fixed.Empty;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.utils.Numeric;

import java.util.ArrayList;
import java.util.List;

public class DynamicTest {

  @Test
  public void toBytesTest() {
    ArrayList<Type> lock = new ArrayList<>();
    lock.add(new Byte32("0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88"));
    lock.add(new Byte1("01"));
    List<Bytes> argList = new ArrayList<>();
    argList.add(new Bytes("0xb2e61ff569acf041b3c2c17724e2379c581eeac3"));
    lock.add(new Dynamic<>(argList));
    Table script = new Table(lock);
    Table cellOutput = new Table(new UInt64(125000000000L), script, new Empty());
    List<DynType> cellOutputs = new ArrayList<>();
    cellOutputs.add(cellOutput);
    Dynamic<DynType> outputs = new Dynamic<>(cellOutputs);
    byte[] expected =
        Numeric.hexStringToByteArray(
            "71000000080000006900000010000000180000006900000000a2941a1d0000005100000010000000300000003100000068d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e8801200000000800000014000000b2e61ff569acf041b3c2c17724e2379c581eeac3");
    Assertions.assertArrayEquals(expected, outputs.toBytes());
  }
}
