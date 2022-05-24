package org.nervos.api.mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.SimpleTransferPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

import java.io.IOException;

public class BuildSimpleTransferTransactionTest {

  @Test
  void testBuildTransferTransaction() throws IOException {
    SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
    builder.setAssetInfo(AssetInfo.newCkbAsset());
    builder.addFrom("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqfqyerlanzmnkxtmd9ww9n7gr66k8jt4tclm9jnk");
    builder.addTo("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf"
        , 10000000000L);
    builder.setFeeRate(500L);

    TransactionCompletionResponse s =
        ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());
    Assertions.assertNotNull(s.txView);
    Assertions.assertNotNull(s.scriptGroups);
  }
}
