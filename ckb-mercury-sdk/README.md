## 1. Introduction

[Mercury](https://github.com/nervosnetwork/mercury) 是为开发者提供的一站式代币服务工具。

Nervos CKB 设计的 [Cell 模型](https://docs.nervos.org/docs/basics/concepts/cell-model) 类似比特币的 [UTXO 模型](https://en.wikipedia.org/wiki/Unspent_transaction_output) 。
Cell 是 CKB 的基本单元，就像人体的细胞。所有未被花费的 Cell 构成了整个 CKB 区块链的全局状态。跟 UTXO 不同的是，
Cell 可以用来存储任意类型的数据。CKB 的原生代币 CK Byte（通常也简称为 CKB，为了与 CKB 区块链区分，本文用小写 ckb 表示原生代币）代表全局状态的空间资源。 
你有多少 ckb，就能最多占用多少全局状态空间（1 个 ckb最多可占用 1 Byte 的全局状态空间）。

Nervos 已经推出了自己的代币标准 [sUDT](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0025-simple-udt/0025-simple-udt.md) (simple User-Defined Token)
和 [xUDT](https://talk.nervos.org/t/rfc-extensible-udt/5337) (Extensible User-Defined Token)。
通过这些标准，开发者可以在 Nervos 网络上创建和发行自己的代币。Nervos 设计了特殊的 Cell 类型用于存储代币，这种特殊的 Cell 称为[资产账户](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0026-anyone-can-pay/0026-anyone-can-pay.md)) 。
一个资产账户只能存储一种类型的代币。 资产账户占用了全局状态空间，因而需要一定大小的 ckb（一般为 142 ckb），那么这些 ckb 该由谁来提供呢？
为了适应不同的业务需要，Mercury SDK 提供了多套解决方案：

* 收款方提供资产账户。若收款方已经创建了相应的资产账户，付款方直接向收款方的账户地址转账即可。（该解决方案在主网和测试网均已部署）
* 付款方提供资产账户。若收款方没有创建相应的资产账户，则付款方可以为收款方创建资产账户并转账。资产账户的所有权属于收款方。（该解决方案在主网和测试网均已部署）
* 付款方提供[临时账户](https://talk.nervos.org/t/sudt-cheque-deposit-design-and-implementation/5209) 。若收款方没有创建相应的资产账户，付款方还可以创建一个临时账户（一般需占用 162 ckb），临时账户的所有权属于付款方。收款方须在一定时限内从这个临时账户里转移或花费代币，否则代币将退还收款方。（该解决方案已在测试网部署，主网近期将会部署）

Mercury 集成了这些解决方案，通过调用转账接口，指定 action 参数就可以生成相应解决方案的转账交易（PayByTo -> 收款方提供资产账户, PayByFrom -> 付款方提供资产账户, LendByFrom ->
付款方提供临时账户）。Mercury 还提供了创建资产账户的接口方便为用户生成指定的资产账户。

根据使用限制的不同，我们将用户的代币资金划分成三种类型：

* Unconstrained。没有了任何限制的，可以随时使用的资金。
* Locked。处于锁定状态的资金，这里锁定目前包含两种含义。一是用于创建资产账户或临时账户而被占用的 ckb 属于 Locked 类别。二是锁定期是代币发行中常见的需求，被锁定的代币只能在锁定期过后才能被解锁使用。通过锁定期的资金归属于
  Unconstrained 类别。
* Fleeting。必须在一定时间内转移的代币。付款方创建的临时账户中的代币，在超时前属于收款方的 Fleeting 类别，超时后则属于付款方的 Unconstrained 类别。 收款方或付款方可在转账时将 source 参数设为
  fleeting，从而花费或转移 Fleeting 类别的代币。

Mercury 提供的余额查询接口会按这三种类型分别显示余额。

## 2. mercury 接口

1. 余额查询：查询指定代币和地址的余额，按不同的资金类型显示余额。

``` 
GetBalanceResponse getBalance(String udt_hash, String ident) throws IOException;
// - udt_hash 用于指定代币类型：若 udt_hash 为空，返回 ckb 的余额；若 udt_hash 不为空，则返回指定 udt 代币的余额
// - ident 表示待查询的地址

public class GetBalanceResponse {
    private String unconstrained;
    private String fleeting;
    private String locked;
}
```

2. 构建转账交易：根据指定参数，自动完成凑钱操作并返回拼装好的未签名转账交易以及签名槽位。

支持一个用户对一个或多个用户的转账，不支持多用户对一个或多个用户的转账。支持一个用户使用多个地址。一次转账只能涉及一种类型的代币。

```
TransactionCompletionResponse buildTransferTransaction(TransferPayload payload) throws IOException;

public class TransferPayload {
    private String udt_hash;   
    // - udt_hash 用于指定代币类型：若 udt_hash 为空，表示 ckb 转账；若 udt_hash 不为空，则表示指定 udt 代币的转账
    private FromAccount from;
    // - from 表示付款方的账号信息，只支持一个付款方
    private List<TransferItem> items;
    // - items 表示收款方的信息，可以支持多个收款方
    private String change;
    // - change 表示找零地址：若 change 为空，则默认将 from 的第一个地址作为找零地址。
    private BigInteger fee;
    // - fee 表示交易费，金额单位是 Shannon，1 ckb = 10^8 Shannon
}

public class FromAccount {
    private List<String> idents;
    // - idents 表示付款方的付款地址，支持同时使用多个地址付款
    private Source source;
    // -source 用于指定用于付款的代币资金类型，包括两个选项：unconstrained 和 fleeting。
}

public enum Source {
    unconstrained,
    fleeting,
}

public class TransferItem {
    private ToAccount to;
    // - to 表示收款方的收款信息 
    private BigInteger amount;
    // - amount 表示转账金额，金额单位是 ckb
}

public class ToAccount {
    private String ident;
    // - ident 表示收款方的地址
    private Action action;
    // - action 指定收款方的账户由谁提供，包括三种选项：pay_by_from，lend_by_from 和 pay_by_to
}

public enum Action {
    pay_by_from,
    lend_by_from,
    pay_by_to
}

public class TransactionCompletionResponse {
    private Transaction tx_view;
    // - tx_view 表示接口返回的未签名的交易内容
    private List<SignatureEntry> sigs_entry;
    // - sigs_entry 表示签名槽位，提供用于签名的必要信息
}
```

3. 构建创建资产账户交易：自动完成凑钱操作并返回拼装好的未签名的创建资产账户的交易以及签名槽位。

```
TransactionCompletionResponse buildWalletCreationTransaction(CreateWalletPayload payload) throws IOException;

public class CreateWalletPayload {
    private String ident;
    // - ident 表示用于创建资产账户的地址，该地址必须有足够创建资产账户的 ckb（一般是 142 ckb）
    private List<WalletInfo> info;
    // - info 表示资产账户的资产类型
    private BigInteger fee;
    // - fee 表示交易费，金额单位是 Shannon，1 ckb = 10^8 Shannon
}

public class WalletInfo {
    private String udt_hash;
    // - udt_hash 指定资产账户的代币类型，不能为空
}
```

## 3. 使用示例

1. [查询账户余额](./src/test/java/mercury/BalanceTest.java)
2. [Action](./src/test/java/mercury/ActionTest.java)
3. [转账](./src/test/java/mercury/TransferCompletioTest.java)
4. [创建资产账户](./src/test/java/mercury/CreateWalletTest.java)
