package mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.mercury.model.DaoClaimPayloadBuilder;
import org.nervos.mercury.model.DaoDepositPayloadBuilder;
import org.nervos.mercury.model.DaoWithdrawPayloadBuilder;
import org.nervos.mercury.model.req.item.ItemFactory;

import java.io.IOException;

public class DaoTest {

  @Test
  public void testBuildDaoDeposit() throws IOException {
    DaoDepositPayloadBuilder builder = new DaoDepositPayloadBuilder();
    builder.addFrom(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    builder.to(AddressWithKeyHolder.testAddress1());
    builder.amount(20000000000L);
    builder.feeRate(1200L);

    TransactionWithScriptGroups tx =
        ApiFactory.getApi().buildDaoDepositTransaction(builder.build());
    Assertions.assertNotNull(tx.txView);
    Assertions.assertNotNull(tx.scriptGroups);
  }

  @Test
  public void testBuildDaoWithdraw() throws IOException {
    DaoWithdrawPayloadBuilder builder = new DaoWithdrawPayloadBuilder();
    builder.addFrom(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    builder.feeRate(1200L);

    TransactionWithScriptGroups tx =
        ApiFactory.getApi().buildDaoWithdrawTransaction(builder.build());
    Assertions.assertNotNull(tx.txView);
    Assertions.assertNotNull(tx.scriptGroups);
  }

  @Test
  public void testBuildDaoClaim() throws IOException {
    DaoClaimPayloadBuilder builder = new DaoClaimPayloadBuilder();
    builder.from(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    TransactionWithScriptGroups tx =
        ApiFactory.getApi().buildDaoClaimTransaction(builder.build());
    Assertions.assertNotNull(tx.txView);
    Assertions.assertNotNull(tx.scriptGroups);
  }
}
