import java.util.List;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.signature.ScriptGroup;
import org.nervos.ckb.signature.ScriptSignerManager;
import org.nervos.ckb.signature.TransactionTemplate;
import org.nervos.ckb.signature.scriptSigner.Secp256k1Blake160ScriptSigner;

public class ScriptSignerTest {

  @Test
  void example() {

    TransactionTemplate txTemplate = new TransactionTemplate();
    ScriptSignerManager scriptSignerManager = new ScriptSignerManager();
    List<ScriptGroup> scriptGroups = txTemplate.getScriptGroups();

    // You need to register different ScriptSigner for different address
    // even though they are using the same script.
    scriptSignerManager
        .register(
                scriptGroups.get(1).getScript(), new Secp256k1Blake160ScriptSigner("0x000000000"))
        .register(
                scriptGroups.get(2).getScript(), new Secp256k1Blake160ScriptSigner("0x123456789"));

    scriptSignerManager.signTx(txTemplate);

    // Send transaction ...
  }
}
