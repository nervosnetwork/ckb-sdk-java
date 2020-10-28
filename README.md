# CKB SDK Java

[![License](https://img.shields.io/badge/license-MIT-green)](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/LICENSE)
[![Github Actions CI](https://github.com/nervosnetwork/ckb-sdk-java/workflows/CI/badge.svg?branch=develop)](https://github.com/nervosnetwork/ckb-sdk-java/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.nervos.ckb/ckb/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.nervos.ckb/ckb)
[![Telegram Group](https://cdn.rawgit.com/Patrolavia/telegram-badge/8fe3382b/chat.svg)](https://t.me/nervos_ckb_dev)

Java SDK for Nervos [CKB](https://github.com/nervosnetwork/ckb).

The ckb-sdk-java is still under development and **NOT** production ready. You should get familiar with CKB transaction structure and RPC before using it.

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

#### Install manually

You can generate the jar and import manually.

```shell
git clone https://github.com/nervosnetwork/ckb-sdk-java.git

cd ckb-sdk-java

gradle shadowJar  // ./gradlew shadowJar 
```

##### version <= 0.24.0

A `console-{version}-all.jar` package will be generated in `console/build/libs`, which you can put into your project to develop with it.

##### version >= 0.24.1

A `ckb-sdk-{version}-all.jar` package will be generated in `ckb-sdk/build/libs`, which you can put into your project to develop with it.

If you don't want to generate the jar by yourself, you can download a build from [releases](https://github.com/nervosnetwork/ckb-sdk-java/releases).

#### Import Jar to Project

When you need to import `ckb-java-sdk` dependency to your project, you can add the `console-{version}-all.jar` or `ckb-sdk-{version}-all.jar` to your project `libs` package. 

If you use Java IDE (eg. IntelliJ IDEA or Eclipse or other Editors), you can import jar according to IDE option help documents.

### Usage

#### JSON-RPC

You can make JSON-RPC request to your CKB node URL with this SDK. Below are some examples:

```Java
Api api = new Api("your-ckb-node-url");

// using RPC `get_tip_block_number`, it will return the latest block number
BigInteger blockNumber = api.getTipBlockNumber();

// using RPC `get_block_hash` with block number as parameter, it will return block hash
String blockNumber = "0"
String blockHash = api.getBlockHash(blockNumber);

// using RPC `get_block` with block hash as parameter, it will return block object
Block block = api.getBlock(blockHash);

```

You can see more JSON-RPC requests from [RPC Document](https://github.com/nervosnetwork/ckb/blob/develop/rpc/README.md)

#### Single-sig Transfer

> Note: If you want to run transfer example, you should update example private key of sender whose balance is not zero. 
> And if you want to use example default private key to run, you should make the example sender's balance is not zero or set the blake160 of default sender's public key to CKB dev chain node configuration file to be a miner.

[SingleSigWithCkbIndexerTxExample](https://github.com/nervosnetwork/ckb-sdk-java/tree/develop/example/src/main/java/org/nervos/ckb/SingleSigWithCkbIndexerTxExample.java) provides `sendCapacity` method with any amount inputs which belong to a private key.

[MultiKeySingleSigTxExample](https://github.com/nervosnetwork/ckb-sdk-java/tree/develop/example/src/main/java/org/nervos/ckb/MultiKeySingleSigTxExample.java) provides `sendCapacity` method with any amount inputs which belong to any amount private keys.

You can reference detail example in `example/MultiKeySingleSigTxExample.java`.

```Java
  Api api = new Api("your-ckb-node-url");

  List<CellInput> inputs = Arrays.asList(
    new CellInput(inputs1), // Input from address 'cktxxx', capacity 100 CKB
    new CellInput(inputs2), // Input from address 'cktxxx', capacity 200 CKB
    new CellInput(inputs3), // Input from address 'cktxxx', capacity 300 CKB
  );
  
  List<CellOutput> outputs = Arrays.asList(
    output1, // Output to address 'cktxxx', capacity 200
    output2, // Output to address 'cktxxx', capacity 300
    output3, // Output to address 'cktxxx' as change, capacity 100
  );
  
  TransactionBuilder txBuilder = new TransactionBuilder(api);
  
  SignatureBuilder signBuilder = new SignatureBuilder(txBuilder.buildTx());
  
  // A script group is defined as scripts that share the same hash.
  for (ScriptGroup scriptGroup : scriptGroups) {
    signBuilder.sign(scriptGroup);
  }
  
  String hash = api.sendTransaction(signBuilder.buildTx());
```

#### Multi-sig Transfer

> Note: If you want to run transfer example, you should update example private key of sender whose balance is not zero. 
> And if you want to use example default private key to run, you should make the example sender's balance is not zero or set the blake160 of default sender's public key to CKB dev chain node configuration file to be a miner.

[SendToMultiSigAddressTxExample](https://github.com/nervosnetwork/ckb-sdk-java/tree/develop/example/src/main/java/org/nervos/ckb/SendToMultiSigAddressTxExample.java) provides `sendCapacity` method which single-sig address sends capacity to 2/3 format multi-sig address.

[MultiSignTransactionExample](https://github.com/nervosnetwork/ckb-sdk-java/tree/develop/example/src/main/java/org/nervos/ckb/MultiSignTransactionExample.java) provides `sendCapacity` method which 2/3 format multi-sig address sends capacity to single-sig address.

#### Address

You can generate ckb address through this SDK as below:

```Java
// Generate mainnet address with SECP256K1 and public blake160 hash
String publicKey =
    Sign.publicKeyFromPrivate(
            Numeric.toBigInt(
                "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"),
            true)
        .toString(16);
Script script =
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            Hash.blake160(publicKey),
            Script.TYPE);
String address = AddressGenerator.generate(Network.MAINNET, script);

```

### Development

We use [Google Java Code Format](https://google.github.io/styleguide/javaguide.html#s4.5-line-wrapping) and follow [Google Checkstyle](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml) for development.

If `verifyGoogleJavaFormat FAILED` happens when you build this project, please format your code with [Google Java Code Format](https://google.github.io/styleguide/javaguide.html#s4.5-line-wrapping) 
or execute `./gradlew goJF` on macOS and Linux,  or `gradlew goJF` on Windows.

If you use IntelliJ IDEA to develop, you can install `google-java-format` plugin to format code automatically.

## License

The SDK is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).

## Changelog

See [CHANGELOG](CHANGELOG.md) for more information.
