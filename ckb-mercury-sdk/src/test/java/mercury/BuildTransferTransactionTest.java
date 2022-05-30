package mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.mercury.model.TransferPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.req.payload.CapacityProvider;

import java.io.IOException;
import java.math.BigInteger;

public class BuildTransferTransactionTest {

  @Test
  void testBuildTransferTransaction() throws IOException {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(AssetInfo.newCkbAsset());
    builder.addFrom(ItemFactory.newAddressItem("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqfqyerlanzmnkxtmd9ww9n7gr66k8jt4tclm9jnk"));
    builder.addTo("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf"
        , BigInteger.valueOf(100));
    builder.payFee(CapacityProvider.FROM);
    builder.feeRate(1100L);

    TransactionWithScriptGroups s =
        ApiFactory.getApi().buildTransferTransaction(builder.build());
    Assertions.assertNotNull(s.txView);
    Assertions.assertNotNull(s.scriptGroups);
    Assertions.assertNotNull(s.scriptGroups.get(0).getGroupType());
    Assertions.assertNotNull(s.scriptGroups.get(0).getScript());
    Assertions.assertNotNull(s.scriptGroups.get(0).getOutputIndices());
    Assertions.assertNotNull(s.scriptGroups.get(0).getInputIndices());
  }

}
