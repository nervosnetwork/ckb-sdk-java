## Example

CKB Java SDK examples 

### Introduction

Example module provides some examples which are related to RPC, transaction and Nervos DAO(deposit and withdraw). 

#### RPC Example

[RpcExample](https://github.com/nervosnetwork/ckb-sdk-java/tree/develop/example/src/main/java/org/nervos/ckb/RpcExample.java) is very simple 
and provides some rpc requests usages. If you want to see more rpc requests, you can reference [CKB RPC Document](https://github.com/nervosnetwork/ckb/blob/develop/rpc/README.md)

Now we just only support synchronous http request which can be found in [Api](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/ckb/src/main/java/org/nervos/ckb/service/Api.java).
If you want to call asynchronous http request, you can use `RpcService.postAsync` to package rpc api whose name may be called `ApiAsync`.

#### Transaction Examples

Transaction examples include simple signature transaction, multiply private keys transaction and multi-sig transaction and transaction examples use same cell collector(except SingleSigWithIndexerTxExample) to collect live cells to build transaction 
and use Secp256K1 signature builder([Secp256k1SighashAllBuilder](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/ckb/src/main/java/org/nervos/ckb/transaction/Secp256k1SighashAllBuilder.java) and [Secp256k1MultisigAllBuilder](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/ckb/src/main/java/org/nervos/ckb/transaction/Secp256k1MultisigAllBuilder.java)) to sign transaction.

> Note: After sending capacity or calling `index_lock_hash` rpc request, you should wait a moment (N*BlockTime) to make transaction into blockchain or indexer execute finish

- Simple signature transaction ([SingleKeySingleSigTxExample](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/example/src/main/java/org/nervos/ckb/SingleKeySingleSigTxExample.java) and [SingleSigWithIndexerTxExample](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/example/src/main/java/org/nervos/ckb/SingleSigWithIndexerTxExample.java))
   - Execute main method to run transaction example and log will be displayed in console window
   - Support single private key to sign transaction with any number of inputs and outputs
   - Support two rpc methods to collect live cells which are `get_cells_by_lock_hash`(collect live cells block by block) and `get_live_cells_by_lock_hash`(collect live cells page by page)
   
- Multi private keys signature transaction ([MultiKeySingleSigTxExample](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/example/src/main/java/org/nervos/ckb/MultiKeySingleSigTxExample.java))
   - Execute main method to run transaction example and log will be displayed in console window
   - Support multi private keys to sign transaction with any number of inputs and outputs
   - Use rpc `get_cells_by_lock_hash` to collect live cells
   
- Multi-sig transaction ([MultiSignTransactionExample](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/example/src/main/java/org/nervos/ckb/MultiSignTransactionExample.java))
   - Execute main method to run transaction example and log will be displayed in console window
   - Multi-sig transaction is different from multi private keys signature transaction
   - Multi-sig transaction use multi private keys to sign the same input and multi private keys signature transaction only use one private key to sign one input
   - Multi-sig has himself address format which can reference [CKB Address Format RFC](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md)
   
- Simple address sending capacity to multi-sig address transaction ([SendToMultiSigAddressTxExample](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/example/src/main/java/org/nervos/ckb/SendToMultiSigAddressTxExample.java))
   - Execute main method to run transaction example and log will be displayed in console window
   - Simple address use single signature to sign the transaction whose receive address is multi-sig address
   
 #### Nervos DAO Example
 
 Nervos DAO is a smart contract, with which users can interact the same way as any smart contract on CKB. Nervos DAO has deposit and withdraw(phase1 and phase2). 
 
 - Deposit: Users can send a transaction to deposit CKB into Nervos DAO at any time. CKB includes a special Nervos DAO type script in the genesis block.
 
 - Withdraw: Users can send a transaction to withdraw deposited CKB from Nervos DAO at any time(but a locking period will be applied to determine when exactly the tokens can be withdrew). Withdraw is a 2-phase process:
    - In phase 1, the first transaction transforms a Nervos DAO deposit cell into a Nervos DAO withdrawing cell.
    - In phase 2, a second transaction will be used to withdraw tokens from Nervos DAO withdrawing cell.
    
 If you want to know more information about Nervos DAO, please reference [Nervos DAO RFC](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0023-dao-deposit-withdraw/0023-dao-deposit-withdraw.md)
    
 Withdraw phase1 transaction must be waiting 4 epoch(header_deps cell has 4 epoch mature period) after deposit transaction and withdraw phase2 transaction must be waiting 180 epoch.
 [NervosDaoExample](https://github.com/nervosnetwork/ckb-sdk-java/blob/develop/example/src/main/java/org/nervos/ckb/NervosDaoExample.java) provides three examples which include deposit and withdraw phase1 / phase2 transaction.
 
 - When you execute deposit transaction, you need give a string parameter `deposit` to main method.
 - When you execute withdraw phase1 transaction, you need give two string parameters `withdraw` and `${deposit_transction_hash}` to main method.
 - When you execute withdraw phase2 transaction, you need give two string parameters `claim` and `${withdraw_phase1_transaction_hash}` to main method.