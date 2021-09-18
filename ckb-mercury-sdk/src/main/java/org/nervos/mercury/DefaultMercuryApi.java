package org.nervos.mercury;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.nervos.ckb.service.RpcService;
import org.nervos.mercury.model.common.AssetType;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.common.RecordStatus;
import org.nervos.mercury.model.common.ViewType;
import org.nervos.mercury.model.req.Source;
import org.nervos.mercury.model.req.payload.AdjustAccountPayload;
import org.nervos.mercury.model.req.payload.DepositPayload;
import org.nervos.mercury.model.req.payload.GetBalancePayload;
import org.nervos.mercury.model.req.payload.GetBlockInfoPayload;
import org.nervos.mercury.model.req.payload.QueryTransactionsPayload;
import org.nervos.mercury.model.req.payload.TransferPayload;
import org.nervos.mercury.model.req.payload.WithdrawPayload;
import org.nervos.mercury.model.resp.AddressOrLockHash;
import org.nervos.mercury.model.resp.BlockInfoResponse;
import org.nervos.mercury.model.resp.GetBalanceResponse;
import org.nervos.mercury.model.resp.GetTransactionInfoResponse;
import org.nervos.mercury.model.resp.RecordResponse;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import org.nervos.mercury.model.resp.TransactionInfo;
import org.nervos.mercury.model.resp.TransactionView;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;

public class DefaultMercuryApi implements MercuryApi {

  private RpcService rpcService;
  private Gson g =
      new GsonBuilder()
          .registerTypeAdapter(AddressOrLockHash.class, new AddressOrLockHash())
          .registerTypeAdapter(RecordStatus.class, new RecordStatus())
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

    GetBalanceResponse resp =
        this.rpcService.post(
            RpcMethods.GET_BALANCE, Arrays.asList(payload), GetBalanceResponse.class, g);

    return resp;
  }

  @Override
  public TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException {

    if (Objects.equals(payload.assetInfo.assetType, AssetType.CKB)
        && Objects.equals(payload.from.source, Source.Claimable)) {
      throw new RuntimeException("The transaction does not support ckb");
    }

    return this.rpcService.post(
        RpcMethods.BUILD_TRANSFER_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public TransactionCompletionResponse buildAdjustAccountTransaction(AdjustAccountPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_ADJUST_ACCOUNT_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  //  @Override
  //  public TransactionCompletionResponse buildSmartTransferTransaction(SmartTransferPayload
  // payload)
  //      throws IOException {
  //    return this.buildTransferTransaction(toTransferPayload(payload));
  //  }

  @Override
  public GetTransactionInfoResponse getTransactionInfo(String txHash) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_TRANSACTION_INFO,
        Arrays.asList(txHash),
        GetTransactionInfoResponse.class,
        g);
  }

  @Override
  public BlockInfoResponse getBlockInfo(GetBlockInfoPayload payload) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_BLOCK_INFO, Arrays.asList(payload), BlockInfoResponse.class, g);
  }

  @Override
  public List<String> registerAddresses(List<String> normalAddresses) throws IOException {
    return this.rpcService.post(
        RpcMethods.REGISTER_ADDRESSES,
        Arrays.asList(normalAddresses),
        new TypeToken<List<String>>() {}.getType());
  }

  @Override
  public PaginationResponse<TransactionView> queryTransactionsWithTransactionView(
      QueryTransactionsPayload payload) throws IOException {
    payload.viewType = ViewType.Native;
    return this.rpcService.post(
        RpcMethods.QUERY_TRANSACTIONS,
        Arrays.asList(payload),
        new TypeToken<PaginationResponse<TransactionView>>() {}.getType(),
        this.g);
  }

  @Override
  public PaginationResponse<TransactionInfo> queryTransactionsWithTransactionInfo(
      QueryTransactionsPayload payload) throws IOException {
    payload.viewType = ViewType.DoubleEntry;
    return this.rpcService.post(
        RpcMethods.QUERY_TRANSACTIONS,
        Arrays.asList(payload),
        new TypeToken<PaginationResponse<TransactionInfo>>() {}.getType(),
        this.g);
  }

  @Override
  public DBInfo getDbInfo() throws IOException {
    return this.rpcService.post(RpcMethods.GET_DB_INFO, Arrays.asList(), DBInfo.class);
  }

  @Override
  public MercuryInfo getMercuryInfo() throws IOException {
    return this.rpcService.post(RpcMethods.GET_MERCURY_INFO, Arrays.asList(), MercuryInfo.class);
  }

  @Override
  public TransactionCompletionResponse buildDepositTransaction(DepositPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DEPOSIT_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public TransactionCompletionResponse buildWithdrawTransaction(WithdrawPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_WITHDRAW_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  //  @Override
  //  public Integer getAccountNumber(String address) throws IOException {
  //    return this.rpcService.post(
  //        RpcMethods.GET_ACCOUNT_NUMBER, Arrays.asList(address), Integer.class);
  //  }

  //  private TransferPayload toTransferPayload(SmartTransferPayload payload) throws IOException {
  //
  //    List<GetBalanceResponse> fromBalances = this.getBalance(payload.from, payload.assetInfo);
  //    List<GetBalanceResponse> toBalance =
  //        this.getBalance(
  //            payload.to.stream().map(x -> x.address).collect(toList()), payload.assetInfo);
  //
  //    feePay(payload, fromBalances, toBalance);
  //    Source source = getSource(payload.assetInfo, fromBalances, payload.to);
  //
  //    TransferPayloadBuilder builder = new TransferPayloadBuilder();
  //    builder.from(new FromKeyAddresses(payload.from.stream().collect(toSet()), source));
  //    for (SmartTo to : payload.to) {
  //      if (Objects.equals(payload.assetInfo.assetType, AssetInfo.AssetType.UDT)
  //          && this.getAccountNumber(to.address).compareTo(0) > 0) {
  //        builder.addItem(new ToKeyAddress(to.address, Action.pay_by_to), to.amount);
  //      } else {
  //        builder.addItem(new ToKeyAddress(to.address, Action.pay_by_from), to.amount);
  //      }
  //    }
  //
  //    if (Objects.equals(payload.assetInfo.assetType, AssetInfo.AssetType.UDT)) {
  //      builder.udtHash(payload.assetInfo.udtHash);
  //    }
  //
  //    System.out.println(new Gson().toJson(builder.build()));
  //    return builder.build();
  //  }
  //
  //  private Source getSource(
  //      AssetInfo assetInfo, List<GetBalanceResponse> fromBalances, List<SmartTo> to) {
  //    BigInteger fromBalance = this.getBalance(fromBalances, assetInfo.assetType, "claimable");
  //    BigInteger totalAmount =
  //        to.stream().map(x -> x.amount).reduce(BigInteger.ZERO, (x, y) -> x.add(y));
  //
  //    if (fromBalance.compareTo(totalAmount) >= 0) {
  //      return Source.fleeting;
  //    } else {
  //      return Source.unconstrained;
  //    }
  //  }
  //
  //  private void feePay(
  //      SmartTransferPayload payload,
  //      List<GetBalanceResponse> fromBalances,
  //      List<GetBalanceResponse> toBalances) {
  //    BigInteger from = this.getBalance(fromBalances, AssetInfo.AssetType.CKB, "free");
  //    BigInteger to = this.getBalance(toBalances, AssetInfo.AssetType.CKB, "free");
  //
  //    BigInteger feeThreshold = AmountUtils.ckbToShannon(0.0001);
  //    if (from.compareTo(feeThreshold) < 0 && to.compareTo(feeThreshold) < 0) {
  //      throw new RuntimeException("CKB Insufficient balance to pay the fee");
  //    }
  //
  //    if (from.compareTo(feeThreshold) < 0 && to.compareTo(feeThreshold) >= 0) {
  //      payload.from.addAll(
  //          toBalances.stream()
  //              .flatMap(x -> x.balances.stream().map(y -> y.address))
  //              .collect(toSet()));
  //
  //      return;
  //    }
  //
  //    if (from.compareTo(feeThreshold) > 0) {
  //      return;
  //    }
  //
  //    throw new RuntimeException("CKB Insufficient balance to pay the fee");
  //  }
  //
  //  private BigInteger getBalance(
  //      List<GetBalanceResponse> fromBalances, AssetInfo.AssetType assetType, String balanceType)
  // {
  //    return fromBalances.stream()
  //        .map(
  //            x -> {
  //              BalanceResponse balanceResponse =
  //                  x.balances.stream()
  //                      .filter(y -> Objects.equals(y.assetInfo.assetType, assetType))
  //                      .findAny()
  //                      .get();
  //
  //              if (Objects.equals(balanceType, "free")) {
  //                return balanceResponse.free;
  //              }
  //
  //              if (Objects.equals(balanceType, "claimable")) {
  //                return balanceResponse.claimable;
  //              }
  //
  //              if (Objects.equals(balanceType, "freezed")) {
  //                return balanceResponse.freezed;
  //              }
  //              return BigInteger.ZERO;
  //            })
  //        .reduce(BigInteger.ZERO, (x, y) -> x.add(y));
  //  }
  //
  //  private List<GetBalanceResponse> getBalance(List<String> addresses, AssetInfo udtAsset)
  //      throws IOException {
  //    List<GetBalanceResponse> result = new ArrayList<>(addresses.size());
  //    for (String address : addresses) {
  //      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
  //      builder.addressItem(address);
  //      builder.addAssetInfo(AssetInfo.newCkbAsset());
  //      if (Objects.nonNull(udtAsset)) {
  //        builder.addAssetInfo(udtAsset);
  //      }
  //
  //      GetBalanceResponse balance = this.getBalance(builder.build());
  //      result.add(balance);
  //    }
  //
  //    return result;
  //  }
}
