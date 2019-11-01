package org.nervos.ckb.example;

import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.address.CodeHashType;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.transaction.CellCollector;
import org.nervos.ckb.transaction.CollectedCells;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class MultiSignTransactionExample {

  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static Api api;
  private static List<String> privateKeys;
  private static List<String> publicKeys;
  private static Configuration configuration;
  private static SystemScriptCell multiSigSystemCell;
  private static SystemScriptCell systemSecpCell;

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
    systemSecpCell = SystemContract.getSystemSecpCell(api);
    multiSigSystemCell = SystemContract.getSystemMultiSigCell(api);
    configuration = new Configuration(0, 2, publicKeys);

    String multiSigAddress = configuration.address();
    String targetAddress = "ckt1qyqrlj6znd3uhvuln5z83epv54xu8pmphzgse5uylq";
    System.out.println(
        "Before transferring, multi-sig address "
            + multiSigAddress
            + " balance is "
            + getMultiSigBalance().divide(UnitCKB).toString()
            + " CKB");

    String txHash =
        sendCapacity(
            targetAddress,
            UnitCKB.multiply(BigInteger.valueOf(6000)),
            privateKeys,
            BigInteger.valueOf(10000));
    System.out.println("Transaction hash: " + txHash);
    Thread.sleep(30000);
    System.out.println(
        "After transferring, multi-sig address "
            + multiSigAddress
            + " balance is "
            + getMultiSigBalance().divide(UnitCKB).toString()
            + " CKB");
  }

  public static BigInteger getMultiSigBalance() throws IOException {
    Script lock = generateLock();
    CellCollector cellCollector = new CellCollector(api);
    return cellCollector.getCapacityWithLockHash(lock.computeHash());
  }

  public static Transaction generateTx(
      String targetAddress, BigInteger capacity, List<String> privateKeys, BigInteger fee)
      throws IOException {
    if (privateKeys.size() != configuration.threshold) {
      throw new IOException("Invalid number of keys");
    }
    AddressUtils addressUtils = new AddressUtils(Network.TESTNET);
    CellOutput cellOutput =
        new CellOutput(
            Numeric.toHexString(capacity.toString()),
            new Script(
                systemSecpCell.cellHash,
                addressUtils.getArgsFromAddress(targetAddress),
                Script.TYPE));
    String outputData = "0x";
    CellOutput changeOutput = new CellOutput("0x0", generateLock());
    CellCollector cellCollector = new CellCollector(api);
    CollectedCells collectedCells =
        cellCollector.getCellInputs(generateLock().computeHash(), capacity);
    BigInteger inputCapacity = collectedCells.capacity;
    changeOutput.capacity =
        Numeric.toHexString(inputCapacity.subtract(capacity).subtract(fee).toString());
    Transaction transaction =
        new Transaction(
            "0x0",
            Arrays.asList(
                new CellDep(multiSigSystemCell.outPoint, CellDep.DEP_GROUP),
                new CellDep(systemSecpCell.outPoint, CellDep.DEP_GROUP)),
            Collections.emptyList(),
            collectedCells.inputs,
            Arrays.asList(cellOutput, changeOutput),
            Arrays.asList(outputData, outputData),
            collectedCells.witnesses);

    String txHash = transaction.computeHash();
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(txHash));
    StringBuilder emptySignature = new StringBuilder();
    for (int i = 0; i < privateKeys.size(); i++) {
      emptySignature.append(Witness.EMPTY_LOCK);
    }
    Witness emptiedWitness = (Witness) transaction.witnesses.get(0);
    emptiedWitness.lock = configuration.serialize().concat(emptySignature.toString());
    Table table = Serializer.serializeWitnessArgs(emptiedWitness);
    blake2b.update(new UInt64(table.getLength()).toBytes());
    blake2b.update(table.toBytes());

    for (int i = 1; i < transaction.witnesses.size(); i++) {
      byte[] bytes;
      if (transaction.witnesses.get(i).getClass() == Witness.class) {
        bytes = Serializer.serializeWitnessArgs((Witness) transaction.witnesses.get(i)).toBytes();
      } else {
        bytes = Numeric.hexStringToByteArray((String) transaction.witnesses.get(i));
      }
      blake2b.update(new UInt64(bytes.length).toBytes());
      blake2b.update(bytes);
    }

    String message = blake2b.doFinalString();
    StringBuilder concatenatedSignatures = new StringBuilder();
    for (String privateKey : privateKeys) {
      ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
      concatenatedSignatures.append(
          Numeric.toHexStringNoPrefix(
              Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature()));
    }
    ((Witness) transaction.witnesses.get(0)).lock =
        configuration.serialize().concat(concatenatedSignatures.toString());

    List<String> signedWitness = new ArrayList<>();
    for (Object witness : transaction.witnesses) {
      if (witness.getClass() == Witness.class) {
        signedWitness.add(
            Numeric.toHexString(Serializer.serializeWitnessArgs((Witness) witness).toBytes()));
      } else {
        signedWitness.add((String) witness);
      }
    }
    transaction.witnesses = signedWitness;

    return transaction;
  }

  public static String sendCapacity(
      String targetAddress, BigInteger capacity, List<String> privateKeys, BigInteger fee)
      throws IOException {
    Transaction tx = generateTx(targetAddress, capacity, privateKeys, fee);
    return api.sendTransaction(tx);
  }

  public static Script generateLock() {
    return new Script(
        multiSigSystemCell.cellHash,
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

    public String address() {
      AddressUtils addressUtils = new AddressUtils(Network.TESTNET, CodeHashType.MULTISIG);
      return addressUtils.generate(blake160());
    }
  }
}
