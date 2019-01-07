package org.nervos.ckb.service;

import org.nervos.ckb.methods.request.Request;
import org.nervos.ckb.methods.response.*;
import org.nervos.ckb.methods.response.item.Cell;
import org.nervos.ckb.methods.response.item.Transaction;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by duanyytop on 2018-12-20.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class JsonRpcCKBApiImpl implements CKBService {

    protected final APIService apiService;

    public JsonRpcCKBApiImpl(APIService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Request<?, CkbBlock> getBlock(String blockHash) {
        return new Request<>(
                "get_block",
                Arrays.asList(blockHash),
                apiService,
                CkbBlock.class);
    }


    @Override
    public Request<?, CkbTransaction> getTransaction(String transactionHash) {
        return new Request<>(
                "get_transaction",
                Arrays.asList(transactionHash),
                apiService,
                CkbTransaction.class);
    }


    @Override
    public Request<?, CkbBlockHash> getBlockHash(long blockNumber) {
        return new Request<>(
                "get_block_hash",
                Arrays.asList(blockNumber),
                apiService,
                CkbBlockHash.class);
    }


    @Override
    public Request<?, CkbHeader> getTipHeader() {
        return new Request<>(
                "get_tip_header",
                Collections.<String>emptyList(),
                apiService,
                CkbHeader.class);
    }


    @Override
    public Request<?, CkbCells> getCellsByTypeHash(String typeHash, long fromBlockNumber, long toBlockNumber) {
        return new Request<>(
                "get_cells_by_type_hash",
                Arrays.asList(typeHash, fromBlockNumber, toBlockNumber),
                apiService,
                CkbCells.class);
    }


    @Override
    public Request<?, CkbCell> getLiveCell(Cell.OutPoint outPoint) {
        return new Request<>(
                "get_live_cell",
                Arrays.asList(outPoint),
                apiService,
                CkbCell.class);
    }

    @Override
    public Request<?, CkbBlockNumber> getTipBlockNumber() {
        return new Request<>(
                "get_tip_block_number",
                Collections.<String>emptyList(),
                apiService,
                CkbBlockNumber.class);
    }

    @Override
    public Request<?, CkbNodeId> localNodeId() {
        return new Request<>(
                "local_node_id",
                Collections.<String>emptyList(),
                apiService,
                CkbNodeId.class);
    }

    @Override
    public Request<?, CkbTransactionHash> sendTransaction(Transaction transaction) {
        return new Request<>(
                "send_transaction",
                Arrays.asList(transaction),
                apiService,
                CkbTransactionHash.class);
    }


}
