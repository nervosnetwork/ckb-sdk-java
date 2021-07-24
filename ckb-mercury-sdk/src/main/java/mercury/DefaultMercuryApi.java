package mercury;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nervos.ckb.service.RpcService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import model.CreateAssetAccountPayload;
import model.GetBalancePayload;
import model.GetGenericBlockPayload;
import model.KeyAddress;
import model.NormalAddress;
import model.QueryAddress;
import model.ToKeyAddress;
import model.TransferItem;
import model.TransferPayload;
import model.resp.GenericBlockResponse;
import model.resp.GenericTransactionWithStatusResponse;
import model.resp.GetBalanceResponse;
import model.resp.TransactionCompletionResponse;

import static java.util.stream.Collectors.toList;

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
    List<TransferItem> transferItems =
        payload.items.stream()
            .filter(x -> Objects.equals(x.to.getClass(), ToKeyAddress.class))
            .collect(toList());
    if (transferItems.size() > 0) {
      if (payload.items.stream().anyMatch(item -> !item.to.isPayByFrom())
          && (payload.udtHash == null || payload.udtHash == "")) {
        throw new RuntimeException("The transaction does not support ckb");
      }
    }

    return this.rpcService.post(
        RpcMethods.BUILD_TRANSFER_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public TransactionCompletionResponse buildAssetAccountCreationTransaction(
      CreateAssetAccountPayload payload) throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_ASSET_ACCOUNT_CREATION_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public GenericTransactionWithStatusResponse getGenericTransaction(String txHash)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_GENERIC_TRANSACTION,
        Arrays.asList(txHash),
        GenericTransactionWithStatusResponse.class);
  }

  @Override
  public GenericBlockResponse getGenericBlock(GetGenericBlockPayload payload) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_GENERIC_BLOCK, Arrays.asList(payload), GenericBlockResponse.class);
  }
}
