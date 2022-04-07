package org.nervos.mercury.model.req.item;

import com.google.common.primitives.Bytes;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

public class Record {

  public static final String SCRIPT_TYPE = "0x01";

  public static final String Address_TYPE = "0x00";

  public String record;

  public transient OutPoint outPoint;

  public transient Script script;

  public transient String address;

  public Record(OutPoint outPoint, Script script) {
    this(outPoint, script, null);
  }

  public Record(OutPoint outPoint, String address) {
    this(outPoint, null, address);
  }

  public Record(OutPoint outPoint, Script script, String address) {
    this.outPoint = outPoint;
    this.script = script;
    this.address = address;
    this.record = this.toRecordItem();
  }

  public String toRecordItem() {
    byte[] record = Bytes.concat(this.outPoint.txHash, intToByteArray(this.outPoint.index));

    if (Objects.nonNull(this.script)) {
      return Numeric.toHexString(
          Bytes.concat(
              record,
              Numeric.hexStringToByteArray(Record.SCRIPT_TYPE),
              Arrays.copyOfRange(this.script.computeHash(), 0, 20)));
    } else {
      return Numeric.toHexString(
          Bytes.concat(
              record,
              Numeric.hexStringToByteArray(Record.Address_TYPE),
              this.address.getBytes(StandardCharsets.UTF_8)));
    }
  }

  private byte[] intToByteArray(int value) {
    byte[] byteArray = new byte[4];
    byteArray[3] = (byte) (value & 0xFF);
    byteArray[2] = (byte) (value & 0xFF00);
    byteArray[1] = (byte) (value & 0xFF0000);
    byteArray[0] = (byte) (value & 0xFF000000);
    return byteArray;
  }
}
