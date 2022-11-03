package org.nervos.ckb.example;

import com.nervos.lightclient.DefaultLightClientApi;
import com.nervos.lightclient.LightClientApi;
import com.nervos.lightclient.type.ScriptDetail;
import org.nervos.ckb.Network;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.LightClientInputIterator;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import java.io.IOException;
import java.util.Collections;

public class SendCkbByLightClientExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r";

    TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);

    LightClientApi lightClientApi = new DefaultLightClientApi("http://localhost:9000");

    ScriptDetail senderScriptDetail = new ScriptDetail();
    senderScriptDetail.script = Address.decode(sender).getScript();
    senderScriptDetail.scriptType = ScriptType.LOCK;
    senderScriptDetail.blockNumber = 0;
    // Set script to let light client sync information about this script on chain.
    lightClientApi.setScripts(Collections.singletonList(senderScriptDetail));

    LightClientInputIterator iterator = new LightClientInputIterator(lightClientApi);
    iterator.addSearchKey(sender);
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(configuration, iterator)
            .addOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r",
                    50100000000L)
            .setChangeOutput(sender)
            .build();

    TransactionSigner.getInstance(network)
            .signTransaction(txWithGroups, "0x6c9ed03816e3111e49384b8d180174ad08e29feb1393ea1b51cef1c505d4e36a");

    byte[] txHash = lightClientApi.sendTransaction(txWithGroups.getTxView());
    iterator.applyOffChainTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Numeric.toHexString(txHash));
  }
}
