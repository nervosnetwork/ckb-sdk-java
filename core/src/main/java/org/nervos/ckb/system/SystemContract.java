package org.nervos.ckb.system;

import java.io.IOException;
import org.nervos.ckb.crypto.Hash;
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
    Transaction transaction0 = block.transactions.get(0);
    Transaction transaction1 = block.transactions.get(1);
    if (transaction0 == null || transaction1 == null) {
      throw new IOException("Genesis block transactions not found");
    }
    return new SystemScriptCell(
        Hash.blake2b(transaction0.outputsData.get(1)), new OutPoint(transaction1.hash, "0"));
  }
}
