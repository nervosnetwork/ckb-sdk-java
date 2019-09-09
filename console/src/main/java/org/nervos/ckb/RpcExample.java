package org.nervos.ckb;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import org.nervos.ckb.methods.type.Block;
import org.nervos.ckb.methods.type.BlockchainInfo;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class RpcExample {

  private static final String NODE_URL = "http://localhost:8114";

  private static CKBService ckbService;

  public RpcExample() {
    HttpService.setDebug(false);
    ckbService = CKBService.build(new HttpService(NODE_URL));
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to use SDK to visit CKB Blockchain");
    RpcExample client = new RpcExample();
    System.out.println(
        "CKB Blockchain information: " + new Gson().toJson(client.getBlockchainInfo()));
    BigInteger currentBlockNumber = client.getTipBlockNumber();
    System.out.println("Current block number: " + currentBlockNumber.toString());
    System.out.println(
        "Current block information: "
            + new Gson().toJson(client.getBlockByNumber(currentBlockNumber.toString())));
  }

  public Block getBlockByNumber(String blockNumber) throws IOException {
    return ckbService.getBlockByNumber(blockNumber).send().getBlock();
  }

  public BigInteger getTipBlockNumber() throws IOException {
    return ckbService.getTipBlockNumber().send().getBlockNumber();
  }

  public BlockchainInfo getBlockchainInfo() throws IOException {
    return ckbService.getBlockchainInfo().send().getBlockchainInfo();
  }
}
