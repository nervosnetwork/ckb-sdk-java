package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.AbstractInputIterator;
import org.nervos.ckb.transaction.SudtTransactionBuilder;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.transaction.InputIterator;

import java.io.IOException;
import java.util.Iterator;

public class IssueSudtExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdamwzrffgc54ef48493nfd2sd0h4cjnxg4850up";

    TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);

    AbstractInputIterator iterator = new InputIterator(sender);
    TransactionWithScriptGroups txWithGroups = new SudtTransactionBuilder(configuration, iterator,
                                                                          SudtTransactionBuilder.TransactionType.ISSUE,
                                                                          sender)
        .addSudtOutput(sender, 10L)
        .setChangeOutput(sender)
        .build();

    // Sign transaction
    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, "0x0c982052ffd4af5f3bbf232301dcddf468009161fc48ba1426e3ce0929fb59f8");

    // Send transaction
    Api api = new Api("https://testnet.ckb.dev", false);
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    iterator.applySendTransaction(txWithGroups.getTxView(), txHash);
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
