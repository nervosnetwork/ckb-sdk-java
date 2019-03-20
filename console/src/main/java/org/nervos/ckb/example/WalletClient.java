package org.nervos.ckb.example;

import com.google.gson.Gson;
import org.nervos.ckb.methods.type.Transaction;
import org.nervos.ckb.rpc.RpcRequest;
import org.nervos.ckb.wallet.AlwaysSuccessWallet;
import org.nervos.ckb.wallet.VerifyWallet;

/**
 * Created by duanyytop on 2019-01-31.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class WalletClient {

    private static String MRUBY_CELL_HASH = "0x828d1e109a79964521bf5fbbedb4f6e695a9c4b6b674a58887f30c7398e93a76";
    private static String MRUBY_OUT_POINT_HASH = "0x765273ea8ca662eb38c66b307779968a031cf5242f72972a7352dbaed6e9fecb";

    public static void main(String[] args) throws Exception {

        initConfig();

        AlwaysSuccessWallet minerWallet = new AlwaysSuccessWallet();
        System.out.println("Miner's always success wallet address: " + minerWallet.getAddress() + " and balance: " + minerWallet.getBalance());

        VerifyWallet bobWallet = VerifyWallet.createWithPrivateKey("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
        System.out.println("Bob's signed wallet address: " + bobWallet.getAddress() + " and balance: " + bobWallet.getBalance());

        System.out.println("Miner send 1000 capacity to Bob...");
        String minerHash = minerWallet.sendCapacity(bobWallet.getAddress(), 1000);
        Transaction transaction = RpcRequest.getTransaction(minerHash);

        while(transaction == null) {
            System.out.println("The transaction are packaging into blockchain");
            transaction = RpcRequest.getTransaction(minerHash);
            Thread.sleep(6000);
        }
        System.out.println("Miner's transaction: " + new Gson().toJson(transaction));
        System.out.println("Bob's signed wallet address: " + bobWallet.getAddress() + " and balance: " + bobWallet.getBalance());

        VerifyWallet aliceWallet = VerifyWallet.createWithPrivateKey("76e853efa8245389e33f6fe49dcbd359eb56be2f6c3594e12521d2a806d32156");
        System.out.println("Alice's signed wallet address: " + aliceWallet.getAddress() + " and balance: " + aliceWallet.getBalance());

        System.out.println("Bob send 100 capacity to Alice...");
        String bobHash = bobWallet.sendCapacity(aliceWallet.getAddress(), 100);
        Transaction bobTransaction = RpcRequest.getTransaction(bobHash);

        while(bobTransaction == null) {
            System.out.println("The transaction are packaging into blockchain");
            Thread.sleep(6000);
            bobTransaction = RpcRequest.getTransaction(bobHash);
        }
        System.out.println("Bob's transaction: " + new Gson().toJson(bobTransaction));

        System.out.println("Bob's signed wallet address:  " + bobWallet.getAddress() + " and balance: " + bobWallet.getBalance());
        System.out.println("Alice's signed wallet address:  " + aliceWallet.getAddress() + " and balance: " + aliceWallet.getBalance());

    }


    private static void initConfig() {
        VerifyWallet.setMRubyCellHash(MRUBY_CELL_HASH);
        VerifyWallet.setMRubyOutPointHash(MRUBY_OUT_POINT_HASH);
    }

}
