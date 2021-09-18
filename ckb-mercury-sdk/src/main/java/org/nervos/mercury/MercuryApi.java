package org.nervos.mercury;

import java.io.IOException;
import java.util.List;
import org.nervos.indexer.SearchKey;
import org.nervos.indexer.resp.CellCapacityResponse;
import org.nervos.indexer.resp.CellsResponse;
import org.nervos.indexer.resp.TipResponse;
import org.nervos.indexer.resp.TransactionResponse;
import org.nervos.mercury.model.req.*;
import org.nervos.mercury.model.resp.*;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;

public interface MercuryApi {

  GetBalanceResponse getBalance(GetBalancePayload payload) throws IOException;

  TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException;

  TransactionCompletionResponse buildAdjustAccountTransaction(AdjustAccountPayload payload)
      throws IOException;

  TransactionCompletionResponse buildSmartTransferTransaction(SmartTransferPayload payload)
      throws IOException;

  TransactionInfoWithStatusResponse getTransactionInfo(String txHash) throws IOException;

  BlockInfoResponse getBlockInfo(GetBlockInfoPayload payload) throws IOException;

  List<String> registerAddresses(List<String> normalAddresses) throws IOException;

  TransactionCompletionResponse buildAssetCollectionTransaction(CollectAssetPayload payload)
      throws IOException;

  QueryTransactionsResponse queryTransactions(QueryTransactionsPayload payload) throws IOException;

  Integer getAccountNumber(String address) throws IOException;

  DBInfo getDbInfo() throws IOException;

  MercuryInfo getMercuryInfo() throws IOException;

  TipResponse getTip() throws IOException;

  CellsResponse getCells(SearchKey searchKey, String order, String limit, String afterCursor)
      throws IOException;

  TransactionResponse getTransactions(
      SearchKey searchKey, String order, String limit, String afterCursor) throws IOException;

  CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException;
}
