package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.fixed.UInt128;
import org.nervos.ckb.udt.UDTCellCollector;
import org.nervos.ckb.udt.UDTCollectResult;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Utils;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class SUDTExample {

  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static final String SUDT_CODE_HASH =
      "0x48dbf59b4c7ee1547238021b4869bceedf4eea6b43772e5d66ef8865b6ae7212";
  private static final String SUDT_OUT_POINT_TX_HASH =
      "0x78fbb1d420d242295f8668cb5cf38869adac3500f6d4ce18583ed42ff348fa64";
  private static final String OUTPUTS_DATE_PLACEHOLDER = "00000000000000000000000000000000";
  private static final BigInteger SUDT_MIN_CELL_CAPACITY = Utils.ckbToShannon(142);
  private static final BigInteger SUDT_ISSUE_CELL_CAPACITY = Utils.ckbToShannon(1000);
  private static final String ReceiveAddress = "ckt1qyqvsv5240xeh85wvnau2eky8pwrhh4jr8ts8vyj37";
  private static final String TestPrivateKey =
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
  private static final String TestAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";

  private static final String NODE_URL = "http://localhost:8114";
  private static Api api;

  static {
    api = new Api(NODE_URL, false);
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Tip block number: " + api.getTipBlockNumber());
    Script udtType =
        new Script(SUDT_CODE_HASH, LockUtils.generateLockHashWithAddress(TestAddress), Script.DATA);
    String hash = issue(BigInteger.valueOf(10000000000L), udtType);
    System.out.println("UDT issue hash: " + hash);

    Thread.sleep(30000);

    String balance = getUdtBalance(TestAddress, udtType.computeHash());
    System.out.println("Balance: " + balance);

    String transferHash = transfer(BigInteger.valueOf(100000L), udtType);
    System.out.println("UDT transfer hash: " + transferHash);
  }

  private static String issue(BigInteger sudtAmount, Script udtType) throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    Receiver receiver = new Receiver(TestAddress, SUDT_ISSUE_CELL_CAPACITY);
    List<CellOutput> cellOutputs =
        txUtils.generateOutputs(Collections.singletonList(receiver), TestAddress);

    cellOutputs.get(0).type = udtType;
    List<String> cellOutputsData =
        Arrays.asList(Numeric.toHexString(new UInt128(sudtAmount).toBytes()), "0x");

    txBuilder.addOutputs(cellOutputs);
    txBuilder.setOutputsData(cellOutputsData);
    txBuilder.addCellDep(new CellDep(new OutPoint(SUDT_OUT_POINT_TX_HASH, "0x0"), CellDep.CODE));

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    CollectResult collectResult =
        txUtils.collectInputs(
            Collections.singletonList(TestAddress),
            txBuilder.buildTx(),
            feeRate,
            Sign.SIGN_LENGTH * 2);

    // update change output capacity after collecting cells
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

  private static String getUdtBalance(String address, String typeHash) throws IOException {
    return new UDTCellCollector(api).getUdtBalanceWithAddress(address, typeHash).toString(10);
  }

  private static String transfer(BigInteger sudtAmount, Script udtType) throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    Receiver receiver = new Receiver(ReceiveAddress, SUDT_MIN_CELL_CAPACITY);
    List<CellOutput> cellOutputs =
        txUtils.generateOutputs(Collections.singletonList(receiver), TestAddress);

    for (CellOutput output : cellOutputs) {
      output.type = udtType;
    }
    txBuilder.addOutputs(cellOutputs);

    List<String> cellOutputsData =
        Arrays.asList(
            Numeric.toHexString(new UInt128(sudtAmount).toBytes()),
            Numeric.toHexString(new UInt128(OUTPUTS_DATE_PLACEHOLDER).toBytes()));
    txBuilder.setOutputsData(cellOutputsData);

    txBuilder.addCellDep(new CellDep(new OutPoint(SUDT_OUT_POINT_TX_HASH, "0x0"), CellDep.CODE));

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    UDTCollectResult result =
        new UDTCellCollector(api)
            .collectInputs(
                Collections.singletonList(TestAddress),
                txBuilder.buildTx(),
                feeRate,
                Sign.SIGN_LENGTH * 2,
                udtType.computeHash(),
                sudtAmount);

    // update change output capacity after collecting cells
    CellOutput changeOutput = cellOutputs.get(1);
    changeOutput.capacity = result.changeCapacity;
    cellOutputs.set(1, changeOutput);

    txBuilder.setOutputs(cellOutputs);

    cellOutputsData.set(1, Numeric.toHexString(new UInt128(result.udtAmount).toBytes()));
    txBuilder.setOutputsData(cellOutputsData);

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : result.cellsWithAddresses) {
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

  private static void burn() {}
}
