package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.GsonFactory;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.*;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;
import java.util.Iterator;

public class SendChainedTransactionExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String address1 = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r";
    String address2 = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdamwzrffgc54ef48493nfd2sd0h4cjnxg4850up";
    String address3 = "ckt1qrejnmlar3r452tcg57gvq8patctcgy8acync0hxfnyka35ywafvkqgxhjvp3k9pf88upngryvuxc346q7fq5qmlqqlrhr0p";

    OffChainInputCollector offChainInputCollector = OffChainInputCollector.getInstance();
    TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
    AbstractInputIterator iterator1 = new OffChainInputIterator(
            new InputIterator(address1), offChainInputCollector, true);

    // The first tx
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(configuration, iterator1)
        .addOutput(address2, 50100000000L)
        .setChangeOutput(address1)
        .build();
    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, "0x6c9ed03816e3111e49384b8d180174ad08e29feb1393ea1b51cef1c505d4e36a");

    Api api = new Api("https://testnet.ckb.dev", false);
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    offChainInputCollector.applyOffChainTransaction(api.getTipBlockNumber(), txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));

    AbstractInputIterator iterator2 = new OffChainInputIterator(
            new InputIterator(address2), offChainInputCollector, true);

    // The second tx will consume outpoint created by the first tx.
    txWithGroups = new CkbTransactionBuilder(configuration, iterator2)
            .addOutput(address3,100000000000L)
            .setChangeOutput(address2)
            .build();
    TransactionSigner.getInstance(network)
            .signTransaction(txWithGroups, "0x0c982052ffd4af5f3bbf232301dcddf468009161fc48ba1426e3ce0929fb59f8");

    txHash = api.sendTransaction(txWithGroups.getTxView());
    offChainInputCollector.applyOffChainTransaction(api.getTipBlockNumber(), txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
