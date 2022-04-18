package org.nervos.ckb.type;

import java.util.List;

public class NodeInfo {
  public String nodeId;
  public boolean active;
  public long connections;

  public String version;
  public List<Address> addresses;
  public List<Protocol> protocols;

  public static class Address {
    public String address;
    public long score;
  }

  public static class Protocol {
    public long id;
    public String name;
    public List<String> supportVersions;
  }
}
