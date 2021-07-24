package mercury;

import indexer.CkbIndexerApi;
import java.io.IOException;
import java.util.List;
import model.CreateAssetAccountPayload;
import model.GetBalancePayload;
import model.GetGenericBlockPayload;
import model.TransferPayload;
import model.resp.GenericBlockResponse;
import model.resp.GenericTransactionWithStatusResponse;
import model.resp.GetBalanceResponse;
import model.resp.TransactionCompletionResponse;

public interface MercuryApi extends CkbIndexerApi {

  GetBalanceResponse getBalance(GetBalancePayload payload) throws IOException;

  TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException;

  TransactionCompletionResponse buildAssetAccountCreationTransaction(
      CreateAssetAccountPayload payload) throws IOException;

  GenericTransactionWithStatusResponse getGenericTransaction(String txHash) throws IOException;

  GenericBlockResponse getGenericBlock(GetGenericBlockPayload payload) throws IOException;

  List<String> registerAddresses(List<String> normalAddresses) throws IOException;
}
