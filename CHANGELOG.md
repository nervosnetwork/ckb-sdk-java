# 3.0.0 (2024-11-26)
- feat: support indexer filter script_len_range
- feat: support v0.119.0 rpcs #683
- feat: add script hash type data2
- feat: force small change as fee
- fix: Make dao handler get correct withdraw block hash
- feat: support type id handler


# 2.1.1 (2023-03-23)
- feat: support with_cycles for get_block and get_block_by_number rpc (#623)
- feat: support packed rpcs (#624)
- feat: support indexer exact search mod (#627)

## 🚀 Features
# 2.1.0 (2022-12-26)

## 🚀 Features

- feat: support omnilock script (#590)
- feat: support building transaction in dev chain (#591)
- feat: support indexer RPC in ckb (#601)
- feat: support light client RPC (#602)
- feat: support offchain cell (#603, 606)
- feat: support field extra in mercury RPC get_balance (#605)
- feat: support RPC method estimate_cycles (#608)
- feat:support rpc method get fee rate statics (#611)
- feat: guitar Make ckb-indexer can configure which api url to use (#613)

# 2.0.3 (2022-07-13)

## 🐛 Bug Fixes

- fix: fix signer test fixture (#575)
- fix: fix incorrect index at method `encodeFullBech32` (#576)

# 2.0.2 (2022-06-24)

## 🚀 Features

- feat: enhance mercury builder (#570)

# 2.0.1 (2022-06-16)

## 🚀 Features

- feat: make mercury enum public (#566)
- feat: add more easy-use mercury tool methods (#565)

# 2.0.0 (2022-06-14)

2.0.0 is a refactored ckb-java-sdk release and brings plenty of BREAKING CHANGES compared with `v1.0.*` and the earlier releases.

Breaking changes related with user interfaces

- Type or name change of quite a few fields in RPC type representation.
- Unified address representation and operation.
- Transaction signing mechanism by `ScriptGroup`, `ScriptSigner`, and `TransactionSigner`.
- `TransactionBuilder` and `ScriptHandler` for transaction construction by manual.
- Operation support for most common scripts.
- Clean some utils classes and unused classes.

Underlying breaking changes that less likely need users' changes

- Molecule serialization in a code-generated way.
- Unify type adapter for Gson serialization.
- More robust test.

Check [migration-guide.md](./docs/migration-guide.md) for more details.

# 1.0.0 (2022-03-17)

## Breaking Changes

- feat: Replace Record Id with Outpoint (#534)

# 0.101.3 (2022-01-21)

## 🚀 Features

- feat: support pw lock signature (#525)
- feat: support mercury get\_sync\_state RPC API (#522)
# 0.101.2 (2021-12-16)

## 🚀 Features

- feat: add method for legacy address conversion (#514) @fjchen7

## 🐛 Bug Fixes

- fix: improve address validation (#515) @fjchen7
- fix: fix PR labler not permission (#516) @fjchen7

## 🧰 Maintenance

- chore: fix release github action (#513) @fjchen7

## 0.101.1 (2021-12-01)

## 🚀 Features

- feat: adopt full address by default (#506) @fjchen7
- feat: add enum Freeze in extraFilter (#508) @fjchen7
- feat(mercury): support `build_sudt_issue_transaction` (#501) @handsome0hell
- feat(mercury): add build\_sudt\_issue\_transaction test (#502) @rev-chaos

## 🐛 Bug Fixes

- fix: replace short address with bech32m full address in test (#511) @fjchen7

## 🧰 Maintenance

- chore: label PR based on title (#507) @fjchen7
- chore: add CICD github actions (#505) @fjchen7

## 📝 Document

- docs: update README for default address (#510) @fjchen7

# [v0.101.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.101.1...v0.101.0-beta.1) (2021-12-01)
### Feature

- Support `build_sudt_issue_transaction` ([#501](https://github.com/nervosnetwork/ckb-sdk-java/pull/501))
- Add enum Freeze for extra ([#508](https://github.com/nervosnetwork/ckb-sdk-java/pull/508))
- Adopt full address by default ([#501](https://github.com/nervosnetwork/ckb-sdk-java/pull/501), [#511](https://github.com/nervosnetwork/ckb-sdk-java/pull/511))

### Chore

- Label PR based on title ([#508](https://github.com/nervosnetwork/ckb-sdk-java/pull/508))

# [v0.101.0-beta.2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.101.0-beta.1...v0.101.0-beta.2) (2021-11-17)

### Feature
- Enhance README.md ([#490](https://github.com/nervosnetwork/ckb-sdk-java/pull/490), [#494](https://github.com/nervosnetwork/ckb-sdk-java/pull/494), [#497](https://github.com/nervosnetwork/ckb-sdk-java/pull/497))
- Add address generation methods ([#494](https://github.com/nervosnetwork/ckb-sdk-java/pull/494), [#496](https://github.com/nervosnetwork/ckb-sdk-java/pull/496))
- Add timestamp in RPC response ([#489](https://github.com/nervosnetwork/ckb-sdk-java/pull/489))
- Make ckb-indexer interface compatible ([#491](https://github.com/nervosnetwork/ckb-sdk-java/pull/491))

### Breaking Changes
- Rename `build_smart_transfer_transction` ([#488](https://github.com/nervosnetwork/ckb-sdk-java/pull/488))
- Change RPC response structure ([#492](https://github.com/nervosnetwork/ckb-sdk-java/pull/492), [#493](https://github.com/nervosnetwork/ckb-sdk-java/pull/493), [#495](https://github.com/nervosnetwork/ckb-sdk-java/pull/495), [#497](https://github.com/nervosnetwork/ckb-sdk-java/pull/497))

### Bugfix
- Fix key invalidation error ([#487](https://github.com/nervosnetwork/ckb-sdk-java/pull/487))

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

# [v0.100.0-beta.2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.100.0-beta.1...v0.100.0-beta.2) (2021-10-13)

### Bug Fixes

* Fix the case that script type data1 is not taken into account during bech32m serialization and
  deserialization ([#473](https://github.com/nervosnetwork/ckb-sdk-java/pull/473))

# [v0.100.0-beta.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.43.3-beta.3...v0.100.0-beta.1) (2021-10-10)

### Features

* The field hash_type has a new allowed value `data1` but it is only valid after hard fork
  activation
* Add support for Bech32m

### Breaking Changes

* The field `uncles_hash` in header will be renamed to `extra_hash` for all JSON RPC methods

# [v0.43.3-beta.4](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.43.3-beta.3...v0.43.3-beta.4) (2021-10-13)

### Bugfix

* Fix a serialization error
  in `GetTransactionInfoResponse` ([#474](https://github.com/nervosnetwork/ckb-sdk-java/pull/474))

# [v0.43.3-beta.2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.43.3-beta.1...v0.43.3-beta.2) (2021-09-27)

### Refactor

* Refactor nameing
  field ([078c9e7](https://github.com/nervosnetwork/ckb-sdk-java/commit/078c9e7a2f49b29cef4f1a33d1af114cc91ae6c3))

### Bugfix

* Fix `register_address` url
  error ([4eba9b2](https://github.com/nervosnetwork/ckb-sdk-java/commit/4eba9b284f9b4650733ad09839ab9b4cfaced252))
* Fix serialization error
  in `get_spent_transaction`([ca4599a](https://github.com/nervosnetwork/ckb-sdk-java/commit/ca4599aba283ac345c2eb541f8796b758aed7a3c))
* Fix `ExtraFilter` structure definition
  problem ([f4cc990](https://github.com/nervosnetwork/ckb-sdk-java/commit/f4cc990180bb48f983465de2106d459e5e8a36ca))
* Fix `Record` structure definition
  problem ([532d17](https://github.com/nervosnetwork/ckb-sdk-java/commit/5532d17e3741bad3525cb501d2fa4565e02fca17))

# [v0.43.3-beta.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.43.2...v0.43.3-beta.1) (2021-09-22)

### Features

* Add `get_spent_transaction`
  support ([bc744ab](https://github.com/nervosnetwork/ckb-sdk-java/commit/bc744abde786b09cffee0c440dbd405a8b6300ba))
* Add `dao`
  support ([6eccbf0](https://github.com/nervosnetwork/ckb-sdk-java/commit/6eccbf0e438b19bd94a9af405c9ee2690782710d))
* Add `get_mercury_info`
  api ([7346d3d](https://github.com/nervosnetwork/ckb-sdk-java/commit/7346d3dffeddde6d645aa1c1b4af689724c08e55))
* Add `get_db_info`
  api ([28d3f20](https://github.com/nervosnetwork/ckb-sdk-java/commit/28d3f20d322f5af66ae998c0a212316b7b5f71a6))

### Breaking Changes

*

Adjusting `build_smart_transfer_transaction` ([4f2683d](https://github.com/nervosnetwork/ckb-sdk-java/commit/4f2683df83f98a685192cf988566803619f741b7))

* Adjusting `query_transactions`
  interface ([9d15fed](https://github.com/nervosnetwork/ckb-sdk-java/commit/9d15fed6e1cf89ce522054014b9ee60b54117ccf))
* Adjusting `build_transfer_transaction`
  interface ([0ce8890](https://github.com/nervosnetwork/ckb-sdk-java/commit/0ce88901bf770eeab2d209519a978fed9310177d))
* Adjusting `build_adjust_account_transaction`
  interface ([aa89d7a](https://github.com/nervosnetwork/ckb-sdk-java/commit/aa89d7a41adc216f5a1255f3dbf0066a88a5d8f8) [ca7efe5](https://github.com/nervosnetwork/ckb-sdk-java/commit/ca7efe545e08f730b5ec0b9139d35e78f0393ef5))
* Adjusting `get_block_info`
  interface（[09c3e38](https://github.com/nervosnetwork/ckb-sdk-java/commit/09c3e38990f5df2bd7ebaae4d1a876a05795dc2f))
* Adjusting `get_transaction_info`
  interface ([ef6ece4](https://github.com/nervosnetwork/ckb-sdk-java/commit/ef6ece46530a172fb1fec444376cea11bb1872fc))
* Adjusting `balance`
  interface ([ee1f367](https://github.com/nervosnetwork/ckb-sdk-java/commit/ee1f367eaace231c0ca04660615f3e6f89598b35))
* remove `build_asset_collection_transaction`
  interface ([aa89d7a](https://github.com/nervosnetwork/ckb-sdk-java/commit/aa89d7a41adc216f5a1255f3dbf0066a88a5d8f8)

# [v0.43.2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.43.1...v0.43.2) (2021-09-03)

### Features

* Add `build_smart_transfer_transaction`
  api ([539017a](https://github.com/zhengjianhui/ckb-sdk-java/commit/539017aef3806e0ffcdc87aae8af46258644132b) [1213bdd](https://github.com/nervosnetwork/ckb-sdk-java/pull/438/commits/1213bddc33a0cc0f142d18e848e5549b3180400d) [63f63bb](https://github.com/nervosnetwork/ckb-sdk-java/pull/439/commits/63f63bb25c4a9cf18b291feaedc06fecba0df4fd))

### BreakingChanges

* The uniform unit is
  shannon ([33f63bc](https://github.com/nervosnetwork/ckb-sdk-java/pull/431/commits/33f63bc203f208656b0444a05fe2248f17488518) [9f33b10](https://github.com/nervosnetwork/ckb-sdk-java/pull/435/commits/9f33b10a03f8924c7df6901e9fc6ce97c583820d))
* Adjusting the `build_asset_account_creation_transaction`
  interface ([2f74758](https://github.com/nervosnetwork/ckb-sdk-java/pull/433/commits/2f747586011632d5593741206178ef18d3bad9f6) [f660dbe](https://github.com/nervosnetwork/ckb-sdk-java/pull/433/commits/f660dbeb66c7bd6f7532107675e5085d473bb0f1))

* Adjusting the `get_generic_transaction` and `get_generic_block`
  interface ([acf3ec8](https://github.com/nervosnetwork/ckb-sdk-java/pull/430/commits/acf3ec8dbc3304bc4f29b7ce1009ec338ff333aa) [156876b](https://github.com/nervosnetwork/ckb-sdk-java/pull/430/commits/156876be5a69b7cff0b2806b968e410d487da161) [33a229b](https://github.com/nervosnetwork/ckb-sdk-java/pull/436/commits/33a229b3a00e392a392a3ffed101f88d8620b349))

* Adjusting the `get_balance`
  interface ([2f74758](https://github.com/zhengjianhui/ckb-sdk-java/commit/2f747586011632d5593741206178ef18d3bad9f6))

# [v0.43.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.43.0...v0.43.1) (2021-08-01)

### Features

* Relay ckb rpc request through
  mercury ([1504473](https://github.com/nervosnetwork/ckb-sdk-java/pull/420/commits/150447330614bc81f41a2e973c84d15f5b3cc8d4))
* Add `get_fork_block`
  api ([0755910](https://github.com/nervosnetwork/ckb-sdk-java/pull/426/commits/075591061c9bc266f42686f480dbee3b9f3d879f))
* Add `get_block_median_time`
  api ([83ca4a9](https://github.com/nervosnetwork/ckb-sdk-java/pull/425/commits/83ca4a96427ec0cb1c34afea36a65f1fe9c976f6))

### Bugfix

Fix the return value of the
interface `verify_transaction_proof` ([751f07b](https://github.com/nervosnetwork/ckb-sdk-java/pull/424/commits/751f07bdd73c20ead2968fdc06c2fc7279219aa4))

# [v0.43.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.42.0...v0.43.0) (2021-07-25)

### Features

* [#415](https://github.com/nervosnetwork/ckb-sdk-java/pull/415) Support Mercury version 0.1.0-rc2
* Support CKB version 0.43.0
* [#415](https://github.com/nervosnetwork/ckb-sdk-java/pull/415) Support ckb-indexer version 0.2.1

# [v0.42.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.41.1...v0.42.0) (2021-06-25)

### BreakingChanges

* Remove `get_peers_state` rpc
* Remove `get_cellbase_output_capacity_details` rpc

# [v0.41.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.41.0...v0.41.1) (2021-06-12)

### Bugfix

* Fix JDK version 15 to 8

# [v0.41.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.40.0...v0.41.0) (2021-06-12)

### Feature

* Implement Mercury
  SDK([0a2b5fc](https://github.com/nervosnetwork/ckb-sdk-java/commit/455967c0b4b6304423f82d6b46d279c5e0a2b5fc))

# [v0.40.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.39.1...v0.40.0) (2021-03-08)

### Feature

* Remove indexer module rpc and disable getcellbase_output_capacity_details
  rpc([0872f27](https://github.com/nervosnetwork/ckb-sdk-java/commit/0872f279692aac96006474903fe088f9fbeda2b7))

### BreakingChanges

* Remove indexer module rpc
* Add @Deprecated to get_cellbase_output_capacity_details rpc

# [v0.39.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.39.0...v0.39.1) (2021-01-30)

### Feature

* Add sudt example with
  ckb-indexer([176bfa1](https://github.com/nervosnetwork/ckb-sdk-java/commit/176bfa13855925b9c140d88cbf061aa31b1e650c))
* Add acp example with
  ckb-indexer([c4a2502](https://github.com/nervosnetwork/ckb-sdk-java/commit/c4a2502e2233906edaeaa0554a0de29600c8c6ca))

### Bugfix

* Fix WitnessArgs serialization
  size([bb73a39](https://github.com/nervosnetwork/ckb-sdk-java/commit/bb73a3947ad1ce3822845cdba8cd7b681397d8b2))

# [v0.39.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.38.1...v0.39.0) (2021-01-12)

### Feature

* Add
  rpc `get_raw_tx_pool`([41a5e3d](https://github.com/nervosnetwork/ckb-sdk-java/commit/41a5e3d66f5508176a1fa9697c23596da96c48bc))
* Add
  rpc `get_consensus`([a036153](https://github.com/nervosnetwork/ckb-sdk-java/commit/a036153e11f014d0f2bea84c24439833ca5862f6))

# [v0.38.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.38.0...v0.38.1) (2020-12-16)

### Feature

* Add anyone_can_pay short
  address([5585ad1](https://github.com/nervosnetwork/ckb-sdk-java/commit/5585ad159c329cc079c1170f3c2224d4e95abb99))
* Add note of indexer rpc
  deprecated([e6f91a9](https://github.com/nervosnetwork/ckb-sdk-java/commit/e6f91a99295928c641e5d566a70a4ad57dc2dc7f))

### BugFix

* Update interface RpcCallback
  public([b3f6b61](https://github.com/nervosnetwork/ckb-sdk-java/commit/b3f6b61e155aef3aa22e201408287ca4d253685e))

# [v0.38.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.37.1...v0.38.0) (2020-11-23)

Bump version to v0.38.0

# [v0.37.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.37.0...v0.37.1) (2020-11-10)

### Feature

* Lock bouncycastle version to
  1.65 ([79b870d](https://github.com/nervosnetwork/ckb-sdk-java/commit/79b870db9a27dc76c868e02546726658cbddb662))

# [v0.37.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.36.1...v0.37.0) (2020-10-29)

### Feature

* Add new rpc (`ping_peers`, `clear_banned_addresses`, `get_transaction_proof`
  , `verify_transaction_proof`) ([ad83ea7](https://github.com/nervosnetwork/ckb-sdk-java/commit/ad83ea7b4965f49b92dc5f77918a431d59007e26))

### Refactor

* Remove `get_cells_by_lock_hash` and indexer
  examples ([d70522f](https://github.com/nervosnetwork/ckb-sdk-java/commit/d70522fce4a6722a02ce731e34b37b378b34e6e4))

### BreakingChanges

* Remove `get_cells_by_lock_hash` rpc
* Remove indexer and udt examples and new sudt examples will be added in the next release

# [v0.36.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.36.0...v0.36.1) (2020-09-22)

### BugFix

* Package ckb-sdk.jar with jdk8

# [v0.36.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.35.1...v0.36.0) (2020-09-22)

### Feature

* Add @Deprecated to indexer rpc
  requests ([ef26730](https://github.com/nervosnetwork/ckb-sdk-java/commit/ef26730afbd9401c207c11f343368da53188a2d0))

# [v0.35.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.35.0...v0.35.1) (2020-8-26)

### Feature

* Add a `ckb-indexer`
  example ([0f4eeda](https://github.com/nervosnetwork/ckb-sdk-java/commit/0f4eeda0805fcf8eaa9b9e2b028b35dd246762fc))

### BugFix

* Fix transaction fee calculating
  bug ([6bb687e](https://github.com/nervosnetwork/ckb-sdk-java/commit/6bb687ef4cf2c78582bac138eb002b499e6a1544))
* Fix witnesses updating
  bug ([7873fe4](https://github.com/nervosnetwork/ckb-sdk-java/commit/7873fe43681005aaaf37d05dd0ccfa134efa1ce1))

# [v0.35.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.34.0...v0.35.0) (2020-8-25)

### Feature

* Add `sync_state`
  rpc ([202fda9](https://github.com/nervosnetwork/ckb-sdk-java/commit/202fda9d121fb4477d3934eaa6ce37934960f2c5))
* Add tipHash to
  txPoolInfo([af8c54f](https://github.com/nervosnetwork/ckb-sdk-java/commit/af8c54fd928446d3244826d49deffde6e4d8d02a))
* Add `set_network_active`
  rpc ([bcd2c2b](https://github.com/nervosnetwork/ckb-sdk-java/commit/bcd2c2b041721a7e515060c02ea8a75c09867eea))
* Add add_node and remove_node
  rpc ([7cd8b9b](https://github.com/nervosnetwork/ckb-sdk-java/commit/7cd8b9be7bbdef31077dd27d6abcedf1b689475b))
* Add more fields to node_info and
  peer ([bba542e](https://github.com/nervosnetwork/ckb-sdk-java/commit/bba542e36dce8236c8618aa14db931db1b5cd7fd))
* Set `get_peers_state` as deprecated rpc
  request ([d2c4baf](https://github.com/nervosnetwork/ckb-sdk-java/commit/d2c4bafa9671060badb17e9db8a99ff2e0b8f50f))

# [v0.34.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.33.0...v0.34.0) (2020-7-21)

### Feature

* Add batch rpc
  request ([e5eef53](https://github.com/nervosnetwork/ckb-sdk-java/commit/e5eef53b98f671620f5d8c05c0cc7b1e62f7674f))
* Add `clearTxPool` rpc
  request([64819d9](https://github.com/nervosnetwork/ckb-sdk-java/commit/64819d9e6be352ae193c5ee4da1949868e96426a))
* Remove `estimateFeeRate` rpc
  request ([054bd01](https://github.com/nervosnetwork/ckb-sdk-java/commit/054bd01e22b77591ec0d5735d23f679cc6712e4b))
* Set `computeScriptHash` and `computeTransactionHash` as deprecated rpc
  request ([fec42ec](https://github.com/nervosnetwork/ckb-sdk-java/commit/fec42ec72248d49cb5c62f6933a3d54be35c13dd))

### BugFix

* Check full address payload
  length ([02a425a](https://github.com/nervosnetwork/ckb-sdk-java/commit/02a425ae70110439e8118db8dc9fbbdcc829c05b))
* Fix address parse method
  bug ([cf3997b](https://github.com/nervosnetwork/ckb-sdk-java/commit/cf3997b51baefd44a63d9c449b488c47c1373a74))

### BreakingChanges

* Remove `estimateFeeRate` rpc
  request ([054bd01](https://github.com/nervosnetwork/ckb-sdk-java/commit/054bd01e22b77591ec0d5735d23f679cc6712e4b))

# [v0.33.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.32.0...v0.33.0) (2020-6-22)

Bump version to v0.33.0

# [v0.32.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.31.0...v0.32.0) (2020-5-26)

### Feature

* Add Simple UDT
  example ([18a15f5](https://github.com/nervosnetwork/ckb-sdk-java/commit/18a15f5c4f9d90a62a4b5368a46a94ca89d912e6))

# [v0.31.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.30.0...v0.31.0) (2020-4-17)

### Refactor

* Update cell collector error
  information ([1468d85](https://github.com/nervosnetwork/ckb-sdk-java/commit/1468d850597ff89be5e6daeb654c5eabdbf39e06))

### BugFix

* Update output size calculating
  method ([3e7f6d8](https://github.com/nervosnetwork/ckb-sdk-java/commit/3e7f6d8cf955c30fc4e25d6718d3d60ae5447b34))

# [v0.30.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.29.0...v0.30.0) (2020-3-23)

### Feature

* Add `min_tx_fee` to `tx_pool_info`
  rpc ([d9203d4](https://github.com/nervosnetwork/ckb-sdk-java/commit/d9203d401c008b7c7766e8ec1f55dbe116bdbc47))
* Add `get_block_economic_state`
  rpc ([542dc26](https://github.com/nervosnetwork/ckb-sdk-java/commit/542dc261d5fd683aed8a09aded0144ef51ba96e6))

### Refactor

* Replace test private key with dev chain private
  key ([ad94180](https://github.com/nervosnetwork/ckb-sdk-java/commit/ad941802e9cb2b075f085399007cef16cb866c01))
* Add fromBlockNumber to collectInputs
  method ([3a66ab5](https://github.com/nervosnetwork/ckb-sdk-java/commit/3a66ab54d9ba0dba372f533a5a9a152484120baf))
* Refactor epoch parser class and method
  name ([ca45791](https://github.com/nervosnetwork/ckb-sdk-java/commit/ca45791f6ce1bfb72fe37dd149750fc14311c7b7))

### BugFix

* Fix cell collector
  bug ([80eca88](https://github.com/nervosnetwork/ckb-sdk-java/commit/80eca88fdbcbd8c626ff6f99fa64edbddddfca9d))

# [v0.29.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.28.0...v0.29.0) (2020-2-28)

### Refactor

* Remove useless
  code ([a858559](https://github.com/nervosnetwork/ckb-sdk-java/commit/a858559abd8dee81d0969334b5d6487533545ca6))
* Update transaction fee
  calculating ([004c6e3](https://github.com/nervosnetwork/ckb-sdk-java/commit/004c6e30b571b805ec2905df0c909a1287c6917d))

# [v0.28.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.27.1...v0.28.0) (2020-2-7)

Bump version to v0.28.0

# [v0.27.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.27.0...v0.27.1) (2020-2-3)

### Feature

* Add outputsValidator as parameter to sendTransaction
  rpc ([d6d715e](https://github.com/nervosnetwork/ckb-sdk-java/commit/d6d715e05d0877697f4f78f4f148b74df8d26487))

### BreakingChanges

Add outputsValidator as parameter to sendTransaction rpc which is used to validate the transaction
outputs before entering the tx-pool, an optional string parameter (enum: default | passthrough ),
null means using default validator, passthrough means skipping outputs validation

# [v0.27.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.26.1...v0.27.0) (2020-1-10)

### Refactor

* Reactor cell collector with
  iterator ([a677b92](https://github.com/nervosnetwork/ckb-sdk-java/commit/a677b92e3fe3f08bc0f126e09c98df38a2903567))
* Adapt examples for new cell collector with
  iterator ([41cc1a8](https://github.com/nervosnetwork/ckb-sdk-java/commit/41cc1a8afac0cd3b9d2e4cda46b17179b3b2aea8))

# [v0.26.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.26.0...v0.26.1) (2020-1-2)

### Features

* Add get_capacity_by_lock_hash
  rpc ([69aea7b](https://github.com/nervosnetwork/ckb-sdk-java/commit/69aea7b5477182faa1e269d383a0be6413f552f4))

### Docs

* Add example readme
  document ([2a5bfd3](https://github.com/nervosnetwork/ckb-sdk-java/commit/2a5bfd37fb484881b61c6cef65380b435d56ea0b))

### BugFix

* Update transaction
  builder ([20eac75](https://github.com/nervosnetwork/ckb-sdk-java/commit/20eac75e7be63fe83842bdb0d56c155ff55223e3))

# [v0.26.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.26.0-rc2...v0.26.0) (2019-12-14)

### Refactor

* Remove useless
  code ([557632a](https://github.com/nervosnetwork/ckb-sdk-java/commit/557632aef474d7ff310a83e292f117b0502dbdba))

# [v0.26.0-rc2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.25.0...v0.26.0-rc2) (2019-12-8)

### Feature

* Implement Nervos DAO
  deposit ([8cedeca](https://github.com/nervosnetwork/ckb-sdk-java/commit/8cedeca5ead7bd297e111cff2c962abc58cadffe))
* Implement Nervos DAO
  withdraw ([8f330ff](https://github.com/nervosnetwork/ckb-sdk-java/commit/8f330ffe97a127723ec8afaba5d9dc58fe15a63b))
* Refactor collectInputs of
  CellCollector ([1c74a03](https://github.com/nervosnetwork/ckb-sdk-java/commit/1c74a03c9a6565418b7468af8404afe862760653))
* Add fields of
  get_cells_by_lock_hash ([b44c40a](https://github.com/nervosnetwork/ckb-sdk-java/commit/b44c40a2a9c0c742b91a7b311ffb050e9edc77f8))
* Add fields of
  get_live_cells_by_lock_hash ([2229dbe](https://github.com/nervosnetwork/ckb-sdk-java/commit/2229dbe45b751d6fbe6ea3815adcfe2034fdccab))

### BugFix

* Update set_ban params and estimate fee test
  case ([b776c59](https://github.com/nervosnetwork/ckb-sdk-java/commit/b776c59908efe54b630d6e907d75585e8a095fc6))

# [v0.25.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.9...v0.25.0) (2019-11-16)

### BugFix

* Update ckb-sdk build.gradle for
  packing ([d0824ee](https://github.com/nervosnetwork/ckb-sdk-java/commit/d0824ee7d68fa76eb0db35172e9d052d9d205662))

# [v0.24.9](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.8...v0.24.9) (2019-11-13)

### Feature

* Impl indexer to collect cells and send
  transaction ([cdbe0a0](https://github.com/nervosnetwork/ckb-sdk-java/commit/cdbe0a0a793033b34ddfcffaa267250ff864636d))

# [v0.24.8](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.7...v0.24.8) (2019-11-13)

### BugFix

* Update occupiedCapacity method to convert ckb to
  shannon([bd00da8](https://github.com/nervosnetwork/ckb-sdk-java/commit/bd00da806d70d68e6cd1dd7775043887536bb3d8))

# [v0.24.7](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.6...v0.24.7) (2019-11-12)

### Feature

* Update cell output and script size
  calculating([b19d201](https://github.com/nervosnetwork/ckb-sdk-java/commit/b19d20145aeeb34ae940ac7cc919e56b6778ea21))
* Update multisig transaction estimating
  fee([4a084fa](https://github.com/nervosnetwork/ckb-sdk-java/commit/4a084fac174c834a18a9abf84d6bbdcb02b68685))

### BugFix

* Remove useless min
  capacity([2709d57](https://github.com/nervosnetwork/ckb-sdk-java/commit/2709d570c61db110b37103cca00b60b84caaaf10))

# [v0.24.6](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.5...v0.24.6) (2019-11-09)

### BugFix

* Update witnesses signature and update witness initial
  value([78b2600](https://github.com/nervosnetwork/ckb-sdk-java/commit/78b260038322b50dc48280619bb498696d20ec9d))
* Update transaction fee
  calculating([400a763](https://github.com/nervosnetwork/ckb-sdk-java/commit/400a763dbbde8dcb49ea4751f4a834c5bb757c6e))

# [v0.24.5](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.2...v0.24.5) (2019-11-08)

### BugFix

* Update address parse args length
  exception([dbf4fe5](https://github.com/nervosnetwork/ckb-sdk-java/commit/f21e62f69a92c9059ec990743cd5fcf509f4cf5b))

# [v0.24.2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.1...v0.24.2) (2019-11-07)

### Feature

* Add AddressGenerator and
  AddressParser([dbf4fe5](https://github.com/nervosnetwork/ckb-sdk-java/commit/dbf4fe57cde7965387e1fa2116b9d9a1eae9a30c))
* Update address generator and parser for
  example([7cdf948](https://github.com/nervosnetwork/ckb-sdk-java/commit/7cdf9488c7b3618f89c6c03492d96949fce85e8e))

# [v0.24.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.0...v0.24.1) (2019-11-06)

### Feature

* Refactor module names(rename core to ckb and add example
  module)([02ba44a](https://github.com/nervosnetwork/ckb-sdk-java/commit/02ba44aa7acf6f2eb77ed20ee933642c24f3c02d))
* Split transaction building and
  signature([cc4fc63](https://github.com/nervosnetwork/ckb-sdk-java/commit/cc4fc63ad25134bdff6d876f15486d04bdc3518c))
* Refactor multisig example to split transaction building and
  signature([48ac5c5](https://github.com/nervosnetwork/ckb-sdk-java/commit/c86f9ebd18dc240bbfd189fc5d540d50e1f90080))
* Update cell collector for transaction fee
  calculating([b4c720b](https://github.com/nervosnetwork/ckb-sdk-java/commit/b4c720b3923dca609a7d94aad8e18b7f224e849f))

### Breaking Change

* Rename core module to ckb
* Rename console module to ckb-sdk
* Create new example module
* Refactor examples for splitting transaction building and signature
* Reactor cell collector for transaction fee calculating

# [v0.24.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.23.2...v0.24.0) (2019-11-02)

### Feature

* Add estimate_fee_rate
  rpc([4e633ec](https://github.com/nervosnetwork/ckb-sdk-java/commit/4e633ec1c716690071a12796b72a1005839cff2b))
* Update transaction example with estimating tx
  fee([43fe747](https://github.com/nervosnetwork/ckb-sdk-java/commit/43fe747b58e0afc47a91f09d6aa44df0db5cf979))
* Update single sig transaction with new
  witness([48ac5c5](https://github.com/nervosnetwork/ckb-sdk-java/commit/48ac5c5c050ed64d63b85d8494cceb3909272605))
* Impl multi sig address and
  transaction([9696651](https://github.com/nervosnetwork/ckb-sdk-java/commit/96966511deeabf6b62a1a9389443b803754158d1))

### Breaking Change

Sending transaction with single sig address will be changed because of new witness data structure
and new signature logic.

# [v0.23.2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.23.1...v0.23.2) (2019-10-22)

### Bugfix

* Fix witnesses count
  error([2163a03](https://github.com/nervosnetwork/ckb-sdk-java/commit/2163a03fad89a9231841e6f862234fc04e5a0853))

# [v0.23.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.23.0...v0.23.1) (2019-10-22)

### Feature

* Add transaction fee to sendCapacity
  example([60f2faf](https://github.com/nervosnetwork/ckb-sdk-java/commit/60f2fafedb129d31100c26827379ef4a56dbe9c8))
* Move exceptions to outside
  application([52b85e5](https://github.com/nervosnetwork/ckb-sdk-java/commit/52b85e540fdecbf04225a6ba44b89e7d4df59c93))
* Add maven config to
  build.gradle([61f08ae](https://github.com/nervosnetwork/ckb-sdk-java/commit/61f08aedffab372b03fd93720f15a6ee9ec54a90))

# [v0.23.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.22.0...v0.23.0) (2019-10-19)

### Feature

* Impl transaction size and transaction fee
  calculating([c7285d3](https://github.com/nervosnetwork/ckb-sdk-java/commit/c7285d33078bcf0cca6666702e841c49b2b4b8ad))

# [v0.22.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.21.1...v0.22.0) (2019-10-05)

### Feature

* Refactor rpc service
  module([ef7a809](https://github.com/nervosnetwork/ckb-sdk-java/commit/ef7a809b9a87358d8c50ce0e4d971b16606c2fb1))
* Update script args and witnesses data
  type([af1f3ae](https://github.com/nervosnetwork/ckb-sdk-java/commit/af1f3aef9a5c98d86994817a8c16c97a29d13a26))
* Update epoch and header
  structure([2e8a6af](https://github.com/nervosnetwork/ckb-sdk-java/commit/2e8a6afa7ec64dda531abeef188308a972584a80))

### BREAKING CHANGES

* header and epoch structures are changed.
* script args and witnesses are changed.

# [v0.21.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.21.0...v0.21.1) (2019-09-25)

### Feature

* Add blockHash and constructor for
  CellOutputWithOutPoint([fe3a929](https://github.com/nervosnetwork/ckb-sdk-java/commit/fe3a929f6e0ebb93965098b5b67559226dfc0e86))
* Support multiply private keys to sign
  transaction([1ae3cc6](https://github.com/nervosnetwork/ckb-sdk-java/commit/1ae3cc6c9c6187215680128faaa63de8badf4210))
* Support multiply private keys to sign
  transaction([4efd32c](https://github.com/nervosnetwork/ckb-sdk-java/commit/4efd32ccd2487fd392087a3814d26e90b557485e))
* Update readme about capacity
  transfer([d09587a](https://github.com/nervosnetwork/ckb-sdk-java/commit/d09587a080f62b7e1e1618b779c0aa35818dc341))

# [v0.21.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.20.0...v0.21.0) (2019-09-21)

### Feature

* Add getBalance method and example
  usage([a915e66](https://github.com/nervosnetwork/ckb-sdk-java/commit/a915e661df2a00451e44535e9733170f049d5c45))
* Update header and nodeinfo data type to
  string([3f38f1](https://github.com/nervosnetwork/ckb-sdk-java/commit/3f38f1a791f57ced8ff8b1cf3a671e56e3b5dcd1))
* Update transaction parameter to hex
  string([1847c7d](https://github.com/nervosnetwork/ckb-sdk-java/commit/1847c7dc78a28394b2b70f7d981bf7a47b126c75))
* Update integer to hex string for
  jsonrpc([f93b5bd](https://github.com/nervosnetwork/ckb-sdk-java/commit/f93b5bd87579327cf409438449e730396021d167))
* Update transaction generator and serializer data type to
  integer([96709a5](https://github.com/nervosnetwork/ckb-sdk-java/commit/96709a55eeacf7003462910db9e838660d37e838))
* Remove hash for
  sendTransaction([9718b79](https://github.com/nervosnetwork/ckb-sdk-java/commit/9718b79b7fb664cca6f85eb8595a8edd716f15e8))
* Update get_live_cell request parameters and
  response([25227d9](https://github.com/nervosnetwork/ckb-sdk-java/commit/25227d979a101fa7c6e6192698026f46082a92be))
* Update get_live_cell request parameters and
  response([d88ded8](https://github.com/nervosnetwork/ckb-sdk-java/commit/d88ded8fb1193a6335c54750600a68523a69b5c4))

### Bugfix

* Fix gradle config of
  junit5([603dab7](https://github.com/nervosnetwork/ckb-sdk-java/commit/603dab7a9d41c9204c5d5a3a2f9bd4def01e78b2))
* Fix some test cases can't be
  executed([1c8c32c](https://github.com/nervosnetwork/ckb-sdk-java/commit/1c8c32c8cd0a6770a286eebf9520bdd9e9cd7801))

# [v0.20.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.19.1...v0.20.0) (2019-09-07)

**This version SDK supports computing script hash and transaction hash offline through script and
transaction serialization.**

### Feature

* Move wallet to core
  module([1bd1050](https://github.com/nervosnetwork/ckb-sdk-java/commit/1bd1050d9263f54250cbe282d1bd090ca2624e05))
* Implement serialization
  schema ([195a234](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/195a2345a1ee52af8cab68272d9e31134aefda57))
* Implement serialization
  schema ([2d55ea0](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/2d55ea0f086a65c329d5298f2c1240f261cda240))
* Implement serialization
  schema ([384d555](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/384d55528cb69e717baed2df90ac90da4b1c38d0))
* Implement serialization
  schema ([9433c56](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/9433c56904fbca083d91855512a01e5d7a46b286))
* Add serialization for script and
  transaction ([2aaa141](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/2aaa141185d640cc665cb85a9747412c7a054645))
* Refactor secp256k1 sign and key
  method ([f8d48c2](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/f8d48c23ce824886e153826c0227ca311d2777b5))

# [v0.19.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.19.0...v0.19.1) (2019-09-02)

### Bugfix

* Fix script hash
  bug([342365c](https://github.com/nervosnetwork/ckb-sdk-java/commit/342365c488bb4dfee751bbabaf4e1dc6dc3592c9))

# [v0.19.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.18.0...v0.19.0) (2019-08-28)

### Feature

* Update transaction related data
  type([e36173d](https://github.com/nervosnetwork/ckb-sdk-java/commit/e36173dc0936ee838fc0ff29740c394c477b897d))
* Update transaction related data
  type([e5bbf46](https://github.com/nervosnetwork/ckb-sdk-java/commit/e5bbf46ac7cc80fb276e86fd91cf3540cf934899))
* Update transaction related data
  type([1087071](https://github.com/nervosnetwork/ckb-sdk-java/commit/108707191a69f1a49a4b505db032c53d6071481a))
* Remove epoch_reward of
  Epoch([ce2cdbb](https://github.com/nervosnetwork/ckb-sdk-java/commit/ce2cdbba4e3341f555f278f5103d05d5471a7336))
* Update genesis block system cell and code
  hash([4a81810](https://github.com/nervosnetwork/ckb-sdk-java/commit/4a8181066f1a9b1662b5f98d53d5e1af09dd8a95))
* Update genesis block system cell and code
  hash([3cb10bb](https://github.com/nervosnetwork/ckb-sdk-java/commit/3cb10bb9956ecd6b347dd9dfcdab08349621e903))
* Add compute lock hash
  rpc([3cb10bb](https://github.com/nervosnetwork/ckb-sdk-java/commit/5548addd05cc5aaff7d5bfab9865050884ac862c))

### Bugfix

* Fix transaction witness
  bug([77b7f67](https://github.com/nervosnetwork/ckb-sdk-java/commit/77b7f674eb95cafb126ccfabf9f9f41fa70797e3))

### Test

* Refactor wallet and sendCapacity test
  cases([05428e0](https://github.com/nervosnetwork/ckb-sdk-java/commit/05428e0cb5b953f196fd8c244457ffc2f5cb58a4))

### Breaking Changes

* Remove header seal and add nonce to header
* Update script hash generator
* Update genesis block system cell and code hash
* Remove cell_output data and add outputs_data to transaction
* Remove epoch_reward of epoch
* Update cell dep(cell_dep and header_dep)

# [v0.18.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.17.0...v0.18.0) (2019-08-10)

* Update to support CKB v0.18.0.

# [v0.17.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.16.0...v0.17.0) (2019-07-27)

### Feature

* Add hash_type to
  Script ([6c84642](https://github.com/nervosnetwork/ckb-sdk-java/commit/6c84642a413e15cba82a62e67277cc507d4efca1))
* Update address format generator according recent
  RFC ([cd93318](https://github.com/nervosnetwork/ckb-sdk-java/commit/cd93318d470d8437201b004a3834720f95b74861))
* Add get_cellbase_output_capacity_details
  rpc ([4d15c22](https://github.com/nervosnetwork/ckb-sdk-java/commit/4d15c22508edb79c1b8e8117c52573376da0427d))
* Add get_header and get_header_by_number
  rpcs ([52663f0](https://github.com/nervosnetwork/ckb-sdk-java/commit/52663f0b87832550107ddcd880c25c8068ca1a03))
* Add set_ban and get_banned_address
  rpcs ([ea424e5](https://github.com/nervosnetwork/ckb-sdk-java/commit/ea424e5a9e4ae4d048c888e2f2009b6717abbe1b))

### Bugfix

* Fix bech32 covertBits with any length
  parameter ([217897f](https://github.com/nervosnetwork/ckb-sdk-java/commit/217897f3b5ab3b8bea775886832f09e5c9691823))

# [v0.16.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.15.0...v0.16.0) (2019-07-13)

* Update to support CKB v0.16.0.

# [v0.15.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.14.0...v0.15.0) (2019-06-29)

### Features

- Add index related
  rpc ([b77194c](https://github.com/nervosnetwork/ckb-sdk-java/commit/b77194c8fcbaf81dda44877d28307ed761335137))
- Update `get_blockchain_info`
  rpc ([80dbfe2](https://github.com/nervosnetwork/ckb-sdk-java/commit/80dbfe2c442c3718c080a4b6a4d576dbf8957d77))
- Use signRecoverable method to sign
  transaction ([e004f98](https://github.com/nervosnetwork/ckb-sdk-java/commit/e004f986cb6ec5f3ac141663e933b1ff1e632957))

# [v0.14.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.13.0...v0.14.0) (2019-06-15)

### Features

- Add sign method for
  transaction ([8b66b05](https://github.com/nervosnetwork/ckb-sdk-java/commit/8b66b05bcfce07eabb161dcafb45f2371678100f))
- Remove `args`
  from `CellInput` ([ce55fc3](https://github.com/nervosnetwork/ckb-sdk-java/pull/89/commits/ce55fc33aa912fe497678d1d05530bb99d9f759a))

# [v0.13.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.12.0...v0.13.0) (2019-06-01)

### Features

- Update tx_pool_info response data
  type ([beb4488](https://github.com/nervosnetwork/ckb-sdk-java/commit/beb4488562176bc4655e9575fcca47db8c4573cd))

### BugFix

- Add non-parameter constructor to
  witness ([6da731e](https://github.com/nervosnetwork/ckb-sdk-java/pull/87/commits/6da731eff63c860143585f7825f0d225f1806430))

# [v0.12.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.11.0...v0.12.0) (2019-05-18)

### Features

- Add example of sending
  capacity ([8f25ae7](https://github.com/nervosnetwork/ckb-sdk-java/pull/80/commits/8f25ae79aa281b0479ad7c081685326ab540a93d))

- Generalize OutPoint struct to reference
  headers ([3bc8146](https://github.com/nervosnetwork/ckb-sdk-java/pull/80/commits/3bc81469fa8f735fe7da086220ddeac6b9b50b12))

- Implement get_current_epoch RPC
  method ([a74ad7d](https://github.com/nervosnetwork/ckb-sdk-java/pull/80/commits/a74ad7def8f42dfbec30b89a87867034ad85eb09))

- Implement get_epoch_by_number RPC
  method ([88723c3](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/88723c346a6c78468bdb4c1ac8f02aa29d2158df))

- Implement get_peers RPC
  method ([2583cb8](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/2583cb867a1d1510d01e877a8b03ab0f803e45e4))

- Implement State RPC
  methods ([e461a3f](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/e461a3f23f4b00a958b00ca82b57ca85ebb69781))

- Secp256k1: Implement ECDSA
  sign ([d41b683](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/d41b683e6cea946d9d10cbab7bd481ab34a8b5c6))

- Remove always success
  script ([f39a106](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/f39a1065be6c7d5edf9a75f691588cc7073ae7ae))

- Implement tx_pool_info RPC
  method ([fee7130](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/fee7130ac2a840860a293f36586c71f23200c46f))

- Implement dry_run_transaction RPC
  method  ([b8b48f6](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/b8b48f62c45e12d64bae928ba67673fbcba6b795))

- Implement compute_transaction_hash RPC
  method  ([3edcb9c](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/3edcb9c1989e1f4f0c557df1c6faa18397d6164d))

- Rename proposalsRoot to
  proposalsHash ([4e55ef9](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/4e55ef9e69485f56ecdf089117d93414358eb916))

- Update the parameter of
  OutPoint ([7eeebad](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/7eeebad71b806841c76fc1950ce3c9808a6dc7d1))

- Remove trace RPC
  methods ([be29cac](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/be29cac5cb3d5ea8074b24ed9def34ee203f0b10))

- Add Prerequisites and Installation and Usage for
  README ([931f1af](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/931f1afdcb1d62f2bc4076af149c7a5f29a6d7ff))

### Bug Fixes

- Update transaction version data type to string

### BREAKING CHANGES

- Trace RPC methods are removed.

# [v0.11.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.10.0...v0.11.0) (2019-05-14)

### Features

- Implement `get_block_by_number` rpc and add more rpc test cases
- Update transaction data structure
- Update CellInput data structure

### Bug Fixes

- Update proposals to string list
- Update CellOutputWithOutPoint lock string to script

### Document

- Add development to README

# [v0.10.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.9.0...v0.10.0) (2019-05-06)

### Bug Fixes

- Change capacity in RPC interface data type to
  string ([88723c3](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/88723c346a6c78468bdb4c1ac8f02aa29d2158df))
- Add valid since to cell
  input ([b4758e1](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/b4758e1fe34706a366cf66f30054ee96d1421e1c))

# [v0.9.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.8.0...v0.9.0) (2019-4-22)

### Features

- Add segregated witness
  structure ([5611387](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/56113878e12da9dcebb361915956a3c383c9bb38))
- Add
  Bech32 ([52976a3](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/52976a3a22f818b80b12ab24785e7d1a7038e7aa))
- Implement address
  format ([eac763e](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/eac763ee70f9b0fac81633f5739fd50649e4b7a4))

### BREAKING CHANGES

- Update Cell and Script data
  type ([005b181](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/005b181e38299d60c7855466e97dc6a37d7b9a4b))
- Delete cellbase and cellbase_id
  fileds ([378b58e](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/378b58e73cc408bb67b93069d83ee4549f308f67))
- Delete signature and wallet
  logic ([fe2cc84](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/fe2cc84e0a789bf4d9724f54f64dfdd91e70726c))

# [v0.8.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.7.0...v0.8.0) (2019-4-8)

### Features

- Adapt CKB v0.8.0

# [v0.7.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.6.0...v0.7.0) (2019-03-25)

### Features

- rpc: implement trace rpc trace_transaction and
  get_transaction_trace ([dc56e9](https://github.com/nervosnetwork/ckb-sdk-java/commit/dc56e9812a865bcc233344e3497722a0c1686315))

### BREAKING CHANGES

- Repalce SHA3 with Blake2b.
- Remove bitcoin_unblock.rb.

# [v0.6.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.5.0...rc/v0.6.0) (2019-02-25)

### Refactor

* rename PRC request
  localNodeInfo ([9ae422](https://github.com/nervosnetwork/ckb-sdk-java/commit/9ae422))
* rename RPC response model CellInput and
  CellOutput ([080378](https://github.com/nervosnetwork/ckb-sdk-java/commit/080378))

### Features

* support asw account and
  transfer ([46b1ca](https://github.com/nervosnetwork/ckb-sdk-java/commit/46b1ca))

# [v0.5.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.4.0...rc/v0.5.0) (2019-02-11)

### Refactor

* update response model to ckb
  prefix ([316313e](https://github.com/nervosnetwork/ckb-sdk-java/commit/316313e))

### Features

* add script model and calculate type
  hash ([c3ddb81](https://github.com/nervosnetwork/ckb-sdk-java/commit/c3ddb81))
