package org.nervos.api;

import org.nervos.ckb.CkbRpcApi;
import org.nervos.indexer.CkbIndexerApi;
import org.nervos.mercury.MercuryApi;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public interface CkbApi extends CkbRpcApi, MercuryApi, CkbIndexerApi {}
