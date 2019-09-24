# CKB SDK Java

[![License](https://img.shields.io/badge/license-MIT-green)](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/LICENSE)
[![Build Status](https://travis-ci.com/nervosnetwork/ckb-sdk-java.svg?branch=develop)](https://travis-ci.com/nervosnetwork/ckb-sdk-java)
[![Telegram Group](https://cdn.rawgit.com/Patrolavia/telegram-badge/8fe3382b/chat.svg)](https://t.me/nervos_ckb_dev)

Java SDK for Nervos [CKB](https://github.com/nervosnetwork/ckb).

### Prerequisites

* Java 8
* Gradle 5.0 or later

### Installation

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
CkbService ckbService = CKBService.build(new HttpService("your-ckb-node-url"));

// using RPC `get_tip_block_number`, it will return the latest block number
BigInteger blockNumber = ckbService.getTipBlockNumber().send().getBlockNumber();

// using RPC `get_block_hash` with block number as parameter, it will return block hash
String blockNumber = "0"
String blockHash = ckbService.getBlockHash(blockNumber).send().getBlockHash();

// using RPC `get_block` with block hash as parameter, it will return block object
Block block = ckbService.getBlock(blockHash).send().getBlock();

```

You can see more JSON-RPC requests from [RPC Document](https://github.com/nervosnetwork/ckb/blob/develop/rpc/README.md)

#### Transfer

`console/TransactionExample.java` provides `sendCapacityWithSinglePrivateKey` method with inputs which belong to single private key 
and `sendCapacityWithMultiPrivateKey` method with inputs which belong to multi private keys.

You can reference detail example in `console/TransactionExample.java`.

```Java
  private static final String NODE_URL = "http://localhost:8114";
  private static final BigInteger UnitCKB = new BigInteger("100000000");
  private static CKBService ckbService;
  private static List<Account> Accounts;

  static {
    HttpService.setDebug(false);
    ckbService = CKBService.build(new HttpService(NODE_URL));
    Accounts =
        Arrays.asList(
            new Account(
                "08730a367dfabcadb805d69e0e613558d5160eb8bab9d6e326980c2c46a05db2",
                "ckt1qyqxgp7za7dajm5wzjkye52asc8fxvvqy9eqlhp82g"),
            new Account(
                "a202386cb9e46cecff9bc14b748b714c713075dd964c2507c8a8900540164959",
                "ckt1qyqtnz38fht9nvmrfdeunrhdtp29n0gagkps4duhek"),
            new Account(
                "89b773ec5cf97b8fd2cf280ab1e37cd658dc28d84bac8f8dda4a8646cc08d266",
                "ckt1qyqxvnycu7tdtyuejn3mmcnl4y09muxz8c3s2ewjd4"));
  }

  public static void main(String[] args) throws Exception {
      String minerPrivateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
      String minerAddress = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";
      List<Receiver> receivers1 =
          Arrays.asList(
              new Receiver(Accounts.get(0).address, new BigInteger("800").multiply(UnitCKB)),
              new Receiver(Accounts.get(1).address, new BigInteger("900").multiply(UnitCKB)),
              new Receiver(Accounts.get(2).address, new BigInteger("1000").multiply(UnitCKB)));
    
      System.out.println(
          "Before transfer, first receiver1's balance: "
              + getBalance(Accounts.get(0).address).divide(UnitCKB).toString(10)
              + " CKB");
    
      // miner send capacity to three receiver1 accounts with 800, 900 and 1000 CKB
      sendCapacityWithSinglePrivateKey(minerPrivateKey, receivers1, minerAddress);
      Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds
    
      System.out.println(
          "After transfer, first receiver1's balance: "
              + getBalance(Accounts.get(0).address).divide(UnitCKB).toString(10)
              + " CKB");
    
      List<Sender> senders1 =
          Arrays.asList(
              new Sender(Accounts.get(0).privateKey, new BigInteger("500").multiply(UnitCKB)),
              new Sender(Accounts.get(1).privateKey, new BigInteger("600").multiply(UnitCKB)),
              new Sender(Accounts.get(2).privateKey, new BigInteger("700").multiply(UnitCKB)));
      List<Receiver> receivers2 =
          Arrays.asList(
              new Receiver(
                  "ckt1qyqqtdpzfjwq7e667ktjwnv3hngrqkmwyhhqpa8dav",
                  new BigInteger("400").multiply(UnitCKB)),
              new Receiver(
                  "ckt1qyq9ngn77wagfurp29738apv738dqgrpqpssfhr0l6",
                  new BigInteger("500").multiply(UnitCKB)),
              new Receiver(
                  "ckt1qyq2pmuxkr0xwx8kp3ya2juryrygf27dregs44skek",
                  new BigInteger("600").multiply(UnitCKB)));
      String changeAddress = "ckt1qyqfnym6semhw2vzm33fjvk3ngxuf5433l9qz3af8a";
    
      System.out.println(
          "Before transfer, first receiver2's balance: "
              + getBalance(receivers2.get(0).address).divide(UnitCKB).toString(10)
              + " CKB");
    
      // sender1 accounts send capacity to three receiver2 accounts with 400, 500 and 600 CKB
      sendCapacityWithMultiPrivateKey(senders1, receivers2, changeAddress);
      Thread.sleep(30000); // waiting transaction into block, sometimes you should wait more seconds
    
      System.out.println(
          "After transfer, receiver2's balance: "
              + getBalance(receivers2.get(0).address).divide(UnitCKB).toString(10)
              + " CKB");
    }

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

String address = utils.generate(publicKey);
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
