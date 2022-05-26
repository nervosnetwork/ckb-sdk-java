package mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.SudtIssuePayloadBuilder;
import org.nervos.mercury.model.req.payload.CapacityProvider;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

import java.io.IOException;

public class BuildSudtIssueTransactionTest {

  @Test
  void testBuildSudtIssueTransaction() throws IOException {
    SudtIssuePayloadBuilder builder = new SudtIssuePayloadBuilder();
    builder.setOwner("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf");
    builder.addTo("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg6flmrtx8y8tuu6s3jf2ahv4l6sjw9hsc3t4tqv", 1);
    builder.setOutputCapacityProvider(CapacityProvider.FROM);

    TransactionCompletionResponse s =
        ApiFactory.getApi().buildSudtIssueTransaction(builder.build());
    Assertions.assertNotNull(s.txView);
    Assertions.assertNotNull(s.txView);
  }
}
