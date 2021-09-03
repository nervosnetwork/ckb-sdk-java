package org.nervos.mercury;

import java.io.IOException;
import java.util.List;
import org.nervos.mercury.model.req.*;
import org.nervos.mercury.model.resp.*;

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
}
