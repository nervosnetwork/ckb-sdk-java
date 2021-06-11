package mercury;

import java.io.IOException;
import model.CreateWalletPayload;
import model.TransferPayload;
import model.resp.GetBalanceResponse;
import model.resp.TransactionCompletionResponse;

public interface MercuryApi {

  GetBalanceResponse getBalance(String udtHash, String ident) throws IOException;

  TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException;

  TransactionCompletionResponse buildWalletCreationTransaction(CreateWalletPayload payload)
      throws IOException;
}
