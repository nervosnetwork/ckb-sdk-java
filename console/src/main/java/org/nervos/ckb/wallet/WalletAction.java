package org.nervos.ckb.wallet;

/**
 * Created by duanyytop on 2019-02-01.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public interface WalletAction {

    String getAddress();

    String sendCapacity(String toAddress, long capacity);

    long getBalance();

}
