package org.nervos.mercury;

import org.nervos.indexer.CkbIndexerApi;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.req.payload.*;
import org.nervos.mercury.model.resp.*;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;
import org.nervos.mercury.model.resp.info.MercurySyncState;

import java.io.IOException;
import java.util.List;

public interface MercuryApi extends CkbIndexerApi {

  GetBalanceResponse getBalance(GetBalancePayload payload) throws IOException;

  TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException;

  TransactionCompletionResponse buildAdjustAccountTransaction(AdjustAccountPayload payload)
      throws IOException;

  TransactionCompletionResponse buildSimpleTransferTransaction(SimpleTransferPayload payload)
      throws IOException;

  GetTransactionInfoResponse getTransactionInfo(byte[] txHash) throws IOException;

  BlockInfoResponse getBlockInfo(GetBlockInfoPayload payload) throws IOException;

  List<byte[]> registerAddresses(List<String> normalAddresses) throws IOException;

  PaginationResponse<TransactionWithRichStatus> queryTransactionsWithTransactionView(
      QueryTransactionsPayload payload) throws IOException;

  PaginationResponse<TransactionInfoResponse> queryTransactionsWithTransactionInfo(
      QueryTransactionsPayload payload) throws IOException;

  DBInfo getDbInfo() throws IOException;

  MercuryInfo getMercuryInfo() throws IOException;

  MercurySyncState getSyncState() throws IOException;

  TransactionCompletionResponse buildDaoDepositTransaction(DaoDepositPayload payload)
      throws IOException;

  TransactionCompletionResponse buildDaoWithdrawTransaction(DaoWithdrawPayload payload)
      throws IOException;

  TransactionCompletionResponse buildDaoClaimTransaction(DaoClaimPayload payload)
      throws IOException;

  TxView<TransactionWithRichStatus> getSpentTransactionWithTransactionView(
      GetSpentTransactionPayload payload) throws IOException;

  TxView<TransactionInfoResponse> getSpentTransactionWithTransactionInfo(
      GetSpentTransactionPayload payload) throws IOException;

  TransactionCompletionResponse buildSudtIssueTransaction(SudtIssuePayload payload)
      throws IOException;
}
