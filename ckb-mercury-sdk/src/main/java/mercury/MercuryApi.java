package mercury;

import model.TransferPayload;
import model.resp.TransferCompletionResponse;

import java.io.IOException;

public interface MercuryApi {
    String getCkbBalance(String address) throws IOException;
    String getSudtBalance(String sudtHash, String address) throws IOException;
//    String getXudtBalance(String sudtHash, String address);

    TransferCompletionResponse transferCompletion(TransferPayload payload) throws IOException;
}
