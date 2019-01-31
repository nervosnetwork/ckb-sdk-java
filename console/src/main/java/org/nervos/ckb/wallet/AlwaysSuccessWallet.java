package org.nervos.ckb.wallet;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class AlwaysSuccessWallet {

    public static void main(String[] args) throws IOException {

        WalletApi bobWallet = WalletApi.createWithPrivateKey("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
//        System.out.println("Bob's address: " + bobWallet.getAddress() + " and balance: " + bobWallet.getBalance());
//
//
        WalletApi aliceWallet = WalletApi.createWithPrivateKey("76e853efa8245389e33f6fe49dcbd359eb56be2f6c3594e12521d2a806d32156");
//        System.out.println("Alice's address: " + aliceWallet.getAddress() + " and balance: " + aliceWallet.getBalance());

        System.out.println("Bob send 200 capacity to alice...");
        String hash = bobWallet.sendCapacity(aliceWallet.getAddress(), 200);

        try {
            Thread.sleep(4000);
            System.out.println(new Gson().toJson(bobWallet.getTransactionByHash(hash)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(4000);
            System.out.println(new Gson().toJson(bobWallet.getTransactionByHash(hash)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//
//        System.out.println("Bob's address:  " + bobWallet.getAddress() + " and balance: " + bobWallet.getBalance());
//        System.out.println("Alice's address:  " + aliceWallet.getAddress() + " and balance: " + aliceWallet.getBalance());
    }

}
