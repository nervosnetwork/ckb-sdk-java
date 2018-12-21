package org.nervos.ckb.response;

import org.nervos.ckb.response.item.Transaction;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResTransaction extends Response<Transaction> {

    public Transaction getTransaction() {
        return result;
    }

}
