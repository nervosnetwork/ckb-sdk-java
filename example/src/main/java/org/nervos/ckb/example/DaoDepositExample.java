package org.nervos.ckb.example;

import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.transaction.scriptHandler.DaoScriptHandler;
import org.nervos.ckb.transaction.scriptHandler.Secp256k1Blake160SighashAllScriptHandler;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.InputIterator;

import java.io.IOException;
import java.util.Iterator;

public class DaoDepositExample {
  private static byte[] depositDaoData = Numeric.hexStringToByteArray("0x0000000000000000");
  private static Script daoScript = new Script(Script.DAO_CODE_HASH,
                                               new byte[0],
                                               Script.HashType.TYPE);

  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r";
    // Deposit 500 CKB to DAO
    CellOutput output = new CellOutput(50000000000L,
                                       Address.decode(sender).getScript(),
                                       daoScript);

    // Construct transaction
    Iterator<TransactionInput> iterator = new InputIterator(sender);
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(iterator)
        .registerScriptHandler(new Secp256k1Blake160SighashAllScriptHandler(network))
        .registerScriptHandler(new DaoScriptHandler(network))
        .addOutput(output, depositDaoData)
        .setFeeRate(1000)
        .setChangeOutput(sender)
        .build(null);

    // Sign transaction
    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, "0x6c9ed03816e3111e49384b8d180174ad08e29feb1393ea1b51cef1c505d4e36a");

    // Send transaction
    Api api = new Api("https://testnet.ckb.dev", false);
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
