package org.nervos.ckb.example;

import com.google.gson.Gson;
import java.math.BigInteger;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.BlockchainInfo;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class RpcExample {

  private static final String NODE_URL = "http://localhost:8114";

  private static Api api;

  public RpcExample() {
    api = new Api(NODE_URL, false);
  }

  public static void main(String[] args) {
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

  public Block getBlockByNumber(String blockNumber) {
    return api.getBlockByNumber(blockNumber);
  }

  public BigInteger getTipBlockNumber() {
    return api.getTipBlockNumber();
  }

  public BlockchainInfo getBlockchainInfo() {
    return api.getBlockchainInfo();
  }
}
