package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellOutput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class NervosDaoExample {
  private static final int DAO_LOCK_PERIOD_EPOCHS = 180;
  private static final int DAO_MATURITY_BLOCKS = 5;

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static String MinerPrivateKey =
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
  private static String MinerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";

  static {
    api = new Api(NODE_URL, false);
  }

  private static OutPoint depositToDao(BigInteger capacity) throws IOException {
    Script lock = LockUtils.generateLockScriptWithAddress(MinerAddress);
    Script type =
        new Script(SystemContract.getSystemNervosDaoCell(api).cellHash, "0x", Script.TYPE);
    CellOutput cellOutput = new CellOutput(capacity.toString(16), lock, type);
    CellOutput changeOutput = new CellOutput("0x0", lock);
    List<CellOutput> cellOutputs = Arrays.asList(cellOutput, changeOutput);

    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();
    TransactionBuilder txBuilder = new TransactionBuilder(api);
    txBuilder.addOutputs(cellOutputs);
    txBuilder.addCellDep(
        new CellDep(SystemContract.getSystemNervosDaoCell(api).outPoint, CellDep.DEP_GROUP));

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(1024);
    CollectUtils collectUtils = new CollectUtils(api);
    List<CellsWithAddress> cellsWithAddresses =
        collectUtils.collectInputs(
            Collections.singletonList(MinerAddress), cellOutputs, feeRate, Sign.SIGN_LENGTH * 2);

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
      }
      scriptGroupWithPrivateKeysList.add(
          new ScriptGroupWithPrivateKeys(
              new ScriptGroup(NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size())),
              Collections.singletonList(MinerPrivateKey)));
      startIndex += cellsWithAddress.inputs.size();
    }

    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(txBuilder.buildTx());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys.get(0));
    }
    String txHash = api.sendTransaction(signBuilder.buildTx());
    return new OutPoint(txHash, "0x0");
  }
}
