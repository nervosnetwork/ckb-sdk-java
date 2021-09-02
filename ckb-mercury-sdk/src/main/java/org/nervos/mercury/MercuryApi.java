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

  GenericTransactionWithStatusResponse getGenericTransaction(String txHash) throws IOException;

  GenericBlockResponse getGenericBlock(GetGenericBlockPayload payload) throws IOException;

  List<String> registerAddresses(List<String> normalAddresses) throws IOException;

  TransactionCompletionResponse buildAssetCollectionTransaction(CollectAssetPayload payload)
      throws IOException;

  QueryGenericTransactionsResponse queryGenericTransactions(QueryGenericTransactionsPayload payload)
      throws IOException;
}
