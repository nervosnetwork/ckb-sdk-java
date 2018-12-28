package org.nervos.ckb.service;

import org.nervos.ckb.request.Request;
import org.nervos.ckb.response.*;
import org.nervos.ckb.response.item.Cell;
import org.nervos.ckb.response.item.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by duanyytop on 2018-12-20.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class JsonRpcCKBApiImpl implements CKBService {

    protected final APIService apiService;

    public JsonRpcCKBApiImpl(APIService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Request<?, ResBlock> getBlock(String blockHash) {
        return new Request<>(
                "get_block",
                Arrays.asList(blockHash),
                apiService,
                ResBlock.class);
    }


    @Override
    public Request<?, ResTransaction> getTransaction(String transactionHash) {
        return new Request<>(
                "get_transaction",
                Arrays.asList(transactionHash),
                apiService,
                ResTransaction.class);
    }


    @Override
    public Request<?, ResBlockHash> getBlockHash(long blockNumber) {
        return new Request<>(
                "get_block_hash",
                Arrays.asList(blockNumber),
                apiService,
                ResBlockHash.class);
    }


    @Override
    public Request<?, ResHeader> getTipHeader() {
        return new Request<>(
                "get_tip_header",
                Collections.<String>emptyList(),
                apiService,
                ResHeader.class);
    }


    @Override
    public Request<?, ResCells> getCellsByTypeHash(String typeHash, long fromBlockNumber, long toBlockNumber) {
        return new Request<>(
                "get_cells_by_type_hash",
                Arrays.asList(typeHash, fromBlockNumber, toBlockNumber),
                apiService,
                ResCells.class);
    }


    @Override
    public Request<?, ResCell> getLiveCell(Cell.OutPoint outPoint) {
        return new Request<>(
                "get_live_cell",
                Arrays.asList(outPoint),
                apiService,
                ResCell.class);
    }

    @Override
    public Request<?, ResBlockNumber> getTipBlockNumber() {
        return new Request<>(
                "get_tip_block_number",
                Collections.<String>emptyList(),
                apiService,
                ResBlockNumber.class);
    }

    @Override
    public Request<?, ResNodeId> localNodeId() {
        return new Request<>(
                "local_node_id",
                Collections.<String>emptyList(),
                apiService,
                ResNodeId.class);
    }

    @Override
    public Request<?, ResTransactionHash> sendTransaction(Transaction transaction) {
        return new Request<>(
                "send_transaction",
                Arrays.asList(transaction),
                apiService,
                ResTransactionHash.class);
    }


}
