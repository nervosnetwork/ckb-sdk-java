package org.nervos.ckb.main;

import com.google.gson.Gson;
import org.nervos.ckb.response.ResBlock;
import org.nervos.ckb.response.item.Block;
import org.nervos.ckb.service.HttpService;
import org.nervos.ckb.service.NervosService;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class APIClient {

    private static final String NODE_URL = "http://localhost:8114/";

    private static NervosService nervosService;


    static {
        HttpService.setDebug(true);
        nervosService = NervosService.build(new HttpService(NODE_URL));
    }

    public static void main(String[] args) {
        try {

            String blockHash = nervosService.getBlockHash(1).send().getBlockHash();
            System.out.println("Second block hash is " + blockHash);

            Block block = nervosService.getBlock(blockHash).send().getBlock();
            System.out.println("Second block is " + new Gson().toJson(block));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
