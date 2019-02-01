package org.nervos.ckb.example;

import org.nervos.ckb.wallet.VerifyWallet;

import java.io.IOException;

/**
 * Created by duanyytop on 2019-01-31.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class VerifyWalletClient {

    public static void main(String[] args) throws IOException {

        VerifyWallet bobWallet = VerifyWallet.createWithPrivateKey("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
        System.out.println("Bob's signed wallet address: " + bobWallet.getAddress() + " and balance: " + bobWallet.getBalance());


        VerifyWallet aliceWallet = VerifyWallet.createWithPrivateKey("76e853efa8245389e33f6fe49dcbd359eb56be2f6c3594e12521d2a806d32156");
        System.out.println("Alice's signed wallet address: " + aliceWallet.getAddress() + " and balance: " + aliceWallet.getBalance());

        System.out.println("Bob send 100 capacity to alice...");
        bobWallet.sendCapacity(aliceWallet.getAddress(), 100);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Bob's signed wallet address:  " + bobWallet.getAddress() + " and balance: " + bobWallet.getBalance());
        System.out.println("Alice's signed wallet address:  " + aliceWallet.getAddress() + " and balance: " + aliceWallet.getBalance());

    }

}
