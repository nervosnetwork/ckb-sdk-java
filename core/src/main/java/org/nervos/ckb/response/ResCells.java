package org.nervos.ckb.response;

import org.nervos.ckb.response.item.Cell;

import java.util.List;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResCells extends Response<List<Cell>> {

    public List<Cell> getCells() {
        return result;
    }

}
