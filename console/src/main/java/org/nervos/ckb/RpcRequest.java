package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.exceptions.APIErrorException;
import org.nervos.ckb.methods.type.*;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;

/** Created by duanyytop on 2019-01-31. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class RpcRequest {

  private static final String NODE_URL = "http://localhost:8114/";

  private static CKBService ckbService;

  static {
    HttpService.setDebug(false);
    ckbService = CKBService.build(new HttpService(NODE_URL));
  }

  public static String getBlockHash(String blockNumber) throws IOException {
    return ckbService.getBlockHash(blockNumber).send().getBlockHash();
  }

  public static Block getBlock(String blockHash) throws IOException {
    return ckbService.getBlock(blockHash).send().getBlock();
  }

  public static Block getBlockByNumber(String blockNumber) throws IOException {
    return ckbService.getBlockByNumber(blockNumber).send().getBlock();
  }

  public static Transaction getTransaction(String transactionHash) throws IOException {
    return ckbService.getTransaction(transactionHash).send().getTransaction().transaction;
  }

  public static Header getTipHeader() throws IOException {
    return ckbService.getTipHeader().send().getHeader();
  }

  public static BigInteger getTipBlockNumber() throws IOException {
    return ckbService.getTipBlockNumber().send().getBlockNumber();
  }

  public static Epoch getCurrentEpoch() throws IOException {
    return ckbService.getCurrentEpoch().send().getEpoch();
  }

  public static Epoch getEpochByNumber(String number) throws IOException {
    return ckbService.getEpochByNumber(number).send().getEpoch();
  }

  public static NodeInfo localNodeInfo() throws IOException {
    return ckbService.localNodeInfo().send().getNodeInfo();
  }

  public static List<NodeInfo> getPeers() throws IOException {
    return ckbService.getPeers().send().getPeers();
  }

  public static List<CellOutputWithOutPoint> getCellsByLockHash(
      String lockHash, String fromBlockNumber, String toBlockNumber) throws IOException {
    return ckbService
        .getCellsByLockHash(lockHash, fromBlockNumber, toBlockNumber)
        .send()
        .getCells();
  }

  public static Cell getLiveCell() throws IOException {
    return ckbService
        .getLiveCell(
            new OutPoint("0x15c809f08c7bca63d2b661e1dbc26c74551a6f982f7631c718dc43bd2bb5c90e", 0))
        .send()
        .getCell();
  }

  public static String sendTransaction(Transaction transaction) throws IOException {
    return ckbService.sendTransaction(transaction).send().getTransactionHash();
  }

  public static Block genesisBlock() throws IOException {
    String blockHash = ckbService.getBlockHash("0").send().getBlockHash();
    return ckbService.getBlock(blockHash).send().getBlock();
  }

  public static String alwaysSuccessCellHash() throws IOException {
    List<CellOutput> systemCells = genesisBlock().transactions.get(0).outputs;
    if (systemCells.isEmpty() || systemCells.get(0) == null) {
      throw new APIErrorException("Cannot find always success cellOutputWithOutPoint");
    }
    return Hash.blake2b(systemCells.get(0).data);
  }

  public static List<OutPoint> alwaysSuccessScriptOutPoint() throws IOException {
    String hash = genesisBlock().transactions.get(0).hash;
    return Collections.singletonList(new OutPoint(hash, 0));
  }
}
