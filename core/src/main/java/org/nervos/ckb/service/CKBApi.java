package org.nervos.ckb.service;

import org.nervos.ckb.request.Request;
import org.nervos.ckb.response.*;
import org.nervos.ckb.response.item.Cell;
import org.nervos.ckb.response.item.Transaction;

/**
 * Created by duanyytop on 2018-12-20.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public interface CKBApi {

    Request<?, ResBlock> getBlock(String blockHash);

    Request<?, ResTransaction> getTransaction(String transactionHash);

    Request<?, ResBlockHash> getBlockHash(long blockNumber);

    Request<?, ResHeader> getTipHeader();

    Request<?, ResCells> getCellsByTypeHash(String typeHash, long fromBlockNumber, long toBlockNumber);

    Request<?, ResCell> getLiveCell(Cell.OutPoint outPoint);

    Request<?, ResBlockNumber> getTipBlockNumber();

    Request<?, ResNodeId> localNodeId();

    Request<?, ResTransactionHash> sendTransaction(Transaction transaction);

}
