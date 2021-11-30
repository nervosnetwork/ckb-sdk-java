# CKB SDK Java

[![License](https://img.shields.io/badge/license-MIT-green)](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/LICENSE)
[![Github Actions CI](https://github.com/nervosnetwork/ckb-sdk-java/workflows/CI/badge.svg?branch=develop)](https://github.com/nervosnetwork/ckb-sdk-java/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.nervos.ckb/ckb/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.nervos.ckb/ckb)
[![Telegram Group](https://cdn.rawgit.com/Patrolavia/telegram-badge/8fe3382b/chat.svg)](https://t.me/nervos_ckb_dev)

Java SDK for Nervos [CKB](https://github.com/nervosnetwork/ckb).

The ckb-sdk-java is still under development and **NOT** production ready. You should get familiar
with CKB transaction structure and RPC before using it.

**Note: All RPC methods in the indexer module have been deprecated since CKB version `v0.36.0` and
they have been removed in the CKB version of `v0.40.0`. We strongly recommend migrating to
the [ckb-indexer](https://github.com/nervosnetwork/ckb-indexer) as soon as possible. You can refer
to
the [examples](https://github.com/nervosnetwork/ckb-sdk-java/tree/develop/example/src/main/java/org/nervos/ckb)
of the [ckb-indexer](https://github.com/nervosnetwork/ckb-indexer) in this project.**

### Prerequisites

* Java 8
* Gradle 5.0 or later

### Installation

#### Install from repositories:

##### version <= 0.24.0

- Maven

```
<dependency>
  <groupId>org.nervos.ckb</groupId>
  <artifactId>core</artifactId>
  <version>{version}</version>
</dependency>
```

- Gradle

```
implementation 'org.nervos.ckb:core:{version}'
```

##### version >= 0.24.1

- Maven

```
<dependency>
  <groupId>org.nervos.ckb</groupId>
  <artifactId>ckb</artifactId>
  <version>{version}</version>
</dependency>
```

- Gradle

```
implementation 'org.nervos.ckb:ckb:{version}'
```

##### version >= 0.43.0

- Maven

```
<dependency>
  <groupId>org.nervos.ckb</groupId>
  <artifactId>ckb-api</artifactId>
  <version>{version}</version>
</dependency>
```

- Gradle

```
implementation 'org.nervos.ckb-api:ckb:{version}'
```

#### Install manually

You can generate the jar and import manually.

```shell
git clone https://github.com/nervosnetwork/ckb-sdk-java.git

cd ckb-sdk-java

gradle shadowJar  // ./gradlew shadowJar 
```

##### version <= 0.24.0

A `console-{version}-all.jar` package will be generated in `console/build/libs`, which you can put
into your project to develop with it.

##### version >= 0.24.1

A `ckb-sdk-{version}-all.jar` package will be generated in `ckb-sdk/build/libs`, which you can put
into your project to develop with it.

If you don't want to generate the jar by yourself, you can download a build
from [releases](https://github.com/nervosnetwork/ckb-sdk-java/releases).

#### Import Jar to Project

When you need to import `ckb-java-sdk` dependency to your project, you can add
the `console-{version}-all.jar`
or `ckb-sdk-{version}-all.jar` to your project `libs` package.

If you use Java IDE (eg. IntelliJ IDEA or Eclipse or other Editors), you can import jar according to
IDE option help documents.

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
Block block = ckbApi.getBlock("0x77fdd22f6ae8a717de9ae2b128834e9b2a1424378b5fc95606ba017aab5fed75");
```

For more details about RPC APIs, please check: 
- [CKB RPC doc](https://github.com/nervosnetwork/ckb/blob/develop/rpc/README.md)
- [CKB-indexer RPC doc](https://github.com/nervosnetwork/ckb-indexer/blob/master/README.md)
- [Mercury RPC doc](https://github.com/nervosnetwork/mercury/blob/main/core/rpc/README.md).

### Mercury
[Mercury](https://github.com/nervosnetwork/mercury) is a development service in CKB ecosystem, providing many useful [RPC APIs](https://github.com/nervosnetwork/mercury/blob/main/core/rpc/README.md) for development like querying transaction or getting udt asset information. You need to deploy your own mercury and sync data with the network before using it.

ckb-java-sdk also integrate with Mercury. For usage guide, please check the [examples](./ckb-api/src/test/java/org/nervos/api/mercury).

### Build transaction

In order to build transaction, you have to collect the live cells that you want to spend at first. In the example below we use CKB indexer to get live cells. You also can get them by manual (e.g. check on [CKB explorer](https://explorer.nervos.org/)) or by your own database.

```java
// Prepare client and utils class
Api ckbApi = new Api("http://127.0.0.1:8114", false);
CkbIndexerApi ckbIndexerApi = new CkbIndexerApi("http://127.0.0.1:8114", false);
TransactionBuilder txBuilder = new TransactionBuilder(ckbApi);
IndexerCollector txUtils = new IndexerCollector(ckbApi, ckbIndexerApi);

// Find live cells and calculate capacity balance with the help of CKB indexer.
BigInteger feeRate = BigInteger.valueOf(1024);
List<String> SendAddresses = Arrays.asList("ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83");
CollectResult collectResult = txUtils.collectInputs(SendAddresses, txBuilder.buildTx(), feeRate, Sign.SIGN_LENGTH * 2);

// Generate cell outputs - the target address.
List<CellOutput> cellOutputs = txUtils.generateOutputs(receivers, changeAddress);
txBuilder.addOutputs(cellOutputs);
// Charge back (if inputs capacity - fee > outputs capacity)
if (Numeric.toBigInt(collectResult.changeCapacity).compareTo(BigInteger.ZERO) > 0) {
    cellOutputs.get(cellOutputs.size() - 1).capacity = collectResult.changeCapacity;
    txBuilder.setOutputs(cellOutputs);
}

// Add witnesses placeholder for latter signature
for (CellsWithAddress cellsWithAddress : collectResult.cellsWithAddresses) {
    txBuilder.addInputs(cellsWithAddress.inputs);
    for (int i = 0; i < cellsWithAddress.inputs.size(); i++) {
        txBuilder.addWitness(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
    }
}
// Build the unsiged transaction
Transaction rawTx = txBuilder.buildTx();
```

A more recommended way is to directly call RPC API `build_simple_transfer_transaction` provided by Mercury, which could help you do almost everything above in building transaction.

### Sign and send transaction
To send transaction you build to CKB network, you need to
1. sign transaction with your private key.
2. send signed transaction to CKB node, and wait it to be confirmed.

```java
// Assume that you already have an unsigned transaction named `rawTx`
Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(rawTx);

// Prepare private key to sign
List<ScriptGroupWithPrivateKeys> scriptGroupWithPrivateKeysList = new ArrayList<>();
scriptGroupWithPrivateKeysList.add(
    new ScriptGroupWithPrivateKeys(
        new ScriptGroup(Arrays.asList(0, 1, 2)),  // indices of input cells that are to be unlocked by this private key
        Arrays.asList("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3")  // your private key
    )
);

// Sign transaction with private key
for (ScriptGroupWithPrivateKeys scriptGroupWithPrivateKeys : scriptGroupWithPrivateKeysList) {
    signBuilder.sign(scriptGroupWithPrivateKeys.scriptGroup, 
        scriptGroupWithPrivateKeys.privateKeys.get(0));
}

// Send transaction
Transaction tx = signBuilder.buildTx();
ckbApi.sendTransaction(tx);;
```

### Generate a new address
In CKB world, a lock script can be represented as an address. `secp256k1_blake160` is the most common used address and here we show how to generate it.

```java
import org.nervos.ckb.utils.address.AddressTools;

// Generate a new address randomly
AddressTools.AddressGenerateResult address = AddressTools.generateAddress(Network.TESTNET);
System.out.println("address info - address: " + address.address
    + ", lockArgs: " + address.lockArgs
    + ", private key: " + address.privateKey);
```

For more details please about CKB address refer to [CKB rfc 0021](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md).

### Convert public key to address

Convert elliptic curve public key to an address (`secp256k1_blake160`)
```java
// The public key sent is an elliptic curve public key of compressed format - a 65-length hex (not counting hex prefix 0x).
String address = AddressTools.convertPublicKeyToAddress(
    Network.TESTNET, "0x24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
```

### Parse and validate address

```java
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressTools;

// validate address
AddressParseResult parseResult = AddressParser.parse("ckt1qg8mxsu48mncexvxkzgaa7mz2g25uza4zpz062relhjmyuc52ps3zn47dugwyk5e6mgxvlf5ukx7k3uyq9wlkkmegke");
System.out.println("address info - network: " + parseResult.network + ", script: " + parseResult.script + ", type: " + parseResult.type);

// `AddressFormatException` will be thrown if you are parsing invalid address
try {
    AddressParseResult parseResult = AddressParser.parse("ckt1qyqz9r9w9gkf5799a497jx07kltx6qqgxv8qn492h3");
} catch (AddressFormatException e) {
    System.out.println("invalid address");
}
```

## Contributing

See [CONTRIBUTING.md](./CONTRIBUTING.md).

## License

The SDK is available as open source under the terms of
the [MIT License](https://opensource.org/licenses/MIT).

## Changelog

See [CHANGELOG](CHANGELOG.md) for more information.
