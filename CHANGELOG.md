All notable changes to this project will be documented in this file.
See [Conventional Commits](https://conventionalcommits.org) for commit guidelines.

# [v0.18.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.17.0...v0.18.0) (2019-08-10)

* Update to support CKB v0.18.0.

# [v0.17.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.16.0...v0.17.0) (2019-07-27)

### Feature

* Add hash_type to Script ([6c84642](https://github.com/nervosnetwork/ckb-sdk-java/commit/6c84642a413e15cba82a62e67277cc507d4efca1))
* Update address format generator according recent RFC ([cd93318](https://github.com/nervosnetwork/ckb-sdk-java/commit/cd93318d470d8437201b004a3834720f95b74861))
* Add get_cellbase_output_capacity_details rpc ([4d15c22](https://github.com/nervosnetwork/ckb-sdk-java/commit/4d15c22508edb79c1b8e8117c52573376da0427d))
* Add get_header and get_header_by_number rpcs ([52663f0](https://github.com/nervosnetwork/ckb-sdk-java/commit/52663f0b87832550107ddcd880c25c8068ca1a03))
* Add set_ban and get_banned_address rpcs ([ea424e5](https://github.com/nervosnetwork/ckb-sdk-java/commit/ea424e5a9e4ae4d048c888e2f2009b6717abbe1b))

### Bugfix

* Fix bech32 covertBits with any length parameter ([217897f](https://github.com/nervosnetwork/ckb-sdk-java/commit/217897f3b5ab3b8bea775886832f09e5c9691823))

# [v0.16.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.15.0...v0.16.0) (2019-07-13)

* Update to support CKB v0.16.0.

# [v0.15.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.14.0...v0.15.0) (2019-06-29)

### Features

- Add index related rpc ([b77194c](https://github.com/nervosnetwork/ckb-sdk-java/commit/b77194c8fcbaf81dda44877d28307ed761335137))
- Update `get_blockchain_info` rpc ([80dbfe2](https://github.com/nervosnetwork/ckb-sdk-java/commit/80dbfe2c442c3718c080a4b6a4d576dbf8957d77))
- Use signRecoverable method to sign transaction ([e004f98](https://github.com/nervosnetwork/ckb-sdk-java/commit/e004f986cb6ec5f3ac141663e933b1ff1e632957))

# [v0.14.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.13.0...v0.14.0) (2019-06-15)

### Features

- Add sign method for transaction ([8b66b05](https://github.com/nervosnetwork/ckb-sdk-java/commit/8b66b05bcfce07eabb161dcafb45f2371678100f))
- Remove `args` from `CellInput` ([ce55fc3](https://github.com/nervosnetwork/ckb-sdk-java/pull/89/commits/ce55fc33aa912fe497678d1d05530bb99d9f759a))

# [v0.13.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.12.0...v0.13.0) (2019-06-01)

### Features

- Update tx_pool_info response data type ([beb4488](https://github.com/nervosnetwork/ckb-sdk-java/commit/beb4488562176bc4655e9575fcca47db8c4573cd))

### BugFix

- Add non-parameter constructor to witness ([6da731e](https://github.com/nervosnetwork/ckb-sdk-java/pull/87/commits/6da731eff63c860143585f7825f0d225f1806430))


# [v0.12.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.11.0...v0.12.0) (2019-05-18)

### Features

- Add example of sending capacity ([8f25ae7](https://github.com/nervosnetwork/ckb-sdk-java/pull/80/commits/8f25ae79aa281b0479ad7c081685326ab540a93d))

- Generalize OutPoint struct to reference headers ([3bc8146](https://github.com/nervosnetwork/ckb-sdk-java/pull/80/commits/3bc81469fa8f735fe7da086220ddeac6b9b50b12))

- Implement get_current_epoch RPC method ([a74ad7d](https://github.com/nervosnetwork/ckb-sdk-java/pull/80/commits/a74ad7def8f42dfbec30b89a87867034ad85eb09))

- Implement get_epoch_by_number RPC method ([88723c3](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/88723c346a6c78468bdb4c1ac8f02aa29d2158df))

- Implement get_peers RPC method ([2583cb8](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/2583cb867a1d1510d01e877a8b03ab0f803e45e4))

- Implement State RPC methods ([e461a3f](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/e461a3f23f4b00a958b00ca82b57ca85ebb69781))

- Secp256k1: Implement ECDSA sign ([d41b683](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/d41b683e6cea946d9d10cbab7bd481ab34a8b5c6))

- Remove always success script ([f39a106](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/f39a1065be6c7d5edf9a75f691588cc7073ae7ae))

- Implement tx_pool_info RPC method ([fee7130](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/fee7130ac2a840860a293f36586c71f23200c46f))

- Implement dry_run_transaction RPC method  ([b8b48f6](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/b8b48f62c45e12d64bae928ba67673fbcba6b795))

- Implement compute_transaction_hash RPC method  ([3edcb9c](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/3edcb9c1989e1f4f0c557df1c6faa18397d6164d))

- Rename proposalsRoot to proposalsHash ([4e55ef9](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/4e55ef9e69485f56ecdf089117d93414358eb916))

- Update the parameter of OutPoint ([7eeebad](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/7eeebad71b806841c76fc1950ce3c9808a6dc7d1))

- Remove trace RPC methods ([be29cac](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/be29cac5cb3d5ea8074b24ed9def34ee203f0b10))

- Add Prerequisites and Installation and Usage for README ([931f1af](https://github.com/nervosnetwork/ckb-sdk-java/pull/49/commits/931f1afdcb1d62f2bc4076af149c7a5f29a6d7ff))

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


