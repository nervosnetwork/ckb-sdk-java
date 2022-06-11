package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.DaoTransactionBuilder;
import org.nervos.ckb.transaction.scriptHandler.DaoScriptHandler;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.InputIterator;

import java.io.IOException;
import java.util.Iterator;

public class DaoClaimExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r";
    OutPoint withdrawOutpoint = new OutPoint(
        Numeric.hexStringToByteArray("0x287554d155a9b9e30a1a6fd9e5d9e41afee612b0c8996f0073afb7f2894025f9"), 0);
    Api api = new Api("https://testnet.ckb.dev", false);

    Iterator<TransactionInput> iterator = new InputIterator(sender);
    TransactionWithScriptGroups txWithGroups = new DaoTransactionBuilder(iterator, network, withdrawOutpoint, api)
        .setFeeRate(1000)
        .setChangeOutput(sender)
        .build(new DaoScriptHandler.ClaimInfo(api, withdrawOutpoint));

    // Sign transaction
    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, "0x6c9ed03816e3111e49384b8d180174ad08e29feb1393ea1b51cef1c505d4e36a");

    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
