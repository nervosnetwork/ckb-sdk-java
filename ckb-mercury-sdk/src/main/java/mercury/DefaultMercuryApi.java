package mercury;

import com.google.gson.Gson;
import model.Action;
import model.CreateWalletPayload;
import model.TransferPayload;
import model.resp.GetBalanceResponse;
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
    public GetBalanceResponse getBalance(String sudtHash, String address) throws IOException {
        return this.rpcService.post(RpcMethods.GET_BALANCE, Arrays.asList(sudtHash, address), GetBalanceResponse.class);
    }

    @Override
    public TransferCompletionResponse transferCompletion(TransferPayload payload) throws IOException {
        boolean b = payload.getItems().stream().anyMatch(item -> !item.getTo().getAction().equals(Action.pay_by_from));
        if(payload.getItems().stream().anyMatch(item -> !item.getTo().getAction().equals(Action.pay_by_from))
                && (payload.getUdt_hash() == null || payload.getUdt_hash() == "")) {
            throw new RuntimeException("The transaction does not support ckb");
        }
        return this.rpcService.post(RpcMethods.TRANSFER_COMPLETION, Arrays.asList(payload), TransferCompletionResponse.class);
    }

    @Override
    public TransferCompletionResponse createWallet(CreateWalletPayload payload) throws IOException {
        return this.rpcService.post(RpcMethods.CREATE_WALLET, Arrays.asList(payload), TransferCompletionResponse.class);
    }

}
