package mercury;

import model.CreateWalletPayload;
import model.TransferPayload;
import model.resp.GetBalanceResponse;
import model.resp.TransferCompletionResponse;

import java.io.IOException;

public interface MercuryApi {

  GetBalanceResponse getBalance(String udtHash, String ident) throws IOException;

  TransferCompletionResponse buildTransferTransaction(TransferPayload payload) throws IOException;

  TransferCompletionResponse buildWalletCreationTransaction(CreateWalletPayload payload)
      throws IOException;
}
