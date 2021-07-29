package org.nervos.mercury;

import java.io.IOException;
import java.util.List;
import org.nervos.mercury.model.req.CollectAssetPayload;
import org.nervos.mercury.model.req.CreateAssetAccountPayload;
import org.nervos.mercury.model.req.GetBalancePayload;
import org.nervos.mercury.model.req.GetGenericBlockPayload;
import org.nervos.mercury.model.req.QueryGenericTransactionsPayload;
import org.nervos.mercury.model.req.TransferPayload;
import org.nervos.mercury.model.resp.GenericBlockResponse;
import org.nervos.mercury.model.resp.GenericTransactionWithStatusResponse;
import org.nervos.mercury.model.resp.GetBalanceResponse;
import org.nervos.mercury.model.resp.QueryGenericTransactionsResponse;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

public interface MercuryApi {

  GetBalanceResponse getBalance(GetBalancePayload payload) throws IOException;

  TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException;

  TransactionCompletionResponse buildAssetAccountCreationTransaction(
      CreateAssetAccountPayload payload) throws IOException;

  GenericTransactionWithStatusResponse getGenericTransaction(String txHash) throws IOException;

  GenericBlockResponse getGenericBlock(GetGenericBlockPayload payload) throws IOException;

  List<String> registerAddresses(List<String> normalAddresses) throws IOException;

  TransactionCompletionResponse buildAssetCollectionTransaction(CollectAssetPayload payload)
      throws IOException;

  QueryGenericTransactionsResponse queryGenericTransactions(QueryGenericTransactionsPayload payload)
      throws IOException;
}
