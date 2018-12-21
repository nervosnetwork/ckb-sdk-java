package org.nervos.ckb.response;

import org.nervos.ckb.response.item.Cell;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResCell extends Response<Cell> {

    public Cell getCell() {
        return result;
    }

}
