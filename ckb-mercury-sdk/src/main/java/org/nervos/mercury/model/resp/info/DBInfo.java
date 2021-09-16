package org.nervos.mercury.model.resp.info;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;

public class DBInfo {
  public String version;

  public DBDriver db;

  @SerializedName("conn_size")
  public BigInteger connSize;

  @SerializedName("center_id")
  public BigInteger centerId;

  @SerializedName("machine_id")
  public BigInteger machineId;
}
