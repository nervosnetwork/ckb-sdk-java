# CKB SDK Java

[![License](https://img.shields.io/badge/license-MIT-green)](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/LICENSE)
[![Build Status](https://travis-ci.com/nervosnetwork/ckb-sdk-java.svg?branch=develop)](https://travis-ci.com/nervosnetwork/ckb-sdk-java)
[![Telegram Group](https://cdn.rawgit.com/Patrolavia/telegram-badge/8fe3382b/chat.svg)](https://t.me/nervos_ckb_dev)

Java SDK for Nervos [CKB](https://github.com/nervosnetwork/ckb).

### Prerequisites

* Java 8
* Gradle 5.0 or later

### Installation

#### Install from repositories:  

- Maven  
```
<dependency>
  <groupId>org.nervos.ckb</groupId>
  <artifactId>core</artifactId>
  <version>{version}</version>
</dependency>
```

Gradle
```
implementation 'org.nervos.ckb:core:{version}'
```

#### Install manually

You can generate the jar and import manually.

```shell
git clone https://github.com/nervosnetwork/ckb-sdk-java.git

cd ckb-sdk-java

gradle shadowJar  // ./gradlew shadowJar 
```
A `console-{version}-all.jar` package will be generated in `console/build/libs`, which you can put into your project to develop with it.

If you don't want to generate the jar by yourself, you can download a build from [releases](https://github.com/nervosnetwork/ckb-sdk-java/releases).

#### Import Jar to Project

When you need to import `ckb-java-sdk` dependency to your project, you can add the `console-{version}-all.jar` to your project `libs` package. 

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

#### Transfer

`console/example/TransactionExample.java` provides `sendCapacity` method with any amount inputs which belong to any amount private keys.

You can reference detail example in `console/example/TransactionExample.java`.

```Java
  Api api = new Api("your-ckb-node-url");

  List<CellInput> inputs = Arrays.asList(
    input1, // Input from address 'cktxxx', capacity 100 CKB
    input2, // Input from address 'cktxxx', capacity 200 CKB
    input3, // Input from address 'cktxxx', capacity 300 CKB
  );
  
  List<CellOutput> outputs = Arrays.asList(
    output1, // Output to address 'cktxxx', capacity 200
    output2, // Output to address 'cktxxx', capacity 300
    output3, // Output to address 'cktxxx' as change, capacity 100
  );
  
  TransactionBuilder builder = new TransactionBuilder(api);
  
  builder.addInputs(inputs);
  
  builder.addOutputs(outputs);
  
  builder.signInput(0, privateKey1);
  builder.signInput(1, privateKey2);
  builder.signInput(2, privateKey3);
  
  String hash = api.sendTransaction(builder.getTransaction());
```

#### Address

You can generate ckb address through this SDK as below:

> There are many address generating methods, and this is just an example.

```Java
// Generate ckb testnet address
AddressUtils utils = new AddressUtils(Network.TESTNET);

// Generate public key from private key through SECP256K1
String publicKey =
    Sign.publicKeyFromPrivate(
            Numeric.toBigInt(
                "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"),
            true)
        .toString(16);

String address = utils.generateFromPublicKey(publicKey);
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
