package system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;

@Disabled
public class SystemContractTest {

  @Test
  public void testGetSystemContract() throws Exception {
    SystemScriptCell systemScriptCell =
        SystemContract.getSystemSecpCell(new Api("http://localhost:8114"));
    Assertions.assertNotNull(systemScriptCell);
  }
}
