package mercury;

import mercury.constant.AddressWithKeyHolder;
import mercury.constant.MercuryApiHolder;
import model.resp.GetBalanceResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceTest {

  @Test
  void getBalance() {
    try {
      GetBalanceResponse balance =
          MercuryApiHolder.getApi().getBalance(null, AddressWithKeyHolder.testAddress0());
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
          MercuryApiHolder.getApi()
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
