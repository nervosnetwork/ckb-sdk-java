package org.nervos.ckb.response;

import org.nervos.ckb.response.item.TransactionItem;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResTransaction extends Response<TransactionItem> {

    public TransactionItem getTransaction() {
        return result;
    }

}
