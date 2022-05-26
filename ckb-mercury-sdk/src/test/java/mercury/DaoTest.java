package mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.DaoClaimPayloadBuilder;
import org.nervos.mercury.model.DaoDepositPayloadBuilder;
import org.nervos.mercury.model.DaoWithdrawPayloadBuilder;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

import java.io.IOException;

public class DaoTest {

  @Test
  public void testBuildDaoDeposit() throws IOException {
    DaoDepositPayloadBuilder builder = new DaoDepositPayloadBuilder();
    builder.setFrom(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    builder.setTo(AddressWithKeyHolder.testAddress1());
    builder.setAmount(20000000000L);
    builder.setFeeRate(1200L);

    TransactionCompletionResponse tx =
        ApiFactory.getApi().buildDaoDepositTransaction(builder.build());
    Assertions.assertNotNull(tx.txView);
    Assertions.assertNotNull(tx.scriptGroups);
  }

  @Test
  public void testBuildDaoWithdraw() throws IOException {
    DaoWithdrawPayloadBuilder builder = new DaoWithdrawPayloadBuilder();
    builder.setFrom(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    builder.setPayFee(AddressWithKeyHolder.testAddress1());
    builder.setFeeRate(1200L);

    TransactionCompletionResponse tx =
        ApiFactory.getApi().buildDaoWithdrawTransaction(builder.build());
    Assertions.assertNotNull(tx.txView);
    Assertions.assertNotNull(tx.scriptGroups);
  }

  @Test
  public void testBuildDaoClaim() throws IOException {
    DaoClaimPayloadBuilder builder = new DaoClaimPayloadBuilder();
    builder.setFrom(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    TransactionCompletionResponse tx =
        ApiFactory.getApi().buildDaoClaimTransaction(builder.build());
    Assertions.assertNotNull(tx.txView);
    Assertions.assertNotNull(tx.scriptGroups);
  }
}
