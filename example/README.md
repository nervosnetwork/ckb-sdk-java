## Example

CKB Java SDK examples

### Introduction

The example module provides some examples which are related to RPC, transaction and Nervos DAO(deposit and withdraw).

Now we just only support synchronous http request which can be found in [Api](..//ckb/src/main/java/org/nervos/ckb/service/Api.java).
If you want to call asynchronous http request, you can use `RpcService.postAsync` by yourself to package rpc api whose name may be called `ApiAsync`.

### Transaction Examples

Transaction examples include simple signature transaction and multi-sig transaction to transfer CKB.

- [SendCkbExample](./src/main/java/org/nervos/ckb/example/SendCkbExample.java): sign and send CKB from single-sig address.
- [SendCkbFromMultisigAddressExample](./src/main/java/org/nervos/ckb/example/SendCkbFromMultisigAddressExample.java): sign and send CKB from multi-sig address with multiple private keys.

### SUDT Example

[SUDT](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0025-simple-udt/0025-simple-udt.md) (Simple User Defined Tokens) is a token specification on CKB. You can think SUDT is an analog of ERC20 on Ethereum.

Anyone can issue his own SUDT, or transfer a specific kind of SUDT if he has enough SUDT amount. SUDT smart contract should be used in type script in these transactions.

Check the example [issueSudtExample](./src/main/java/org/nervos/ckb/example/issueSudtExample.java) and [SendSudtExample](./src/main/java/org/nervos/ckb/example/SendSudtExample.java) to learn how to issue and transfer SUDT.

### Nervos DAO Examples

Nervos DAO is a smart contract, with which users can interact the same way as any smart contracts on CKB. Nervos DAO has deposit and withdraw (phase1 and phase2).

- Deposit: Users can send a transaction to deposit CKB into Nervos DAO at any time. CKB includes a special Nervos DAO type script in the genesis block.
- Withdraw: Users can send a transaction to withdraw deposited CKB from Nervos DAO at any time (but a locking period will be applied to determine when exactly the tokens can be withdrawn). Withdraw is a 2-phase process:
  - In phase 1, the first transaction transforms a Nervos DAO deposit cell into a Nervos DAO withdrawing cell.
  - In phase 2, a second transaction will be used to withdraw tokens from Nervos DAO withdrawing cell. Withdraw phase 2 transaction must wait at least 180 epoch after deposit transaction

For more details about Nervos DAO, please refer to [Nervos DAO RFC](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0023-dao-deposit-withdraw/0023-dao-deposit-withdraw.md)

We provide examples for these three DAO operations

- [DaoDepositExample](./src/main/java/org/nervos/ckb/example/DaoDepositExample.java)
- [DaoWithdrawExample](./src/main/java/org/nervos/ckb/example/DaoWithdrawExample.java)
- [DaoClaimExample](./src/main/java/org/nervos/ckb/example/DaoClaimExample.java)

Note: to make withdraw or claim transaction you need to know the outpoint of the deposit or withdraw dao cell.