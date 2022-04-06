package org.nervos.mercury.model.resp.info;

import com.google.gson.annotations.SerializedName;

public class DBInfo {
  public String version;

  public DBDriver db;

  @SerializedName("conn_size")
  public int connSize;

  @SerializedName("center_id")
  public int centerId;

  @SerializedName("machine_id")
  public int machineId;
}
