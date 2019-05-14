All notable changes to this project will be documented in this file.
See [Conventional Commits](https://conventionalcommits.org) for commit guidelines.

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

- Change capacity in RPC interface data type to string ([88723c3](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/88723c346a6c78468bdb4c1ac8f02aa29d2158df))
- Add valid since to cell input ([b4758e1](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/b4758e1fe34706a366cf66f30054ee96d1421e1c))

# [v0.9.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.8.0...v0.9.0) (2019-4-22)

### Features

- Add segregated witness structure ([5611387](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/56113878e12da9dcebb361915956a3c383c9bb38))
- Add Bech32 ([52976a3](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/52976a3a22f818b80b12ab24785e7d1a7038e7aa))
- Implement address format ([eac763e](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/eac763ee70f9b0fac81633f5739fd50649e4b7a4))

### BREAKING CHANGES

- Update Cell and Script data type ([005b181](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/005b181e38299d60c7855466e97dc6a37d7b9a4b))
- Delete cellbase and cellbase_id fileds ([378b58e](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/378b58e73cc408bb67b93069d83ee4549f308f67))
- Delete signature and wallet logic ([fe2cc84](https://github.com/nervosnetwork/ckb-sdk-java/pull/39/commits/fe2cc84e0a789bf4d9724f54f64dfdd91e70726c))


# [v0.8.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.7.0...v0.8.0) (2019-4-8)

### Features

- Adapt CKB v0.8.0


# [v0.7.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.6.0...v0.7.0) (2019-03-25)

### Features

- rpc: implement trace rpc trace_transaction and get_transaction_trace ([dc56e9](https://github.com/nervosnetwork/ckb-sdk-java/commit/dc56e9812a865bcc233344e3497722a0c1686315))

### BREAKING CHANGES

- Repalce SHA3 with Blake2b.
- Remove bitcoin_unblock.rb.

# [v0.6.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.5.0...rc/v0.6.0) (2019-02-25)

### Refactor

* rename PRC request localNodeInfo ([9ae422](https://github.com/nervosnetwork/ckb-sdk-java/commit/9ae422))
* rename RPC response model CellInput and CellOutput ([080378](https://github.com/nervosnetwork/ckb-sdk-java/commit/080378))


### Features

* support asw account and transfer ([46b1ca](https://github.com/nervosnetwork/ckb-sdk-java/commit/46b1ca))

# [v0.5.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.4.0...rc/v0.5.0) (2019-02-11)

### Refactor

* update response model to ckb prefix ([316313e](https://github.com/nervosnetwork/ckb-sdk-java/commit/316313e))


### Features

* add script model and calculate type hash ([c3ddb81](https://github.com/nervosnetwork/ckb-sdk-java/commit/c3ddb81))


