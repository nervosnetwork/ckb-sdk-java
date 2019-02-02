package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.Cell;

/**
 * Created by duanyytop on 2018-12-25.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class CkbCell extends Response<CkbCell.CellStatus> {

    public CellStatus getCellStatus() {
        return result;
    }

    public class CellStatus {
        public Cell cell;
        public String status;
    }

}
