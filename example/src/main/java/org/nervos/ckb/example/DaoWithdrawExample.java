package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.DaoTransactionBuilder;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.transaction.handler.DaoScriptHandler;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.transaction.InputIterator;

import java.io.IOException;
import java.util.Iterator;

public class DaoWithdrawExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    Api api = new Api("https://testnet.ckb.dev", false);
    // the block number where the deposit dao transaction is
    OutPoint depositOutPoint = new OutPoint("0xc4662aa4a0c9087aa299121fef06dcc2dbf30271441a85fdf9d62fb312b259e6", 0);
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r";

    TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
    Iterator<TransactionInput> iterator = new InputIterator(sender);
    TransactionWithScriptGroups txWithGroups = new DaoTransactionBuilder(configuration, iterator, depositOutPoint, api)
        .addWithdrawOutput(sender)
        .setChangeOutput(sender)
        .build(new DaoScriptHandler.WithdrawInfo(api, depositOutPoint));

    // Sign transaction
    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, "0x6c9ed03816e3111e49384b8d180174ad08e29feb1393ea1b51cef1c505d4e36a");

    // Send transaction
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
