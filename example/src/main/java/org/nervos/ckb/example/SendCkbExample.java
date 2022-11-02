package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.AbstractInputIterator;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.InputIterator;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;

public class SendCkbExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r";

    TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
    AbstractInputIterator iterator = new InputIterator(sender);
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(configuration, iterator)
        .addOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r",
                   50100000000L)
        .setChangeOutput(sender)
        .build();

    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, "0x6c9ed03816e3111e49384b8d180174ad08e29feb1393ea1b51cef1c505d4e36a");

    Api api = new Api("https://testnet.ckb.dev", false);
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    iterator.applyOffChainTransaction(txWithGroups.getTxView(), txHash);
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
