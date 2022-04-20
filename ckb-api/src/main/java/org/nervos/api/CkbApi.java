package org.nervos.api;

import org.nervos.ckb.CkbRpcApi;
import org.nervos.indexer.CkbIndexerApi;
import org.nervos.mercury.MercuryApi;

public interface CkbApi extends CkbRpcApi, MercuryApi, CkbIndexerApi {}
