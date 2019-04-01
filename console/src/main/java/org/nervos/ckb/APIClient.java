package org.nervos.ckb;

import com.google.gson.Gson;
import org.nervos.ckb.methods.type.*;

import java.io.IOException;
import java.util.Collections;

public class APIClient {

    private final static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {

        long blockNumber = 0;

        String blockHash = RpcRequest.getBlockHash(blockNumber);
        System.out.println("Block hash: " + blockHash);

        Block block = RpcRequest.getBlock(blockHash);
        System.out.println("Block: " + gson.toJson(block));

        System.out.println("Transaction: " + gson.toJson(RpcRequest.getTransaction(block.commitTransactions.get(0).hash)));

        System.out.println("Header: " + gson.toJson(RpcRequest.getTipHeader()));

        System.out.println("Block number: " + RpcRequest.getTipBlockNumber().toString());

        System.out.println("Local host: " + gson.toJson(RpcRequest.localNodeInfo()));

        System.out.println("Cells: " + gson.toJson(RpcRequest.getCellsByTypeHash("0x0da2fe99fe549e082d4ed483c2e968a89ea8d11aabf5d79e5cbf06522de6e674", 1, 100)));

        System.out.println("CellOutputWithOutPoint: " + gson.toJson(RpcRequest.getLiveCell()));

        System.out.println("Transaction hash: " + RpcRequest.sendTransaction(new Transaction(0, Collections.emptyList(), Collections.emptyList(), Collections.emptyList())));

        System.out.println("Always Success CellOutputWithOutPoint Hash: " + RpcRequest.alwaysSuccessCellHash());

        System.out.println("Always Success Script OutPoint: " + gson.toJson(RpcRequest.alwaysSuccessScriptOutPoint()));

    }

}
