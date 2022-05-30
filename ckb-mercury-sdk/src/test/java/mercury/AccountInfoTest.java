package mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.AccountInfoPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.ItemFactory;

import java.io.IOException;

public class AccountInfoTest {
  @Test
  public void testGetAccountInfo() throws IOException {
    AccountInfoPayloadBuilder builder = new AccountInfoPayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.assetInfo(AssetInfo.newCkbAsset());

    org.nervos.mercury.model.resp.AccountInfo accountInfo = ApiFactory.getApi().getAccountInfo(builder.build());
    Assertions.assertNotNull(accountInfo.accountAddress);
    Assertions.assertNotNull(accountInfo.accountType);
  }
}
