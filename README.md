# CKB SDK Java

[![Build Status](https://travis-ci.com/nervosnetwork/ckb-sdk-java.svg?branch=develop)](https://travis-ci.com/nervosnetwork/ckb-sdk-java)

Java SDK for Nervos [CKB](https://github.com/nervosnetwork/ckb).

### Prerequisites

* Java 8
* Gradle 5.0 or later

### Installation

You can generate the jar and import manually.
```shell
git clone https://github.com/nervosnetwork/ckb-sdk-java.git
gradle shadowJar  // ./gradlew shadowJar 
```
You will get `console-{version}-all.jar` from console module and put jar package to your project to develop.

### Usage

#### JSON-RPC

You can call ckb JSON-RPC request through this SDK from your ckb node url and there is some examples as below:

```Java
CkbService ckbService = CKBService.build(new HttpService("your-ckb-node-url"));

// using RPC `get_tip_block_number`, it will return latest block number
BigInteger blockNumber = ckbService.getTipBlockNumber().send().getBlockNumber();

// using RPC `get_block_hash` with block number as parameter, it will return block hash
String blockNumber = "0"
String blockHash = ckbService.getBlockHash(blockNumber).send().getBlockHash();

// using RPC `get_block` with block hash as parameter, it will return block object
Block block = ckbService.getBlock(blockHash).send().getBlock();

```

You can see more JSON-RPC requests from [RPC Document](https://github.com/nervosnetwork/ckb/blob/develop/rpc/README.md)

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

String address = utils.generate(publicKey);
```

### Development

We use [Google Java Code Format](https://google.github.io/styleguide/javaguide.html#s4.5-line-wrapping) and [google checkstyle](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml) to develop.

If `verifyGoogleJavaFormat FAILED` happens when you build this project, please format your code with [Google Java Code Format](https://google.github.io/styleguide/javaguide.html#s4.5-line-wrapping) 
or execute `./gradlew goJF` on macOS and Linux,  or `gradlew goJF` on Windows.

If you use IntelliJ IDEA to develop, you can install `google-java-format` plugin to format code automatically.

## License

The SDK is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).

## Changelog

See [CHANGELOG](CHANGELOG.md) for more information.
