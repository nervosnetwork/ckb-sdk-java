package org.nervos.ckb.example;

import org.bouncycastle.util.encoders.Hex;
import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.transaction.scriptHandler.Secp256k1Blake160MultisigAllScriptHandler;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.InputIterator;

import java.io.IOException;
import java.util.*;

public class SendCkbFromMultisigAddressExample {
  public static void main(String[] args) throws IOException {
    Network network = Network.TESTNET;
    List<byte[]> keyHashes = new ArrayList<>();
    keyHashes.add(Numeric.hexStringToByteArray("0x7336b0ba900684cb3cb00f0d46d4f64c0994a562"));
    keyHashes.add(Numeric.hexStringToByteArray("0x5724c1e3925a5206944d753a6f3edaedf977d77f"));
    Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript =
        new Secp256k1Blake160MultisigAllSigner.MultisigScript(0, 2, keyHashes);
    Script lock = new Script(
        Script.SECP256_BLAKE160_MULTISIG_ALL_CODE_HASH,
        multisigScript.computeHash(),
        Script.HashType.TYPE);
    // ckt1qpw9q60tppt7l3j7r09qcp7lxnp3vcanvgha8pmvsa3jplykxn32sqdunqvd3g2felqv6qer8pkydws8jg9qxlca0st5v
    String sender = new Address(lock, network).encode();

    Iterator<TransactionInput> iterator = new InputIterator(sender);
    TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(iterator)
        .registerScriptHandler(new Secp256k1Blake160MultisigAllScriptHandler(network))
        .addOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r",
                   50100000000L)
        .setFeeRate(1000)
        .setChangeOutput(sender)
        .build(multisigScript);

    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, new HashSet<>(Arrays.asList(new Context("0x4fd809631a6aa6e3bb378dd65eae5d71df895a82c91a615a1e8264741515c79c", multisigScript))));
    TransactionSigner.getInstance(network)
        .signTransaction(txWithGroups, new HashSet<>(Arrays.asList(new Context("0x7438f7b35c355e3d2fb9305167a31a72d22ddeafb80a21cc99ff6329d92e8087", multisigScript))));

    Api api = new Api("https://testnet.ckb.dev", false);
    byte[] txHash = api.sendTransaction(txWithGroups.getTxView());
    System.out.println("Transaction hash: " + Hex.toHexString(txHash));
  }
}
