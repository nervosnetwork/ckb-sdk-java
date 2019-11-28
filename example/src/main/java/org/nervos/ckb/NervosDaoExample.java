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
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Utils;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class NervosDaoExample {
  private static final String NERVOS_DAO_DATA = "0x0000000000000000";
  private static final BigInteger UnitCKB = new BigInteger("100000000");

  private static final String NODE_URL = "http://localhost:8114";
  private static Api api;
  private static String DaoPrivateKey =
      "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2";
  private static String DaoAddress = "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g";

  static {
    api = new Api(NODE_URL, false);
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Before depositing, balance: " + getBalance(DaoAddress) + "CKB");
    OutPoint outPoint = depositToDao(Utils.ckbToShannon(1000));
    System.out.println(outPoint.txHash);

    Thread.sleep(10000);
    System.out.println("After depositing, balance: " + getBalance(DaoAddress) + "CKB");
  }

  private static String getBalance(String address) throws IOException {
    CellCollector cellCollector = new CellCollector(api, true);
    return cellCollector.getCapacityWithAddress(address).divide(UnitCKB).toString(10);
  }

  private static OutPoint depositToDao(BigInteger capacity) throws IOException {
    Script lock = LockUtils.generateLockScriptWithAddress(DaoAddress);
    Script type =
        new Script(SystemContract.getSystemNervosDaoCell(api).cellHash, "0x", Script.TYPE);
    CellOutput cellOutput = new CellOutput(Numeric.toHexStringWithPrefix(capacity), lock, type);
    CellOutput changeOutput = new CellOutput("0x0", lock);
    List<CellOutput> cellOutputs = Arrays.asList(cellOutput, changeOutput);
    List<String> cellOutputsData = Arrays.asList(NERVOS_DAO_DATA, "0x");

    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();
    TransactionBuilder txBuilder = new TransactionBuilder(api);
    txBuilder.setOutputsData(cellOutputsData);
    txBuilder.addCellDep(
        new CellDep(SystemContract.getSystemNervosDaoCell(api).outPoint, CellDep.CODE));

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(1024);
    CollectUtils collectUtils = new CollectUtils(api, true);
    CollectResult collectResult =
        collectUtils.collectInputs(
            Collections.singletonList(DaoAddress),
            cellOutputs,
            feeRate,
            Sign.SIGN_LENGTH * 2,
            txBuilder.getCellDeps(),
            cellOutputsData);

    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    txBuilder.addOutputs(cellOutputs);

    int startIndex = 0;
    for (CellsWithAddress cellsWithAddress : collectResult.cellsWithAddresses) {
      txBuilder.addInputs(cellsWithAddress.inputs);
      for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
      }
      scriptGroupWithPrivateKeysList.add(
          new ScriptGroupWithPrivateKeys(
              new ScriptGroup(NumberUtils.regionToList(startIndex, cellsWithAddress.inputs.size())),
              Collections.singletonList(DaoPrivateKey)));
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
