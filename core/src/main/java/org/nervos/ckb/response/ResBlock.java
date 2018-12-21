package org.nervos.ckb.response;

import org.nervos.ckb.response.item.Block;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResBlock extends Response<Block> {

    public Block getBlock() {
        return result;
    }

}
