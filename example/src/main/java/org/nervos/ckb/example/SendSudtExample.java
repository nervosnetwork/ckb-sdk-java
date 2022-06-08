package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.SudtTransactionBuilder;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.transaction.scriptHandler.Secp256k1Blake160SighashAllScriptHandler;
import org.nervos.ckb.transaction.scriptHandler.SudtScriptHandler;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.InputIterator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

public class SendSudtExample {
  public static void main(String[] args) throws IOException {
    Script type = new Script(
        Numeric.hexStringToByteArray("0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4"),
        Numeric.hexStringToByteArray("0x7c7f0ee1d582c385342367792946cff3767fe02f26fd7f07dba23ae3c65b28bc"),
        Script.HashType.TYPE);
    String receiver = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqd0pdquvfuq077aemn447shf4d8u5f4a0glzz2g4";
    CellOutput receiverOutput = new CellOutput(15000000000L, Address.decode(receiver).getScript(), type);
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq02cgdvd5mng9924xarf3rflqzafzmzlpsuhh83c";
    CellOutput changeOutput = new CellOutput(0, Address.decode(sender).getScript(), type);

    Iterator<TransactionInput> iterator = new InputIterator(new DefaultIndexerApi("https://testnet.ckb.dev/indexer", false),
                                                            Arrays.asList(sender),
                                                            type);

    TransactionWithScriptGroups txWithGroups = new SudtTransactionBuilder(iterator)
        .registerScriptHandler(new Secp256k1Blake160SighashAllScriptHandler(Network.TESTNET))
        .registerScriptHandler(new SudtScriptHandler(Network.TESTNET))
        .addOutput(receiverOutput, BigInteger.valueOf(900L))
        .setFeeRate(1000)
        .setChangeOutput(changeOutput)
        .build(null);

    TransactionSigner.getInstance(Network.TESTNET)
        .signTransaction(txWithGroups, "0x4e3796fb07ef32553485f995ef6d63a66792f86ebfa431815282f3f81029adfb");

    Api api = new Api("https://testnet.ckb.dev", false);
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
