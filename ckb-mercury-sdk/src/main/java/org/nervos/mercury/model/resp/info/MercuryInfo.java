package org.nervos.mercury.model.resp.info;

import java.util.List;

public class MercuryInfo {
  public String mercuryVersion;
  public String ckbNodeVersion;
  public NetworkType networkType;
  public List<Extension> enabledExtensions;
}
