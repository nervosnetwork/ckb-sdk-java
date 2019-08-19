package org.nervos.ckb.system;

import java.io.IOException;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.transaction.Transaction;
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
          Hash.blake2b(sysContractTx.outputsData.get(1)), new OutPoint(sysContractTx.hash, "1"));
    }
    return null;
  }
}
