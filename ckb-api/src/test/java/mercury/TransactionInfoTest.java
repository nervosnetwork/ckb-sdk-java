package mercury;

import com.google.gson.Gson;
import constant.ApiFactory;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.resp.GetTransactionInfoResponse;

public class TransactionInfoTest {

  @Test
  void testGetTransactionInfo() {
    try {
      GetTransactionInfoResponse transactionInfo =
          ApiFactory.getApi()
              .getTransactionInfo(
                  "0x4329e4c751c95384a51072d4cbc9911a101fd08fc32c687353d016bf38b8b22c");

      System.out.println(new Gson().toJson(transactionInfo));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
