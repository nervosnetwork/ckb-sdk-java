### Features

* The field hash_type has a new allowed value `data1` but it is only valid after hard fork
  activation
* Add support for Bech32m

### Breaking Changes

* The field `uncles_hash` in header will be renamed to `extra_hash` for all JSON RPC methods
