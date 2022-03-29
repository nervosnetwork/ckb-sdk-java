package org.nervos.ckb.system;

import java.io.IOException;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;

public class SystemContract {

  private static List<Transaction> getSystemCellTransactions(Api api) throws IOException {
    Block block = api.getBlockByNumber("0x0");
    if (block == null) {
      throw new IOException("Genesis block not found");
    }
    if (block.transactions == null || block.transactions.size() < 2) {
      throw new IOException("Genesis block transactions system script not found");
    }
    return block.transactions;
  }

  public static SystemScriptCell getSystemSecpCell(Api api) throws IOException {
    List<Transaction> transactions = getSystemCellTransactions(api);
    return new SystemScriptCell(
        transactions.get(0).outputs.get(1).type.computeHash(),
        new OutPoint(Numeric.hexStringToByteArray(transactions.get(1).hash), 0));
  }

  public static SystemScriptCell getSystemMultiSigCell(Api api) throws IOException {
    List<Transaction> transactions = getSystemCellTransactions(api);
    return new SystemScriptCell(
        transactions.get(0).outputs.get(4).type.computeHash(),
        new OutPoint(Numeric.hexStringToByteArray(transactions.get(1).hash), 1));
  }

  public static SystemScriptCell getSystemNervosDaoCell(Api api) throws IOException {
    List<Transaction> transactions = getSystemCellTransactions(api);
    return new SystemScriptCell(
        transactions.get(0).outputs.get(2).type.computeHash(),
        new OutPoint(Numeric.hexStringToByteArray(transactions.get(0).hash), 2));
  }
}
