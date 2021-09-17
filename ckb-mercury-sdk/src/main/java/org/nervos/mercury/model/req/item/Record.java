package org.nervos.mercury.model.req.item;

import com.google.common.primitives.Bytes;
import com.google.gson.annotations.SerializedName;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

public class Record implements Item {

  public static final String SCRIPT_TYPE = "0x01";

  public static final String Address_TYPE = "0x00";

  @SerializedName("Record")
  public String record;

  public transient OutPoint outPoint;

  public transient Script script;

  public transient String address;

  public Record() {}

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
    byte[] record =
        Bytes.concat(
            Numeric.hexStringToByteArray(this.outPoint.txHash),
            intToByteArray(Numeric.toBigInt(this.outPoint.index).intValue()));

    if (Objects.nonNull(this.script)) {

      return Numeric.toHexString(
          Bytes.concat(
              record,
              Numeric.hexStringToByteArray(Record.SCRIPT_TYPE),
              Numeric.cleanHexPrefix(this.script.computeHash())
                  .substring(0, 40)
                  .getBytes(StandardCharsets.UTF_8)));
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
    byteArray[0] = (byte) (value & 0xFF);
    byteArray[1] = (byte) (value & 0xFF00);
    byteArray[2] = (byte) (value & 0xFF0000);
    byteArray[3] = (byte) (value & 0xFF000000);
    return byteArray;
  }
}
