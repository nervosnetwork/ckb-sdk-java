package mercury;

import com.google.gson.Gson;
import model.CreateWalletPayload;
import model.TransferPayload;
import model.resp.TransferCompletionResponse;
import org.nervos.ckb.service.RpcService;

import java.io.IOException;
import java.util.Arrays;

public class DefaultMercuryApi implements MercuryApi {

    private RpcService rpcService;

    private Gson gson = new Gson();

    public DefaultMercuryApi(String mercuryUrl, boolean isDebug) {
        this.rpcService = new RpcService(mercuryUrl, isDebug);
    }

    @Override
    public String getCkbBalance(String address) throws IOException {
        return this.rpcService.post(RpcMethods.GET_CKB_BALANCE, Arrays.asList(address), String.class);
    }

    @Override
    public String getSudtBalance(String sudtHash, String address) throws IOException {
        return this.rpcService.post(RpcMethods.GET_SUDT_BALANCE, Arrays.asList(sudtHash, address), String.class);
    }

    @Override
    public TransferCompletionResponse transferCompletion(TransferPayload payload) throws IOException {
        return this.rpcService.post(RpcMethods.TRANSFER_COMPLETION, Arrays.asList(payload), TransferCompletionResponse.class);
    }

    @Override
    public TransferCompletionResponse createWallet(CreateWalletPayload payload) throws IOException {
        return this.rpcService.post(RpcMethods.CREATE_WALLET, Arrays.asList(payload), TransferCompletionResponse.class);
    }

//    @Override
//    public String getXudtBalance(String sudtHash, String address) {
//        return null;
//    }
}
