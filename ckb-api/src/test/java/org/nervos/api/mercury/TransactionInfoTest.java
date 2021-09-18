package org.nervos.api.mercury;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constant.ApiFactory;
import java.io.IOException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.common.*;
import org.nervos.mercury.model.resp.AddressOrLockHash;
import org.nervos.mercury.model.resp.GetTransactionInfoResponse;
import org.nervos.mercury.model.resp.RecordResponse;

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

  @Test
  void testDaoInfo() {
    Gson g =
        new GsonBuilder()
            .registerTypeAdapter(AddressOrLockHash.class, new AddressOrLockHash())
            .registerTypeAdapter(RecordStatus.class, new RecordStatus())
            .registerTypeAdapter(ExtraFilter.class, new ExtraFilter())
            .registerTypeAdapter(RecordResponse.class, new RecordResponse())
            .create();

    DaoInfo withdrawInfo = new DaoInfo();
    withdrawInfo.depositBlockNumber = BigInteger.ONE;
    withdrawInfo.withdrawBlockNumber = BigInteger.ZERO;
    withdrawInfo.state = DaoState.Withdraw;
    withdrawInfo.reward = new BigInteger("1");

    ExtraFilter withdrawFilter = new ExtraFilter();
    withdrawFilter.daoInfo = withdrawInfo;
    withdrawFilter.type = ExtraFilterType.Dao;

    DaoInfo depositInfo = new DaoInfo();
    depositInfo.depositBlockNumber = BigInteger.ONE;
    depositInfo.state = DaoState.Deposit;
    depositInfo.reward = new BigInteger("1");

    ExtraFilter depositFilter = new ExtraFilter();
    depositFilter.daoInfo = depositInfo;
    depositFilter.type = ExtraFilterType.Dao;

    String withdrawJson = g.toJson(withdrawFilter);
    String depositJson = g.toJson(depositFilter);

    g.fromJson(withdrawJson, ExtraFilter.class);
    g.fromJson(depositJson, ExtraFilter.class);

    System.out.println(withdrawJson);
    System.out.println(depositJson);
  }
}
