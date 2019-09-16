package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class NodeInfo {

  @JsonProperty("node_id")
  public String nodeId;

  public String version;
  public List<Address> addresses;

  public static class Address {

    public String address;
    public String score;
  }
}
