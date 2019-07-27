package org.nervos.ckb.methods.response;

import java.util.List;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.PeerState;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbPeersState extends Response<List<PeerState>> {

  public List<PeerState> getPeersState() {
    return result;
  }
}
