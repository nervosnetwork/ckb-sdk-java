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
import org.nervos.ckb.utils.MoleculeConverter;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.InputIterator;

import java.io.IOException;
import java.util.Iterator;

public class DaoWithdrawExample {
  private static Script daoScript = new Script(Script.DAO_CODE_HASH,
                                               new byte[0],
                                               Script.HashType.TYPE);

  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    // the block number where the deposit dao transaction is
    long daoDepositBlockNumber = 5629199L;
    String daoDepositBlockHash = "0x4427bbd849721a391e234a913b7c2780ad682a36003a29ead759c87cd8ed38df";
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r";
    // Withdraw 500 CKB
    CellOutput output = new CellOutput(50000000000L,
                                       Address.decode(sender).getScript(),
                                       daoScript);
    byte[] data = MoleculeConverter.packUint64(daoDepositBlockNumber).toByteArray();

    // Construct transaction
    Iterator<TransactionInput> iterator = new InputIterator(sender);

    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(iterator)
        .registerScriptHandler(new Secp256k1Blake160SighashAllScriptHandler(network))
        .registerScriptHandler(new DaoScriptHandler(network))
        .addHeaderDep(daoDepositBlockHash)
        .addInput("0xb27ac08c74f4cdac45c1a788379adf0a1923ef76e6c626bd389847748fa36456", 0)
        .addOutput(output, data)
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
