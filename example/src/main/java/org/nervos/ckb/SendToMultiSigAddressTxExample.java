package org.nervos.ckb;

import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.indexer.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.nervos.ckb.utils.Const.*;

public class SendToMultiSigAddressTxExample {

  private static Api api;
  private static CkbIndexerApi ckbIndexerApi;
  private static String MultiSigAddress = "ckt1qyqlqn8vsj7r0a5rvya76tey9jd2rdnca8lqh4kcuq";
  private static String TestPrivateKey =
      "d00c06bfd800d27397002dca6fb0993d5ba6399b4238b2f29ee9deb97593d2bc";
  private static String TestAddress =
      "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqwgx292hnvmn68xf779vmzrshpmm6epn4c0cgwga";

  static {
    api = new Api(NODE_URL, false);
    ckbIndexerApi = new CkbIndexerApi(CKB_INDEXER_URL, false);
  }

  public static void main(String[] args) throws Exception {
    List<Receiver> receivers =
        Collections.singletonList(new Receiver(MultiSigAddress, Utils.ckbToShannon(20000)));
    String changeAddress =
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqwgx292hnvmn68xf779vmzrshpmm6epn4c0cgwga";

    System.out.println("Before transferring, sender's balance: " + getBalance() + " CKB");

    System.out.println(
        "Before transferring, multi-sig address balance: " + getMultiSigBalance() + " CKB");

    System.out.println("Before transferring, change address balance: " + getBalance() + " CKB");

    byte[] hash = sendCapacity(receivers, changeAddress);
    System.out.println("Transaction hash: " + hash);

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);

    System.out.println("After transferring, sender's address balance: " + getBalance() + " CKB");

    System.out.println(
        "After transferring, multi-sig address balance: " + getMultiSigBalance() + " CKB");

    System.out.println("After transferring, change address balance: " + getBalance() + " CKB");
  }

  private static long getBalance() throws IOException {
    return Long.divideUnsigned(new IndexerCollector(api, ckbIndexerApi).getCapacity(TestAddress), UnitCKB);
  }

  private static long getMultiSigBalance() throws IOException {
    return Long.divideUnsigned(new IndexerCollector(api, ckbIndexerApi).getCapacity(MultiSigAddress), UnitCKB);
  }

  private static byte[] sendCapacity(List<Receiver> receivers, String changeAddress)
      throws IOException {
    TransactionBuilder txBuilder = new TransactionBuilder(api);
    IndexerCollector txUtils = new IndexerCollector(api, ckbIndexerApi);

    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, changeAddress);
    txBuilder.addOutputs(cellOutputs);

    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    // You can get fee rate by rpc or set a simple number
    long feeRate = 1024;

    // initial_length = 2 * secp256k1_signature_byte.length
    CollectResult collectResult =
        txUtils.collectInputs(
            Collections.singletonList(TestAddress),
            txBuilder.buildTx(),
            feeRate,
            Sign.SIGN_LENGTH * 2);

    // update change cell output capacity after collecting cells
    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    txBuilder.setOutputs(cellOutputs);

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : collectResult.cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
      }
      scriptGroupWithPrivateKeysList.add(
          new ScriptGroupWithPrivateKeys(
              new ScriptGroup(NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size())),
              Collections.singletonList(TestPrivateKey)));
    }

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }

    return api.sendTransaction(signBuilder.buildTx());
  }
}
