package org.nervos.api.mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.TransferPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.req.payload.TransferPayload;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

import java.io.IOException;

public class BuildTransferTransactionTest {

  @Test
  void testBuildTransferTransaction() throws IOException {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.setAssetInfo(AssetInfo.newCkbAsset());
    builder.addFrom(ItemFactory.newAddressItem("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqfqyerlanzmnkxtmd9ww9n7gr66k8jt4tclm9jnk"));
    builder.addTo("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf"
        , 100);
    builder.setPayFee(TransferPayload.CapacityProvider.FROM);
    builder.setFeeRate(1100L);

    TransactionCompletionResponse s =
        ApiFactory.getApi().buildTransferTransaction(builder.build());
    Assertions.assertNotNull(s.txView);
    Assertions.assertNotNull(s.scriptGroups);
  }
}
