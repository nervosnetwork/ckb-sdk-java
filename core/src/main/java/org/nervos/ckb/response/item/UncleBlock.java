package org.nervos.ckb.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by duanyytop on 2019-01-04.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class UncleBlock {

    public Header header;
    public Transaction cellbase;

    @JsonProperty("proposal_transactions")
    public Byte[] proposalTransactions;

}
