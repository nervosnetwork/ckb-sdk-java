package org.nervos.ckb;

import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Utils;
import org.nervos.ckb.utils.address.AddressGenerator;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class MultiSignTransactionExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static List<String> privateKeys;
  private static List<String> publicKeys;
  private static Configuration configuration;
  private static SystemScriptCell systemMultiSigCell;

  static {
    api = new Api(NODE_URL, false);
    privateKeys =
        Arrays.asList(
            "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2",
            "a202386cb9e46cecff9bc14b748b714c713075dd964c2507c8a8900540164959");
    // another private key is 89b773ec5cf97b8fd2cf280ab1e37cd658dc28d84bac8f8dda4a8646cc08d266

    publicKeys =
        Arrays.asList(
            "32edb83018b57ddeb9bcc7287c5cc5da57e6e0289d31c9e98cb361e88678d6288",
            "33aeb3fdbfaac72e9e34c55884a401ee87115302c146dd9e314677d826375dc8f",
            "29a685b8206550ea1b600e347f18fd6115bffe582089d3567bec7eba57d04df01");
  }

  public static void main(String[] args) throws Exception {
    systemMultiSigCell = SystemContract.getSystemMultiSigCell(api);
    configuration = new Configuration(0, 2, publicKeys);

    String multiSigAddress = configuration.address();
    String targetAddress = "ckt1qyqrlj6znd3uhvuln5z83epv54xu8pmphzgse5uylq";
    System.out.println(
        "Before transferring, multi-sig "
            + multiSigAddress
            + " balance: "
            + getMultiSigBalance()
            + " CKB");
    System.out.println(
        "Before transferring, target "
            + targetAddress
            + " balance: "
            + getBalance(targetAddress)
            + " CKB");

    String txHash = sendCapacity(targetAddress, Utils.ckbToShannon(3000), privateKeys);
    System.out.println("Transaction hash: " + txHash);
    Thread.sleep(30000);

    System.out.println(
        "After transferring, multi-sig "
            + multiSigAddress
            + " balance: "
            + getMultiSigBalance()
            + " CKB");

    System.out.println(
        "After transferring, target "
            + targetAddress
            + " balance: "
            + getBalance(targetAddress)
            + " CKB");
  }

  public static String getMultiSigBalance() throws IOException {
    Script lock = generateLock();
    CellCollector cellCollector = new CellCollector(api);
    return cellCollector.getCapacityWithLockHash(lock.computeHash()).divide(UnitCKB).toString();
  }

  public static String getBalance(String address) throws IOException {
    CellCollector cellCollector = new CellCollector(api);
    return cellCollector.getCapacityWithAddress(address).divide(UnitCKB).toString();
  }

  public static Transaction generateTx(
      String targetAddress, BigInteger capacity, List<String> privateKeys) throws IOException {
    if (privateKeys.size() != configuration.threshold) {
      throw new IOException("Invalid number of keys");
    }
    List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();
    TransactionBuilder txBuilder = new TransactionBuilder(api, true);
    CollectUtils txUtils = new CollectUtils(api);

    List<CellOutput> cellOutputs =
        txUtils.generateOutputs(
            Collections.singletonList(new Receiver(targetAddress, capacity)),
            configuration.address());
    txBuilder.addOutputs(cellOutputs);

    // You can get fee rate by rpc or set a simple number
    // BigInteger feeRate = Numeric.toBigInt(api.estimateFeeRate("5").feeRate);
    BigInteger feeRate = BigInteger.valueOf(1024);

    // initial_length = multi_sig_hash.length + 2 * secp256k1_signature_byte.length
    CollectResult collectResult =
        txUtils.collectInputs(
            Collections.singletonList(configuration.address()),
            txBuilder.buildTx(),
            feeRate,
            configuration.serialize().length() + configuration.threshold * Sign.SIGN_LENGTH * 2);

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
              privateKeys));
      startIndex += cellsWithAddress.inputs.size();
    }

    Secp256k1MultisigAllBuilder signBuilder =
        new Secp256k1MultisigAllBuilder(txBuilder.buildTx(), configuration.serialize());

    for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
      signBuilder.sign(
          scriptGroupWithPrivateKeys.scriptGroup, scriptGroupWithPrivateKeys.privateKeys);
    }

    return signBuilder.buildTx();
  }

  public static String sendCapacity(
      String targetAddress, BigInteger capacity, List<String> privateKeys) throws IOException {
    Transaction tx = generateTx(targetAddress, capacity, privateKeys);
    return api.sendTransaction(tx);
  }

  public static Script generateLock() {
    return new Script(
        systemMultiSigCell.cellHash,
        Numeric.prependHexPrefix(configuration.blake160()),
        Script.TYPE);
  }

  static class Configuration {
    int requireN;
    int threshold;
    List<String> publicKeys;

    Configuration(int requireN, int threshold, List<String> publicKeys) throws IOException {
      if (requireN < 0 || requireN > 255) {
        throw new IOException("requireN should be less than 256");
      }
      if (threshold < 0 || threshold > 255) {
        throw new IOException("threshold should be less than 256");
      }
      if (publicKeys.size() > 255) {
        throw new IOException("Public key number must be less than 256");
      }
      this.requireN = requireN;
      this.threshold = threshold;
      this.publicKeys = publicKeys;
    }

    String serialize() {
      StringBuilder multiSigBuffer = new StringBuilder();
      List<Byte> bytes = new ArrayList<>();
      bytes.addAll(Numeric.intToBytes(0));
      bytes.addAll(Numeric.intToBytes(requireN));
      bytes.addAll(Numeric.intToBytes(threshold));
      bytes.addAll(Numeric.intToBytes(publicKeys.size()));
      multiSigBuffer.append(Numeric.toHexStringNoPrefix(Bytes.toArray(bytes)));
      for (String publicKey : publicKeys) {
        multiSigBuffer.append(Hash.blake160(publicKey));
      }
      return multiSigBuffer.toString();
    }

    public String blake160() {
      return Hash.blake160(serialize());
    }

    public String address() throws IOException {
      Script script =
          new Script(SystemContract.getSystemMultiSigCell(api).cellHash, blake160(), Script.TYPE);
      return AddressGenerator.generate(Network.TESTNET, script);
    }
  }
}
