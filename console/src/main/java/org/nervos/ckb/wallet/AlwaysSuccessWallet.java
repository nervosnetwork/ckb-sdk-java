package org.nervos.ckb.wallet;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class AlwaysSuccessWallet {

    public static void main(String[] args) {

        WalletApi bobWallet = WalletApi.createWithPrivateKey("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
        System.out.println(bobWallet.getAddress() + ": " + bobWallet.getBalance());

        System.out.println(bobWallet.getMinerAddress());

    }

}
