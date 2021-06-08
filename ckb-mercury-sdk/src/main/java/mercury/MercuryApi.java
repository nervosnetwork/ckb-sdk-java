package mercury;

import model.CreateWalletPayload;
import model.TransferPayload;
import model.resp.GetBalanceResponse;
import model.resp.TransferCompletionResponse;

import java.io.IOException;

public interface MercuryApi {

    GetBalanceResponse getBalance(String sudtHash, String address) throws IOException;

    TransferCompletionResponse transferCompletion(TransferPayload payload) throws IOException;

    TransferCompletionResponse createWallet(CreateWalletPayload payload) throws IOException;


}
