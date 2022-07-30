# CKB SDK Java

[![License](https://img.shields.io/badge/license-MIT-green)](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/LICENSE)
[![Github Actions CI](https://github.com/nervosnetwork/ckb-sdk-java/workflows/CI/badge.svg?branch=develop)](https://github.com/nervosnetwork/ckb-sdk-java/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.nervos.ckb/ckb/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.nervos.ckb/ckb)
[![Telegram Group](https://cdn.rawgit.com/Patrolavia/telegram-badge/8fe3382b/chat.svg)](https://t.me/nervos_ckb_dev)

Java SDK for Nervos [CKB](https://github.com/nervosnetwork/ckb).

## Prerequisites

* Java 8
* Gradle 5.0 or later

## Installation

Maven

```
<dependency>
  <groupId>org.nervos.ckb</groupId>
  <artifactId>ckb-api</artifactId>
  <version>{version}</version>
</dependency>
```

Gradle

```
implementation 'org.nervos.ckb:ckb-api:{version}'
```

## Build

Run `gradle build` in project root directory.

## Quick start

Here we will give some most frequently used operations, to bring you enlightenment about how to use ckb-sdk-java to operate your asset in CKB chain.

### Setup
ckb-java-sdk provides a convenient client to help you easily interact with [CKB](https://github.com/nervosnetwork/ckb), [CKB-indexer](https://github.com/nervosnetwork/ckb-indexer) or [Mercury](https://github.com/nervosnetwork/mercury) node.

```java
// Set up client. If you do not use ones of these node, just set them to null;
String ckbUrl = "http://127.0.0.1:8114";
String indexerUrl = "http://127.0.0.1:8114";
String mercuryUrl = "http://127.0.0.1:8116";
DefaultCkbApi ckbApi = new DefaultCkbApi(ckbUrl, mercuryUrl, indexerUrl, false);
```

You can leverage this client to call any RPC APIs provided by CKB, CKB-indexer or Mercury in Java code.
```java
byte[] blockHash = Numeric.hexStringToByteArray("0x77fdd22f6ae8a717de9ae2b128834e9b2a1424378b5fc95606ba017aab5fed75");
Block block = ckbApi.getBlock(blockHash);
```

For more details about RPC APIs, please check:

- [CKB RPC doc](https://github.com/nervosnetwork/ckb/blob/develop/rpc/README.md)
- [CKB-indexer RPC doc](https://github.com/nervosnetwork/ckb-indexer/blob/master/README.md)
- [Mercury RPC doc](https://github.com/nervosnetwork/mercury/blob/main/core/rpc/README.md).

### Build transaction with indexer

[ckb-indexer](https://github.com/nervosnetwork/ckb-indexer) is an application to collect cells and transactions from CKB
chain. With ckb-indexer to collect live cells, we can build transactions easily.

ckb-java-sdk encapsulates the common logic into a user-friendly transaction builder. It could greatly free you from
getting into script details and from tedious manual work of building transaction including adding celldeps, transaction
fee calculation, change output set and so on.

Here is an example to build a CKB transfer transaction with the help of transaction builder and ckb-indexer.

```java
String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r";
String receiver = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqg958atl2zdh8jn3ch8lc72nt0cf864ecqdxm9zf";
Iterator<TransactionInput> iterator = new InputIterator(sender);
TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(iterator, Network.TESTNET)
    .addOutput(receiver, 50100000000L)
    .setFeeRate(1000)
    .setChangeOutput(sender)
    .build();
```

For more use cases of building transaction with ckb-indexer, please refer
to [these examples](./example/src/main/java/org/nervos/ckb/example).

### Build transaction with Mercury

[Mercury](https://github.com/nervosnetwork/mercury) is an application for better interaction with CKB chain, providing
many useful [RPC APIs](https://github.com/nervosnetwork/mercury/blob/main/core/rpc/README.md) for development like
querying transactions or getting UDT asset information. You need to deploy your own mercury server and sync data with the
latest network before using it.

Mercury is another way to build transaction. With the help of Mercury, you can build a transaction by simply calling a
JSON-RPC API. Here we show how to build a CKB transfer transaction with mercury.

```java
String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq0yvcdtsu5wcr2jldtl72fhkruf0w5vymsp6rk9r";
String receiver = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqvglkprurm00l7hrs3rfqmmzyy3ll7djdsujdm6z";

long ckbAmount = AmountUtils.ckbToShannon(100);   // Convert CKB to Shannon (1 CKB = 10^8 Shannon)
SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
builder.addFrom(sender);
builder.addTo(receiver, ckbAmount);
builder.assetInfo(AssetInfo.newCkbAsset());

// Get an unsigned raw transaction with the help of Mercury
TransactionWithScriptGroups txWithScriptGroups = api.buildSimpleTransferTransaction(builder.build());
```

For more use cases of mercury, please refer to [these test cases](./ckb-mercury-sdk/src/test/java/mercury)
and [Mercury JSON-RPC documentation](https://github.com/nervosnetwork/mercury/blob/dev-0.4/core/rpc/README.md).

### Sign and send transaction

To send transaction you build to CKB network, you need to

1. sign transaction with your private key.
2. send signed transaction to CKB node, and wait it to be confirmed.

Before signing and sending transaction, you need to prepare a raw transaction represented by an instance of class `TransactionWithScriptGroups`. You can get it [by Mercury](#Build-transaction-with-Mercury) or [by ckb-indexer](#Build-transaction-with-indexer)

```java
// 0. Set your private key
String privateKey = "0x6c9ed03816e31...";
// 1. Sign transaction with your private key
TransactionSigner.getInstance(Network.TESTNET).signTransaction(txWithScriptGroups, privateKey);
// 2. Send transaction to CKB node
byte[] txHash = ckbApi.sendTransaction(txWithScriptGroups.txView);
System.out.println(Numeric.toHexString(txHash));
```

### Generate a new address
In CKB world, a lock script can be represented as an address. `secp256k1_blake160` is the most common used address and here we show how to generate it.

```java
// Generate a new address randomly
ECKeyPair keyPair = Keys.createEcKeyPair();
Script script = Script.generateSecp256K1Blake160SignhashAllScript(keyPair));
Address address = new Address(script, Network.TESTNET);
System.out.println(address.encode());
```

For more details please about CKB address refer to [CKB rfc 0021](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md).

### Convert public key to address

Convert elliptic curve public key to an address (`secp256k1_blake160`)

```java
// The public key sent is an elliptic curve public key of compressed format - a 65-length hex (not include hex prefix 0x).
byte[] publicKey = Numeric.hexStringToByteArray("0x24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
Script script = Script.generateSecp256K1Blake160SignhashAllScript(publicKey);
Address address = new Address(script, Network.TESTNET);
System.out.println(address.encode());
```

### Parse address

Short address and full bech32 address are deprecated. The standard address encoded way is bech32m. You can parse address
from an encoded string address and then get its network, script and encoded string of other format.

```java
Address address = Address.decode("ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g");
Script script = address.getScript();
Network network = address.getNetwork();
System.out.println(address.encode());
```

## Contributing

See [CONTRIBUTING.md](./CONTRIBUTING.md).

## License

The SDK is available as open source under the terms of
the [MIT License](https://opensource.org/licenses/MIT).

## Changelog

See [CHANGELOG](CHANGELOG.md) for more information.
