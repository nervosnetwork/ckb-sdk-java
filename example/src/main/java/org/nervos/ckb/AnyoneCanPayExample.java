package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.acp.AnyoneCanPayCellCollector;
import org.nervos.ckb.acp.AnyoneCanPayCollectResult;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellOutput;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class AnyoneCanPayExample {

  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static final String ANYONE_CAN_PAY_CODE_HASH =
      "0x6a3982f9d018be7e7228f9e0b765f28ceff6d36e634490856d2b186acf78e79b";
  private static final String ANYONE_CAN_PAY_OUT_POINT_TX_HASH =
      "0x69c70d65832cdfd97fe78d32eb25f840232f6b8cb6445464f11dad891b11fd83";
  private static final String ANYONE_CAN_PAY_MIN_CKB = "09";
  private static final String ReceivePrivateKey =
      "d00c06bfd800d27397002dca6fb0993d5ba6399b4238b2f29ee9deb97593d2bc";
  private static final String ReceiveAddress = "ckt1qyqvsv5240xeh85wvnau2eky8pwrhh4jr8ts8vyj37";
  private static Script ReceiveAnyoneCanPayLock;
  private static final String TestPrivateKey =
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
  private static final String TestAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";

  private static final String NODE_URL = "http://localhost:8114";
  private static Api api;

  static {
    api = new Api(NODE_URL, false);
    ReceiveAnyoneCanPayLock =
        LockUtils.generateLockScriptWithPrivateKey(ReceivePrivateKey, ANYONE_CAN_PAY_CODE_HASH);
    ReceiveAnyoneCanPayLock.args += ANYONE_CAN_PAY_MIN_CKB;
  }

  public static void main(String[] args) throws Exception {
    //    String txHash = createReceiverAnyoneCanPayCell(BigInteger.valueOf(200).multiply(UnitCKB));
    //    System.out.println("Create anyone can pay cell tx hash: " + txHash);
    //
    //    Thread.sleep(30000);

    String hash = transfer(BigInteger.valueOf(11).multiply(UnitCKB));
    System.out.println("Transfer anyone-can-pay lock tx hash: " + hash);
  }

  private static String createReceiverAnyoneCanPayCell(BigInteger capacity) throws IOException {

    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    Receiver receiver = new Receiver(ReceiveAddress, capacity);
    List<CellOutput> cellOutputs =
        txUtils.generateOutputs(Collections.singletonList(receiver), ReceiveAddress);

    cellOutputs.get(0).lock = ReceiveAnyoneCanPayLock;
    List<String> cellOutputsData = Arrays.asList("0x", "0x");

    txBuilder.addOutputs(cellOutputs);
    txBuilder.setOutputsData(cellOutputsData);
    txBuilder.addCellDep(
        new CellDep(new OutPoint(ANYONE_CAN_PAY_OUT_POINT_TX_HASH, "0x0"), CellDep.DEP_GROUP));

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    CollectResult collectResult =
        txUtils.collectInputs(
            Collections.singletonList(ReceiveAddress),
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
              Collections.singletonList(ReceivePrivateKey)));
    }

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }
    return api.sendTransaction(signBuilder.buildTx());
  }

  private static String transfer(BigInteger capacity) throws IOException {
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();

    TransactionBuilder txBuilder = new TransactionBuilder(api);
    CollectUtils txUtils = new CollectUtils(api);

    AnyoneCanPayCollectResult anyoneCanPayCellResult =
        new AnyoneCanPayCellCollector(api).collectCell(ReceiveAddress);

    Receiver receiver = new Receiver(ReceiveAddress, capacity.add(anyoneCanPayCellResult.capacity));
    List<CellOutput> cellOutputs =
        txUtils.generateOutputs(Collections.singletonList(receiver), TestAddress);

    cellOutputs.get(0).lock = ReceiveAnyoneCanPayLock;
    List<String> cellOutputsData = Arrays.asList("0x", "0x");

    txBuilder.addOutputs(cellOutputs);
    txBuilder.setOutputsData(cellOutputsData);
    txBuilder.addCellDep(
        new CellDep(new OutPoint(ANYONE_CAN_PAY_OUT_POINT_TX_HASH, "0x0"), CellDep.DEP_GROUP));

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = 2 * secp256k1_signature_byte.length
    CollectResult collectResult =
        txUtils.collectInputs(
            Collections.singletonList(TestAddress),
            txBuilder.buildTx(),
            feeRate,
            Sign.SIGN_LENGTH * 2,
            anyoneCanPayCellResult.capacity);

    // update change output capacity after collecting cells
    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    cellOutputs.add(cellOutputs.get(0));
    cellOutputs.remove(0);
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
    txBuilder.addInput(anyoneCanPayCellResult.cellInput);
    txBuilder.addWitness("0x");

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }

    return api.sendTransaction(signBuilder.buildTx());
  }
}
