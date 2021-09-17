// package mercury;
//
// import com.google.gson.Gson;
// import com.google.gson.GsonBuilder;
// import mercury.constant.AddressWithKeyHolder;
// import mercury.constant.MercuryApiFactory;
// import mercury.constant.UdtHolder;
// import org.junit.jupiter.api.Test;
// import org.nervos.mercury.MercuryApi;
// import org.nervos.mercury.model.GetBalancePayloadBuilder;
// import org.nervos.mercury.model.common.AssetInfo;
// import org.nervos.mercury.model.req.item.Address;
// import org.nervos.mercury.model.req.item.Item;
// import org.nervos.mercury.model.resp.GetBalanceResponse;
//
// import java.io.IOException;
//
// import static org.junit.jupiter.api.Assertions.assertNotNull;
//
// public class BalanceTest {
//
//  Gson g = new GsonBuilder().create();
//
//  @Test
//  void getCkbBalance() {
//    try {
//
//      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
//      builder.item(new Address(AddressWithKeyHolder.testAddress4()));
//      builder.addAssetInfo(AssetInfo.newCkbAsset());
//
//      System.out.println(g.toJson(builder.build()));
//
//      GetBalanceResponse balance = MercuryApiFactory.getApi().getBalance(builder.build());
//      assertNotNull(balance, "Balance is not empty");
//      System.out.println(g.toJson(balance));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  void getSudtBalance() {
//    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
//    builder.item(new Address(AddressWithKeyHolder.testAddress4()));
//    builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
//
//    System.out.println(g.toJson(builder.build()));
//
//    try {
//      GetBalanceResponse balance = MercuryApiFactory.getApi().getBalance(builder.build());
//      assertNotNull(balance, "Balance is not empty");
//      System.out.println(g.toJson(balance));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  void getAllBalance() {
//
//    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
//    builder.item(new Address(AddressWithKeyHolder.testAddress4()));
//
//    System.out.println(g.toJson(builder.build()));
//
//    try {
//      GetBalanceResponse balance = MercuryApiFactory.getApi().getBalance(builder.build());
//      assertNotNull(balance, "Balance is not empty");
//      System.out.println(g.toJson(balance));
//      System.out.println(balance.balances.size());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  void getBalanceByAddress() {
//
//    try {
//      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
//      builder.item(new Address(AddressWithKeyHolder.testAddress4()));
//      builder.addAssetInfo(AssetInfo.newCkbAsset());
//
//      System.out.println(g.toJson(builder.build()));
//
//      GetBalanceResponse balance = MercuryApiFactory.getApi().getBalance(builder.build());
//      assertNotNull(balance, "Balance is not empty");
//      System.out.println(g.toJson(balance));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  void getBalanceByIdentity() {
//
//    try {
//      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
//      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testAddress4()));
//      builder.addAssetInfo(AssetInfo.newCkbAsset());
//
//      System.out.println(g.toJson(builder.build()));
//
//      GetBalanceResponse balance = MercuryApiFactory.getApi().getBalance(builder.build());
//      assertNotNull(balance, "Balance is not empty");
//      System.out.println(g.toJson(balance));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  void getBalanceByRecord() {
//
//    try {
//
//
//
//      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
//      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testAddress4()));
//      builder.addAssetInfo(AssetInfo.newCkbAsset());
//
//      System.out.println(g.toJson(builder.build()));
//
//      GetBalanceResponse balance = MercuryApiFactory.getApi().getBalance(builder.build());
//      assertNotNull(balance, "Balance is not empty");
//      System.out.println(g.toJson(balance));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
// }
