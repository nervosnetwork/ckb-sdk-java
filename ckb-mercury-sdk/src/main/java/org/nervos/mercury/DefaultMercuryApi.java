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
import org.nervos.mercury.model.req.*;
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

    GetBalanceResponse.RpcGetBalanceResponse resp =
        this.rpcService.post(
            RpcMethods.GET_BALANCE,
            Arrays.asList(payload),
            GetBalanceResponse.RpcGetBalanceResponse.class,
            g);

    GetBalanceResponse result = new GetBalanceResponse();
    result.balances =
        resp.balances
            .stream()
            .map(
                x -> {
                  BalanceResponse balance = new BalanceResponse();
                  balance.address = x.keyAddress;
                  balance.free = x.unconstrained;
                  balance.claimable = x.fleeting;
                  balance.freezed = x.locked;
                  if (Objects.isNull(x.udtHash) || x.udtHash == "") {
                    balance.assetInfo = AssetInfo.newCkbAsset();
                  } else {
                    balance.assetInfo = AssetInfo.newUdtAsset(x.udtHash);
                  }

                  return balance;
                })
            .collect(toList());

    return result;
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
