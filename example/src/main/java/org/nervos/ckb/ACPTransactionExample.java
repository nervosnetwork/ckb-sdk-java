package org.nervos.ckb;

import com.google.common.primitives.Bytes;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.indexer.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.MoleculeConverter;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Utils;
import org.nervos.ckb.utils.address.Address;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static org.nervos.ckb.sign.TransactionSigner.TESTNET_TRANSACTION_SIGNER;
import static org.nervos.ckb.type.WitnessArgs.SECP256K1_BLAKE160_WITNESS_BYTES_SIZE;
import static org.nervos.ckb.utils.Const.*;

// ACP RFC:
// https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0026-anyone-can-pay/0026-anyone-can-pay.md
// Before running this example, please run SUDTExample to issue a SUDT with the sender address
public class ACPTransactionExample {
  // ACP_CKB_MINIMUM is set to 9, which means in each transaction, one must at least transfers 10^9
  // shannons, or 10 CKBytes into the anyone-can-pay cell
  private static final String ACP_CKB_MINIMUM = "09";
  // ACP_SUDT_MINIMUM is set to 3, which means in each transaction, one must at least transfers 10^3
  // SUDT into the anyone-can-pay cell
  private static final String ACP_SUDT_MINIMUM = "03";

  private static Api api;
  private static CkbIndexerApi ckbIndexerApi;

  private static final List<String> SendPrivateKeys =
      Collections.singletonList("08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2");
  private static final List<String> SendAddresses =
      Collections.singletonList(
          "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtyqlpwlx7ed68pftzv69wcvr5nxxqzzus2zxwa6");
  private static final List<String> ReceiveAddresses =
      Collections.singletonList(
          "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqwgx292hnvmn68xf779vmzrshpmm6epn4c0cgwga");
  private static String receiverAcpAddress;
  public static Script sudtType;

  static {
    api = new Api(NODE_URL, false);
    ckbIndexerApi = new CkbIndexerApi(CKB_INDEXER_URL, false);

    Script receiverScript = Address.decode(ReceiveAddresses.get(0)).getScript();
    receiverScript.codeHash = ACP_CODE_HASH;
    receiverScript.args =
        Numeric.hexStringToByteArray(receiverScript.args + ACP_CKB_MINIMUM + ACP_SUDT_MINIMUM);
    receiverAcpAddress = new Address(receiverScript, Network.TESTNET).encode();

    Script senderScript = Address.decode(SendAddresses.get(0)).getScript();
    byte[] sendLockHash = senderScript.computeHash();
    sudtType = new Script(SUDT_CODE_HASH, sendLockHash, Script.HashType.TYPE);
  }

  public static void main(String[] args) throws Exception {
    System.out.println(
        "Before transferring, first sender's balance: "
            + Long.divideUnsigned(getBalance(SendAddresses.get(0)), UnitCKB)
            + " CKB");

    byte[] acpHash = createACPCell();
    System.out.println("Create acp cell tx hash: " + acpHash);

    // waiting transaction into block, sometimes you should wait more seconds
    Thread.sleep(30000);
    System.out.println(
        "Transfer acp tx hash: " + transfer(new OutPoint(acpHash, 0), BigInteger.valueOf(1000L)));
  }

  private static long getBalance(String address) throws IOException {
    return new IndexerCollector(api, ckbIndexerApi).getCapacity(address);
  }

  private static byte[] createACPCell() throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    IndexerCollector txUtils = new IndexerCollector(api, ckbIndexerApi);

    List<Receiver> receivers =
        Collections.singletonList(new Receiver(receiverAcpAddress, Utils.ckbToShannon(200)));
    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, SendAddresses.get(0));
    cellOutputs.get(0).type = sudtType;
    txBuilder.addOutputs(cellOutputs);

    List<byte[]> outputsData = new ArrayList<>();
    outputsData.add(MoleculeConverter.packUint128(BigInteger.ZERO).toByteArray());
    txBuilder.setOutputsData(outputsData);

    txBuilder.addCellDep(new CellDep(new OutPoint(ACP_TX_HASH, 0), CellDep.DepType.DEP_GROUP));
    txBuilder.addCellDep(new CellDep(new OutPoint(SUDT_TX_HASH, 0), CellDep.DepType.CODE));

    // You can get fee rate by rpc or set a simple number
    long feeRate = 1024;

    // initial_length = 2 * secp256k1_signature_byte.length
    // collectInputsWithIndexer method uses indexer rpc to collect cells quickly
    CollectResult collectResult =
        txUtils.collectInputs(SendAddresses, txBuilder.buildTx(), feeRate, SECP256K1_BLAKE160_WITNESS_BYTES_SIZE);

    // update change cell output capacity after collecting cells if there is changeOutput
    if (Long.compareUnsigned(collectResult.changeCapacity, MIN_CKB) >= 0) {
      cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
      txBuilder.setOutputs(cellOutputs);

      outputsData.add(new byte[]{});
      txBuilder.setOutputsData(outputsData);
    }

    List<org.nervos.ckb.sign.ScriptGroup> scriptGroups = new ArrayList<>();
    Set<Context> contexts = new HashSet<>();
    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : collectResult.cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        if (i == 0) {
          WitnessArgs witnessArgs = new WitnessArgs(65);
          txBuilder.addWitness(witnessArgs.pack().toByteArray());
        } else {
          txBuilder.addWitness(new byte[]{});
        }
      }
      if (cellsWithAddress.inputs.size() > 0) {
        Address address = Address.decode(cellsWithAddress.address);
        org.nervos.ckb.sign.ScriptGroup scriptGroup = new org.nervos.ckb.sign.ScriptGroup();
        scriptGroup.setScript(address.getScript());
        scriptGroup.setScriptType(ScriptType.LOCK);
        scriptGroup.setInputIndices(NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size()));

        scriptGroups.add(scriptGroup);
        contexts.add(new Context(SendPrivateKeys.get(SendAddresses.indexOf(cellsWithAddress.address))));
        startIndex += cellsWithAddress.inputs.size();
      }
    }

    TransactionWithScriptGroups transactionWithScriptGroups = new TransactionWithScriptGroups();
    transactionWithScriptGroups.setTxView(txBuilder.buildTx());
    transactionWithScriptGroups.setScriptGroups(scriptGroups);

    TESTNET_TRANSACTION_SIGNER.signTransaction(transactionWithScriptGroups, contexts);
    return api.sendTransaction(transactionWithScriptGroups.getTxView());
  }

  private static byte[] transfer(OutPoint acpOutPoint, BigInteger sudtAmount) throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    IndexerCollector txUtils = new IndexerCollector(api, ckbIndexerApi);
    BigInteger inputSUDTAmount = BigInteger.ZERO;

    List<Receiver> receivers =
        Collections.singletonList(new Receiver(receiverAcpAddress, Utils.ckbToShannon(210)));
    List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, SendAddresses.get(0));
    cellOutputs.get(0).type = sudtType;

    // There must be a change cell for sender to cover sudt change amount whose capacity is at least
    // 142 CKB
    cellOutputs.get(1).type = sudtType;
    txBuilder.addOutputs(cellOutputs);

    byte[] SUDTAmountBytes = api.getLiveCell(acpOutPoint, true).cell.data.content;
    Bytes.reverse(SUDTAmountBytes);
    BigInteger acpOutputSUDTAmount = Numeric.toBigInt(SUDTAmountBytes).add(sudtAmount);

    List<byte[]> outputsData = new ArrayList<>();
    outputsData.add(MoleculeConverter.packUint128(acpOutputSUDTAmount).toByteArray());
    txBuilder.setOutputsData(outputsData);

    txBuilder.addCellDep(new CellDep(new OutPoint(ACP_TX_HASH, 0), CellDep.DepType.DEP_GROUP));
    txBuilder.addCellDep(new CellDep(new OutPoint(SUDT_TX_HASH, 0), CellDep.DepType.CODE));

    // You can get fee rate by rpc or set a simple number
    long feeRate = 1500;

    // initial_length = 2 * secp256k1_signature_byte.length
    // collectInputsWithIndexer method uses indexer rpc to collect cells quickly
    CollectResult collectResult =
        txUtils.collectInputs(
            SendAddresses, txBuilder.buildTx(), feeRate, Sign.SIGN_LENGTH * 2, sudtType);

    // update change cell output capacity after collecting cells if there is changeOutput
    if (Long.compareUnsigned(collectResult.changeCapacity, MIN_SUDT_CKB) >= 0) {
      cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
      txBuilder.setOutputs(cellOutputs);
    } else {
      throw new IOException("The change capacity is not enough for the SUDT cell");
    }

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : collectResult.cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
        OutPoint outPoint = cellsWithAddress.inputs.get(i).previousOutput;
        byte[] cellData = api.getLiveCell(outPoint, true).cell.data.content;
        if (cellData.length < 32) continue;
        SUDTAmountBytes = Arrays.copyOfRange(cellData, 0, 32);
        Bytes.reverse(SUDTAmountBytes);
        inputSUDTAmount =
            inputSUDTAmount.add(Numeric.toBigInt(SUDTAmountBytes)); // sudt amount: 16bytes
      }
      if (cellsWithAddress.inputs.size() > 0) {
        scriptGroupWithPrivateKeysList.add(
            new ScriptGroupWithPrivateKeys(
                new ScriptGroup(
                    NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size())),
                Collections.singletonList(
                    SendPrivateKeys.get(SendAddresses.indexOf(cellsWithAddress.address)))));
        startIndex += cellsWithAddress.inputs.size();
      }
    }
    txBuilder.addWitness("0x");
    txBuilder.addInput(new CellInput(acpOutPoint));

    byte[] changeCellData = MoleculeConverter.packUint128(inputSUDTAmount.subtract(sudtAmount)).toByteArray();
    outputsData.add(changeCellData);
    txBuilder.setOutputsData(outputsData);

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }
    Transaction tx = signBuilder.buildTx();
    return api.sendTransaction(tx);
  }
}
