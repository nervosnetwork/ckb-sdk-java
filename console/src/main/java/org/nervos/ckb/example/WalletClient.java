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

    public static void main(String[] args) throws Exception {

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



}
