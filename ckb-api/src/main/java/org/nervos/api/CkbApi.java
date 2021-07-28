package org.nervos.api;

import indexer.CkbIndexerApi;
import mercury.MercuryApi;
import org.nervos.ckb.CkbRpcApi;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public interface CkbApi extends CkbRpcApi, MercuryApi, CkbIndexerApi {}
