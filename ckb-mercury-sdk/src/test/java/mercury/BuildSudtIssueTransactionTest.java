package mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.mercury.model.SudtIssuePayloadBuilder;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.req.payload.CapacityProvider;

import java.io.IOException;
import java.math.BigInteger;

public class BuildSudtIssueTransactionTest {

  @Test
  void testBuildSudtIssueTransaction() throws IOException {
    SudtIssuePayloadBuilder builder = new SudtIssuePayloadBuilder();
    String secp256SigHashAddress = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf";
    builder.owner(secp256SigHashAddress);
    builder.addTo("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg6flmrtx8y8tuu6s3jf2ahv4l6sjw9hsc3t4tqv", BigInteger.valueOf(1));
    builder.outputCapacityProvider(CapacityProvider.FROM);
    Item item = ItemFactory.newAddressItem(secp256SigHashAddress);
    builder.addFrom(item);

    TransactionWithScriptGroups s =
        ApiFactory.getApi().buildSudtIssueTransaction(builder.build());
    Assertions.assertNotNull(s.txView);
    Assertions.assertNotNull(s.scriptGroups);
  }
}
