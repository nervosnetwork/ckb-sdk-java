package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class NodeInfo {

  @SerializedName("node_id")
  public String nodeId;

  public String version;
  public List<Address> addresses;

  public static class Address {

    public String address;
    public String score;
  }
}
