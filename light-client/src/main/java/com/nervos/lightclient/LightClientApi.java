package com.nervos.lightclient;

import com.nervos.lightclient.type.*;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.Transaction;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.List;

public interface LightClientApi {

  void setScripts(List<ScriptDetail> scriptDetails) throws IOException;

  List<ScriptDetail> getScripts() throws IOException;

  byte[] sendTransaction(Transaction transaction) throws IOException;

  Header getTipHeader() throws IOException;

  Block getGenesisBlock() throws IOException;

  Header getHeader(byte[] blockHash) throws IOException;

  TransactionWithHeader getTransaction(byte[] transactionHash) throws IOException;

  FetchedHeader fetchHeader(byte[] blockHash) throws IOException;

  FetchedTransaction fetchTransaction(byte[] transactionHash) throws IOException;

  CellsResponse getCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException;

  TxsWithCell getTransactions(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException;

  TxsWithCells getTransactionsGrouped(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException;

  CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException;
}
