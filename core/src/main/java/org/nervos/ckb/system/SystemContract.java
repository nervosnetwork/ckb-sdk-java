package org.nervos.ckb.system;

import java.io.IOException;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.methods.type.CellOutPoint;
import org.nervos.ckb.methods.type.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.utils.Network;

public class SystemContract {

  public static SystemScriptCell getSystemScriptCell(CKBService ckbService, Network network)
      throws IOException {
    if (network == Network.TESTNET) {
      Transaction sysContractTx =
          ckbService.getBlockByNumber("0").send().getBlock().transactions.get(0);
      return new SystemScriptCell(
          Hash.blake2b(sysContractTx.outputs.get(1).data),
          new CellOutPoint(sysContractTx.hash, "1"));
    } else {
      return null;
    }
  }
}
