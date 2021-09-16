package org.nervos.mercury.model.resp.info;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MercuryInfo {
  @SerializedName("mercury_version")
  public String mercuryVersion;

  @SerializedName("ckb_node_version")
  public String ckbNodeVersion;

  @SerializedName("network_type")
  public NetworkType networkType;

  @SerializedName("enabled_extensions")
  public List<Extension> enabledExtensions;
}
