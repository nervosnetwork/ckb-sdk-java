package org.nervos.ckb.system;

import java.io.IOException;
import org.nervos.ckb.methods.type.Block;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.system.type.SystemScriptCell;

public class SystemContract {

  public static SystemScriptCell getSystemScriptCell(CKBService ckbService) throws IOException {
    Block block = ckbService.getBlockByNumber("0").send().getBlock();
    if (block == null) {
      throw new IOException("Genesis block not found");
    }
    Transaction transaction = block.transactions.get(1);
    if (transaction == null) {
      throw new IOException("Genesis block second transaction not found");
    }
    return new SystemScriptCell(
        transaction.outputs.get(0).lock.scriptHash(), new OutPoint(transaction.hash, "0"));
  }
}
