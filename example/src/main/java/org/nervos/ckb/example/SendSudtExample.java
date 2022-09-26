package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.SudtTransactionBuilder;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.InputIterator;

import java.io.IOException;
import java.util.Iterator;

public class SendSudtExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdamwzrffgc54ef48493nfd2sd0h4cjnxg4850up";
    byte[] sudtArgs = Numeric.hexStringToByteArray("0x9d2dab815b9158b2344827749d769fd66e2d3ebdfca32e5628ba0454651851f5");
    String receiver = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqd0pdquvfuq077aemn447shf4d8u5f4a0glzz2g4";

    TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
    Iterator<TransactionInput> iterator =
        new InputIterator(new DefaultIndexerApi("https://testnet.ckb.dev/indexer", false))
            .addSudtSearchKey(sender, sudtArgs);
    TransactionWithScriptGroups txWithGroups = new SudtTransactionBuilder(configuration, iterator,
                                                                          SudtTransactionBuilder.TransactionType.TRANSFER,
                                                                          sudtArgs)
        .addSudtOutput(receiver, 1L)
        .setChangeOutput(sender)
        .build();

    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, "0x0c982052ffd4af5f3bbf232301dcddf468009161fc48ba1426e3ce0929fb59f8");

    Api api = new Api("https://testnet.ckb.dev", false);
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
