package com.nervos.lightclient;

import com.google.gson.reflect.TypeToken;
import com.nervos.lightclient.type.*;
import org.nervos.ckb.service.GsonFactory;
import org.nervos.ckb.service.RpcService;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.OutputsValidator;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.Convert;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultLightClientApi implements LightClientApi {

  private RpcService rpcService;


  public DefaultLightClientApi(String nodeUrl) {
    this(nodeUrl, false);
  }

  public DefaultLightClientApi(String nodeUrl, boolean isDebug) {
    rpcService = new RpcService(nodeUrl, isDebug);
  }

  public DefaultLightClientApi(RpcService rpcService) {
    this.rpcService = rpcService;
  }

  @Override
  public void setScripts(List<ScriptDetail> scriptDetails) throws IOException {
    rpcService.post(
        "set_scripts",
        Collections.singletonList(scriptDetails), Object.class);
  }

  @Override
  public List<ScriptDetail> getScripts() throws IOException {
    return rpcService.post(
        "get_scripts",
        Collections.<String>emptyList(),
        new TypeToken<List<ScriptDetail>>() {}.getType());
  }

  @Override
  public byte[] sendTransaction(Transaction transaction) throws IOException {
    return rpcService.post(
        "send_transaction",
        Arrays.asList(Convert.parseTransaction(transaction), OutputsValidator.PASSTHROUGH),
        byte[].class);
  }

  @Override
  public Header getTipHeader() throws IOException {
    return rpcService.post("get_tip_header", Collections.<String>emptyList(), Header.class);
  }

  @Override
  public Block getGenesisBlock() throws IOException {
    return rpcService.post("get_genesis_block", Collections.<String>emptyList(), Block.class);
  }

  @Override
  public Header getHeader(byte[] blockHash) throws IOException {
    return rpcService.post(
        "get_header",
        Collections.singletonList(blockHash), Header.class);
  }

  @Override
  public TransactionWithHeader getTransaction(byte[] transactionHash) throws IOException {
    return rpcService.post(
        "get_transaction",
        Collections.singletonList(transactionHash), TransactionWithHeader.class);
  }

  @Override
  public FetchedHeader fetchHeader(byte[] blockHash) throws IOException {
    return rpcService.post(
        "fetch_header",
        Collections.singletonList(blockHash), FetchedHeader.class);
  }

  @Override
  public FetchedTransaction fetchTransaction(byte[] transactionHash) throws IOException {
    return rpcService.post(
        "fetch_transaction",
        Collections.singletonList(transactionHash), FetchedTransaction.class);
  }

  @Override
  public CellsResponse getCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    return this.rpcService.post(
        "get_cells",
        Arrays.asList(searchKey, order, limit, afterCursor),
        CellsResponse.class);
  }

  @Override
  public TxsWithCell getTransactions(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    searchKey.groupByTransaction = false;
    return this.rpcService.post(
        "get_transactions",
        Arrays.asList(searchKey, order, limit, afterCursor),
        TxsWithCell.class, GsonFactory.create());
  }

  @Override
  public TxsWithCells getTransactionsGrouped(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    searchKey.groupByTransaction = true;
    return this.rpcService.post(
        "get_transactions",
        Arrays.asList(searchKey, order, limit, afterCursor),
        TxsWithCells.class);
  }

  @Override
  public CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException {
    return this.rpcService.post(
        "get_cells_capacity",
        Arrays.asList(searchKey),
        CellCapacityResponse.class);
  }
}
