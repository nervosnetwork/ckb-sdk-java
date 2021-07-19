package mercury;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Arrays;
import model.Action;
import model.CreateWalletPayload;
import model.GetBalancePayload;
import model.KeyAddress;
import model.NormalAddress;
import model.QueryAddress;
import model.TransferPayload;
import model.resp.GetBalanceResponse;
import model.resp.TransactionCompletionResponse;
import org.nervos.ckb.service.RpcService;

public class DefaultMercuryApi implements MercuryApi {

  private RpcService rpcService;

  private Gson g =
      new GsonBuilder()
          .registerTypeAdapter(QueryAddress.class, new KeyAddress(""))
          .registerTypeAdapter(QueryAddress.class, new NormalAddress(""))
          .create();

  public DefaultMercuryApi(String mercuryUrl, boolean isDebug) {
    this.rpcService = new RpcService(mercuryUrl, isDebug);
  }

  @Override
  public GetBalanceResponse getBalance(GetBalancePayload payload) throws IOException {

    return this.rpcService.post(
        RpcMethods.GET_BALANCE, Arrays.asList(payload), GetBalanceResponse.class, g);
  }

  @Override
  public TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException {
    if (payload.items.stream().anyMatch(item -> !item.to.action.equals(Action.pay_by_from))
        && (payload.udtHash == null || payload.udtHash == "")) {
      throw new RuntimeException("The transaction does not support ckb");
    }
    return this.rpcService.post(
        RpcMethods.BUILD_TRANSFER_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public TransactionCompletionResponse buildWalletCreationTransaction(CreateWalletPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_WALLET_CREATION_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }
}
