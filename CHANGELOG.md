All notable changes to this project will be documented in this file.
See [Conventional Commits](https://conventionalcommits.org) for commit guidelines.

# [v0.26.0-rc2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.25.0...v0.26.0-rc2) (2019-12-8)

### Feature

* Implement Nervos DAO deposit ([8cedeca](https://github.com/nervosnetwork/ckb-sdk-java/commit/8cedeca5ead7bd297e111cff2c962abc58cadffe))
* Implement Nervos DAO withdraw ([8f330ff](https://github.com/nervosnetwork/ckb-sdk-java/commit/8f330ffe97a127723ec8afaba5d9dc58fe15a63b))
* Refactor collectInputs of CellCollector ([1c74a03](https://github.com/nervosnetwork/ckb-sdk-java/commit/1c74a03c9a6565418b7468af8404afe862760653))
* Add fields of get_cells_by_lock_hash ([b44c40a](https://github.com/nervosnetwork/ckb-sdk-java/commit/b44c40a2a9c0c742b91a7b311ffb050e9edc77f8))
* Add fields of get_live_cells_by_lock_hash ([2229dbe](https://github.com/nervosnetwork/ckb-sdk-java/commit/2229dbe45b751d6fbe6ea3815adcfe2034fdccab))

### BugFix

* Update set_ban params and estimate fee test case ([b776c59](https://github.com/nervosnetwork/ckb-sdk-java/commit/b776c59908efe54b630d6e907d75585e8a095fc6))


# [v0.25.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.9...v0.25.0) (2019-11-16)

### BugFix

* Update ckb-sdk build.gradle for packing ([d0824ee](https://github.com/nervosnetwork/ckb-sdk-java/commit/d0824ee7d68fa76eb0db35172e9d052d9d205662))

# [v0.24.9](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.8...v0.24.9) (2019-11-13)

### Feature

* Impl indexer to collect cells and send transaction ([cdbe0a0](https://github.com/nervosnetwork/ckb-sdk-java/commit/cdbe0a0a793033b34ddfcffaa267250ff864636d))

# [v0.24.8](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.7...v0.24.8) (2019-11-13)

### BugFix

* Update occupiedCapacity method to convert ckb to shannon([bd00da8](https://github.com/nervosnetwork/ckb-sdk-java/commit/bd00da806d70d68e6cd1dd7775043887536bb3d8))

# [v0.24.7](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.6...v0.24.7) (2019-11-12)

### Feature

* Update cell output and script size calculating([b19d201](https://github.com/nervosnetwork/ckb-sdk-java/commit/b19d20145aeeb34ae940ac7cc919e56b6778ea21))
* Update multisig transaction estimating fee([4a084fa](https://github.com/nervosnetwork/ckb-sdk-java/commit/4a084fac174c834a18a9abf84d6bbdcb02b68685))

### BugFix

* Remove useless min capacity([2709d57](https://github.com/nervosnetwork/ckb-sdk-java/commit/2709d570c61db110b37103cca00b60b84caaaf10))

# [v0.24.6](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.5...v0.24.6) (2019-11-09)

### BugFix

* Update witnesses signature and update witness initial value([78b2600](https://github.com/nervosnetwork/ckb-sdk-java/commit/78b260038322b50dc48280619bb498696d20ec9d))
* Update transaction fee calculating([400a763](https://github.com/nervosnetwork/ckb-sdk-java/commit/400a763dbbde8dcb49ea4751f4a834c5bb757c6e))

# [v0.24.5](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.2...v0.24.5) (2019-11-08)

### BugFix

* Update address parse args length exception([dbf4fe5](https://github.com/nervosnetwork/ckb-sdk-java/commit/f21e62f69a92c9059ec990743cd5fcf509f4cf5b))

# [v0.24.2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.1...v0.24.2) (2019-11-07)

### Feature

* Add AddressGenerator and AddressParser([dbf4fe5](https://github.com/nervosnetwork/ckb-sdk-java/commit/dbf4fe57cde7965387e1fa2116b9d9a1eae9a30c))
* Update address generator and parser for example([7cdf948](https://github.com/nervosnetwork/ckb-sdk-java/commit/7cdf9488c7b3618f89c6c03492d96949fce85e8e))

# [v0.24.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.24.0...v0.24.1) (2019-11-06)

### Feature

* Refactor module names(rename core to ckb and add example module)([02ba44a](https://github.com/nervosnetwork/ckb-sdk-java/commit/02ba44aa7acf6f2eb77ed20ee933642c24f3c02d))
* Split transaction building and signature([cc4fc63](https://github.com/nervosnetwork/ckb-sdk-java/commit/cc4fc63ad25134bdff6d876f15486d04bdc3518c))
* Refactor multisig example to split transaction building and signature([48ac5c5](https://github.com/nervosnetwork/ckb-sdk-java/commit/c86f9ebd18dc240bbfd189fc5d540d50e1f90080))
* Update cell collector for transaction fee calculating([b4c720b](https://github.com/nervosnetwork/ckb-sdk-java/commit/b4c720b3923dca609a7d94aad8e18b7f224e849f))

### Breaking Change

* Rename core module to ckb
* Rename console module to ckb-sdk
* Create new example module
* Refactor examples for splitting transaction building and signature 
* Reactor cell collector for transaction fee calculating

# [v0.24.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.23.2...v0.24.0) (2019-11-02)

### Feature

* Add estimate_fee_rate rpc([4e633ec](https://github.com/nervosnetwork/ckb-sdk-java/commit/4e633ec1c716690071a12796b72a1005839cff2b))
* Update transaction example with estimating tx fee([43fe747](https://github.com/nervosnetwork/ckb-sdk-java/commit/43fe747b58e0afc47a91f09d6aa44df0db5cf979))
* Update single sig transaction with new witness([48ac5c5](https://github.com/nervosnetwork/ckb-sdk-java/commit/48ac5c5c050ed64d63b85d8494cceb3909272605))
* Impl multi sig address and transaction([9696651](https://github.com/nervosnetwork/ckb-sdk-java/commit/96966511deeabf6b62a1a9389443b803754158d1))

### Breaking Change

Sending transaction with single sig address will be changed because of new witness data structure and new signature logic.

# [v0.23.2](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.23.1...v0.23.2) (2019-10-22)

### Bugfix

* Fix witnesses count error([2163a03](https://github.com/nervosnetwork/ckb-sdk-java/commit/2163a03fad89a9231841e6f862234fc04e5a0853))

# [v0.23.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.23.0...v0.23.1) (2019-10-22)

### Feature

* Add transaction fee to sendCapacity example([60f2faf](https://github.com/nervosnetwork/ckb-sdk-java/commit/60f2fafedb129d31100c26827379ef4a56dbe9c8))
* Move exceptions to outside application([52b85e5](https://github.com/nervosnetwork/ckb-sdk-java/commit/52b85e540fdecbf04225a6ba44b89e7d4df59c93))
* Add maven config to build.gradle([61f08ae](https://github.com/nervosnetwork/ckb-sdk-java/commit/61f08aedffab372b03fd93720f15a6ee9ec54a90))

# [v0.23.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.22.0...v0.23.0) (2019-10-19)

### Feature

* Impl transaction size and transaction fee calculating([c7285d3](https://github.com/nervosnetwork/ckb-sdk-java/commit/c7285d33078bcf0cca6666702e841c49b2b4b8ad))

# [v0.22.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.21.1...v0.22.0) (2019-10-05)

### Feature

* Refactor rpc service module([ef7a809](https://github.com/nervosnetwork/ckb-sdk-java/commit/ef7a809b9a87358d8c50ce0e4d971b16606c2fb1))
* Update script args and witnesses data type([af1f3ae](https://github.com/nervosnetwork/ckb-sdk-java/commit/af1f3aef9a5c98d86994817a8c16c97a29d13a26))
* Update epoch and header structure([2e8a6af](https://github.com/nervosnetwork/ckb-sdk-java/commit/2e8a6afa7ec64dda531abeef188308a972584a80))

### BREAKING CHANGES

* header and epoch structures are changed.
* script args and witnesses are changed.

# [v0.21.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.21.0...v0.21.1) (2019-09-25)

### Feature

* Add blockHash and constructor for CellOutputWithOutPoint([fe3a929](https://github.com/nervosnetwork/ckb-sdk-java/commit/fe3a929f6e0ebb93965098b5b67559226dfc0e86))
* Support multiply private keys to sign transaction([1ae3cc6](https://github.com/nervosnetwork/ckb-sdk-java/commit/1ae3cc6c9c6187215680128faaa63de8badf4210))
* Support multiply private keys to sign transaction([4efd32c](https://github.com/nervosnetwork/ckb-sdk-java/commit/4efd32ccd2487fd392087a3814d26e90b557485e))
* Update readme about capacity transfer([d09587a](https://github.com/nervosnetwork/ckb-sdk-java/commit/d09587a080f62b7e1e1618b779c0aa35818dc341))


# [v0.21.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.20.0...v0.21.0) (2019-09-21)

### Feature

* Add getBalance method and example usage([a915e66](https://github.com/nervosnetwork/ckb-sdk-java/commit/a915e661df2a00451e44535e9733170f049d5c45))
* Update header and nodeinfo data type to string([3f38f1](https://github.com/nervosnetwork/ckb-sdk-java/commit/3f38f1a791f57ced8ff8b1cf3a671e56e3b5dcd1))
* Update transaction parameter to hex string([1847c7d](https://github.com/nervosnetwork/ckb-sdk-java/commit/1847c7dc78a28394b2b70f7d981bf7a47b126c75))
* Update integer to hex string for jsonrpc([f93b5bd](https://github.com/nervosnetwork/ckb-sdk-java/commit/f93b5bd87579327cf409438449e730396021d167))
* Update transaction generator and serializer data type to integer([96709a5](https://github.com/nervosnetwork/ckb-sdk-java/commit/96709a55eeacf7003462910db9e838660d37e838))
* Remove hash for sendTransaction([9718b79](https://github.com/nervosnetwork/ckb-sdk-java/commit/9718b79b7fb664cca6f85eb8595a8edd716f15e8))
* Update get_live_cell request parameters and response([25227d9](https://github.com/nervosnetwork/ckb-sdk-java/commit/25227d979a101fa7c6e6192698026f46082a92be))
* Update get_live_cell request parameters and response([d88ded8](https://github.com/nervosnetwork/ckb-sdk-java/commit/d88ded8fb1193a6335c54750600a68523a69b5c4))

### Bugfix

* Fix gradle config of junit5([603dab7](https://github.com/nervosnetwork/ckb-sdk-java/commit/603dab7a9d41c9204c5d5a3a2f9bd4def01e78b2))
* Fix some test cases can't be executed([1c8c32c](https://github.com/nervosnetwork/ckb-sdk-java/commit/1c8c32c8cd0a6770a286eebf9520bdd9e9cd7801))

# [v0.20.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.19.1...v0.20.0) (2019-09-07)

**This version SDK supports computing script hash and transaction hash offline through script and transaction serialization.**

### Feature

* Move wallet to core module([1bd1050](https://github.com/nervosnetwork/ckb-sdk-java/commit/1bd1050d9263f54250cbe282d1bd090ca2624e05))
* Implement serialization schema ([195a234](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/195a2345a1ee52af8cab68272d9e31134aefda57))
* Implement serialization schema ([2d55ea0](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/2d55ea0f086a65c329d5298f2c1240f261cda240))
* Implement serialization schema ([384d555](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/384d55528cb69e717baed2df90ac90da4b1c38d0))
* Implement serialization schema ([9433c56](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/9433c56904fbca083d91855512a01e5d7a46b286))
* Add serialization for script and transaction ([2aaa141](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/2aaa141185d640cc665cb85a9747412c7a054645))
* Refactor secp256k1 sign and key method ([f8d48c2](https://github.com/nervosnetwork/ckb-sdk-java/pull/147/commits/f8d48c23ce824886e153826c0227ca311d2777b5))

# [v0.19.1](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.19.0...v0.19.1) (2019-09-02)

### Bugfix

* Fix script hash bug([342365c](https://github.com/nervosnetwork/ckb-sdk-java/commit/342365c488bb4dfee751bbabaf4e1dc6dc3592c9))

# [v0.19.0](https://github.com/nervosnetwork/ckb-sdk-java/compare/v0.18.0...v0.19.0) (2019-08-28)

### Feature

* Update transaction related data type([e36173d](https://github.com/nervosnetwork/ckb-sdk-java/commit/e36173dc0936ee838fc0ff29740c394c477b897d))
* Update transaction related data type([e5bbf46](https://github.com/nervosnetwork/ckb-sdk-java/commit/e5bbf46ac7cc80fb276e86fd91cf3540cf934899))
* Update transaction related data type([1087071](https://github.com/nervosnetwork/ckb-sdk-java/commit/108707191a69f1a49a4b505db032c53d6071481a))
* Remove epoch_reward of Epoch([ce2cdbb](https://github.com/nervosnetwork/ckb-sdk-java/commit/ce2cdbba4e3341f555f278f5103d05d5471a7336))
* Update genesis block system cell and code hash([4a81810](https://github.com/nervosnetwork/ckb-sdk-java/commit/4a8181066f1a9b1662b5f98d53d5e1af09dd8a95))
* Update genesis block system cell and code hash([3cb10bb](https://github.com/nervosnetwork/ckb-sdk-java/commit/3cb10bb9956ecd6b347dd9dfcdab08349621e903))
* Add compute lock hash rpc([3cb10bb](https://github.com/nervosnetwork/ckb-sdk-java/commit/5548addd05cc5aaff7d5bfab9865050884ac862c))

### Bugfix

* Fix transaction witness bug([77b7f67](https://github.com/nervosnetwork/ckb-sdk-java/commit/77b7f674eb95cafb126ccfabf9f9f41fa70797e3))

### Test

* Refactor wallet and sendCapacity test cases([05428e0](https://github.com/nervosnetwork/ckb-sdk-java/commit/05428e0cb5b953f196fd8c244457ffc2f5cb58a4))

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

