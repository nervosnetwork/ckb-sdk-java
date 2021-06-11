package mercury;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.MercuryApiFactory;
import model.resp.GetBalanceResponse;
import org.junit.jupiter.api.Test;

public class BalanceTest {

  @Test
  void getBalance() {
    try {
      GetBalanceResponse balance =
          MercuryApiFactory.getApi().getBalance(null, AddressWithKeyHolder.testAddress0());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(balance.unconstrained);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void getSudtBalance() {
    try {
      GetBalanceResponse balance =
          MercuryApiFactory.getApi()
              .getBalance(
                  "0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd",
                  AddressWithKeyHolder.testAddress4());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(balance.unconstrained);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
