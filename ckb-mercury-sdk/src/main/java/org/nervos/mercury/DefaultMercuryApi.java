package org.nervos.mercury;

import static java.util.stream.Collectors.toList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.nervos.ckb.service.RpcService;
import org.nervos.mercury.model.req.CollectAssetPayload;
import org.nervos.mercury.model.req.CreateAssetAccountPayload;
import org.nervos.mercury.model.req.GetBalancePayload;
import org.nervos.mercury.model.req.GetGenericBlockPayload;
import org.nervos.mercury.model.req.KeyAddress;
import org.nervos.mercury.model.req.NormalAddress;
import org.nervos.mercury.model.req.QueryAddress;
import org.nervos.mercury.model.req.QueryGenericTransactionsPayload;
import org.nervos.mercury.model.req.ToKeyAddress;
import org.nervos.mercury.model.req.TransferItem;
import org.nervos.mercury.model.req.TransferPayload;
import org.nervos.mercury.model.resp.*;

public class DefaultMercuryApi implements MercuryApi {

  private RpcService rpcService;
  private Gson g =
      new GsonBuilder()
          .registerTypeAdapter(QueryAddress.class, new KeyAddress(""))
          .registerTypeAdapter(QueryAddress.class, new NormalAddress(""))
          .registerTypeAdapter(RecordResponse.class, new RecordResponse())
          .create();

  public DefaultMercuryApi(String mercuryUrl, boolean isDebug) {
    this.rpcService = new RpcService(mercuryUrl, isDebug);
  }

  public DefaultMercuryApi(RpcService rpcService) {
    this.rpcService = rpcService;
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
        payload
            .items
            .stream()
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
        GenericTransactionWithStatusResponse.class,
        g);
  }

  @Override
  public GenericBlockResponse getGenericBlock(GetGenericBlockPayload payload) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_GENERIC_BLOCK, Arrays.asList(payload), GenericBlockResponse.class, g);
  }

  @Override
  public List<String> registerAddresses(List<String> normalAddresses) throws IOException {
    return this.rpcService.post(
        RpcMethods.REGISTER_ADDRESSES,
        Arrays.asList(normalAddresses),
        new TypeToken<List<String>>() {}.getType());
  }

  @Override
  public TransactionCompletionResponse buildAssetCollectionTransaction(CollectAssetPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_ASSET_COLLECTION_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public QueryGenericTransactionsResponse queryGenericTransactions(
      QueryGenericTransactionsPayload payload) throws IOException {
    return this.rpcService.post(
        RpcMethods.QUERY_GENERIC_TRANSACTIONS,
        Arrays.asList(payload),
        QueryGenericTransactionsResponse.class);
  }
}
