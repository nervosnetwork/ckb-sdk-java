# [v0.101.0-beta.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.43.3-beta.4...v0.101.0-beta.1) (2021-11-01)

### Features

* Add new rpc build_dao_claim_transaction, impl by following the RFC
  withdraw-phase-2 ([#483](https://github.com/nervosnetwork/ckb-sdk-java/pull/483))

### Breaking Changes

* Rename rpc `build_deposit_transaction`
  to `build_dao_deposit_transaction` ([#483](https://github.com/nervosnetwork/ckb-sdk-java/pull/483))
* Rename rpc `build_withdraw_transaction`
  to `build_dao_withdraw_transaction` ([#483](https://github.com/nervosnetwork/ckb-sdk-java/pull/483))
* Building tx output support witnesses filling and new signature
  actions ([#481](https://github.com/nervosnetwork/ckb-sdk-java/pull/481)
  , [#482](https://github.com/nervosnetwork/ckb-sdk-java/pull/482))
* When sending a transaction, use passthrough as the default
  parameter ([#479](https://github.com/nervosnetwork/ckb-sdk-java/pull/479))
  
