package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.sign.omnilock.OmnilockArgs;
import org.nervos.ckb.sign.signer.OmnilockSigner;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.InputIterator;

import java.io.IOException;
import java.util.Iterator;

public class OmnilockExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String sender = "ckt1qrejnmlar3r452tcg57gvq8patctcgy8acync0hxfnyka35ywafvkqgqgpy7m88v3gxnn3apazvlpkkt32xz3tg5qq3kzjf3";
    OmnilockSigner.Configuration config = new OmnilockSigner.Configuration();
    config.setOmnilockArgs(new OmnilockArgs(sender));
    config.setMode(OmnilockSigner.Configuration.Mode.AUTH);

    TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
    Iterator<TransactionInput> iterator = new InputIterator(sender);
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(configuration, iterator)
        .addOutput(sender, 50100000000L)
        .setChangeOutput(sender)
        .build(config);

    TransactionSigner.getInstance(network)
        .registerLockScriptSigner(Script.OMNILOCK_CODE_HASH_TESTNET, new OmnilockSigner())
        .signTransaction(txWithGroups, new Context("0x6c9ed03816e3111e49384b8d180174ad08e29feb1393ea1b51cef1c505d4e36a", config));

    Api api = new Api("https://testnet.ckb.dev", false);
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
