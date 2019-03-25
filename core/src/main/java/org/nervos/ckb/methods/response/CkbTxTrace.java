package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.TxTrace;

import java.util.List;

/**
 * Created by duanyytop on 2019-03-01.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class CkbTxTrace extends Response<List<TxTrace>> {

    public List<TxTrace> getTxTrace() {
        return result;
    }

}
