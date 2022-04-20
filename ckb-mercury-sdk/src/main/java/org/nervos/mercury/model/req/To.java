package org.nervos.mercury.model.req;

import java.util.List;

public class To {
  public List<ToInfo> toInfos;
  public Mode mode;

  public To(List<ToInfo> toInfos, Mode mode) {
    this.toInfos = toInfos;
    this.mode = mode;
  }

  public static To newTo(List<ToInfo> infos, Mode mode) {
    return new To(infos, mode);
  }
}
