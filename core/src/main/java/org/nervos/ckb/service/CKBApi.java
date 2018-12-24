package org.nervos.ckb.service;

import org.nervos.ckb.request.Request;
import org.nervos.ckb.response.*;
import org.nervos.ckb.response.item.Cell;

/**
 * Created by duanyytop on 2018-12-20.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public interface CKBApi {

    Request<?, ResBlock> getBlock(String blockHash);

    Request<?, ResTransaction> getTransaction(String transactionHash);

    Request<?, ResBlockHash> getBlockHash(long blockNumber);

    Request<?, ResHeader> getTipHeader();

    Request<?, ResCell> getCellsByTypeHash(String typeHash);

    Request<?, ResCell> getCurrentCell(Cell.OutPoint outPoint);

    Request<?, ResTransactionHash> sendTransaction();

}
