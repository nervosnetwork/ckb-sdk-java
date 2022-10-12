package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.sign.omnilock.OmnilockArgs;
import org.nervos.ckb.sign.signer.OmnilockSigner;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.transaction.handler.OmnilockScriptHandler;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.InputIterator;

import java.io.IOException;
import java.util.Iterator;

public class OmnilockMultisigExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String sender = "ckt1qrejnmlar3r452tcg57gvq8patctcgy8acync0hxfnyka35ywafvkqgxhjvp3k9pf88upngryvuxc346q7fq5qmlqqlrhr0p";
    Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript =
        new Secp256k1Blake160MultisigAllSigner.MultisigScript(0, 2,
                                                              "0x7336b0ba900684cb3cb00f0d46d4f64c0994a562",
                                                              "0x5724c1e3925a5206944d753a6f3edaedf977d77f");
    OmnilockSigner.Configuration config = new OmnilockSigner.Configuration();
    config.setOmnilockArgs(new OmnilockArgs(sender));
    config.setMode(OmnilockSigner.Configuration.Mode.AUTH);
    config.setMultisigScript(multisigScript);

    TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
    configuration.registerScriptHandler(new OmnilockScriptHandler().init(network));
    Iterator<TransactionInput> iterator = new InputIterator(sender);
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(configuration, iterator)
        .addOutput(sender, 50100000000L)
        .setChangeOutput(sender)
        .build(config);

    TransactionSigner signer = TransactionSigner.getInstance(network)
        .registerLockScriptSigner(Script.OMNILOCK_CODE_HASH_TESTNET, new OmnilockSigner());
    signer.signTransaction(txWithGroups, new Context("0x7438f7b35c355e3d2fb9305167a31a72d22ddeafb80a21cc99ff6329d92e8087", config));
    signer.signTransaction(txWithGroups, new Context("0x4fd809631a6aa6e3bb378dd65eae5d71df895a82c91a615a1e8264741515c79c", config));

    Api api = new Api("https://testnet.ckb.dev", false);
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
