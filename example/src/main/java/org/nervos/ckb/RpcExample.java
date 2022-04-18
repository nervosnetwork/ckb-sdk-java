package org.nervos.ckb;

import static org.nervos.ckb.utils.Const.*;

import com.google.gson.Gson;
import java.io.IOException;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.BlockchainInfo;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class RpcExample {

  private static Api api;

  public RpcExample() {
    api = new Api(NODE_URL, false);
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to use SDK to visit CKB Blockchain");
    RpcExample client = new RpcExample();
    System.out.println(
        "CKB Blockchain information: " + new Gson().toJson(client.getBlockchainInfo()));
    long currentBlockNumber = client.getTipBlockNumber();
    System.out.println("Current block number: " + currentBlockNumber);
    System.out.println(
        "Current block information: "
            + new Gson().toJson(client.getBlockByNumber(currentBlockNumber)));
  }

  public Block getBlockByNumber(long blockNumber) throws IOException {
    return api.getBlockByNumber(blockNumber);
  }

  public long getTipBlockNumber() throws IOException {
    return api.getTipBlockNumber();
  }

  public BlockchainInfo getBlockchainInfo() throws IOException {
    return api.getBlockchainInfo();
  }
}
