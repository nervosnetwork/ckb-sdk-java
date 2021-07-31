package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class NodeInfo {

  @SerializedName("node_id")
  public String nodeId;

  public boolean active;

  @SerializedName("connections")
  public String connections;

  public String version;
  public List<Address> addresses;
  public List<Protocol> protocols;

  public static class Address {
    public String address;
    public String score;
  }

  public static class Protocol {
    public String id;
    public String name;

    @SerializedName("support_versions")
    public List<String> supportVersions;
  }
}
