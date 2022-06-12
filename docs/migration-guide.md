We roll out a brand new ckb-java-sdk after a great refactor work. The new sdk brings plenty of BREAKING CHANGES incompatible with deprecated sdk `v1.0.*` and earlier releases.

The breaking changes include

- Type or name change of quite a few fields in RPC type representation.
- Unified address representation and operation.
- Transaction signing mechanism by `ScriptGroup`, `ScriptSigner`, and `TransactionSigner`.
- `TransactionBuilder` and `ScriptHandler` for transaction construction by manual.
- Operation support for most common scripts.
- Clean some utils classes and unused classes.

Other underlying breaking changes that a possibly transparent to you

- Molecule serialization in a code-generated way.
- Unify type adapter for Gson serialization.
- More robust test.

In the following part, we list the aspects you need to care about most if you are ready to migrate from deprecated sdk to the new one.

## Address

The deprecated sdk uses several classes to represent, generate or process ckb address, including `AddressUtils`, `AddressGenerator`, `AddressParser`, `AddressTools`, and so on. The new sdk unifies all of them and represents ckb address by a single class `Address`. Here is an example to manipulate or convert an address.

```java
// decode address string
Address address = Address.decode("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtyqlpwlx7ed68pftzv69wcvr5nxxqzzus2zxwa6");
// get script and network of the address
Script script = address.getScript();
Network network = address.getNetwork();
// encode to address string representation
String shortEncoded = address.encodeShort();
String fullBech32Encoded = address.encodeFullBech32();
String fullBech32mEncoded = address.encodeFullBech32();
```

Note: ckb has 3 kinds of address encoding formats, the short, the full_bech32, and the full_bech32m. The recommended way is full_bech32m, while the other two are under deprecation. For more please refer to [RFC-0021](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md).

## data representation
### hash

The 32-byte hash is widely used in ckb worlds The deprecated sdk uses `String` to represents the hash, while the new sdk represents it by `byte[]`.

### Number

To align with [CKB RPC Types](https://github.com/nervosnetwork/ckb/tree/develop/rpc#rpc-types), the new sdk represents `Uint32` by `int`, `Uint64` by `long` and `Uint128` by `BigInteger` in RPC class. The deprecated sdk does not obey this rule strictly.

Because the number is signed in java but is unsigned in CKB RPC, you should operate the corresponding numeric variable in an unsigned way in sdk.

Let's take [Capacity](https://github.com/nervosnetwork/ckb/tree/develop/rpc#type-capacity), one of the most frequently used fields, for example. The deprecated sdk represents capacity by `BigInteger`, while the new sdk represent it by `long`. The `long` here should be regarded as a **unsigned** `long` in numerical processing. That is to say, for safety you had better use the following methods for the specific purpose of capacity operation

- `Long#compare`
- `Long#divideUnsigned`
- `Long#remainderUnsigned`

Except for these, you can use `+` `-` and `*` operators for capacity as usual.

## Package/class migration

Some important packages move

- `org.nervos.ckb.type.cell` -> `org.nervos.ckb.type`
- `org.nervos.ckb.type` -> `org.nervos.ckb.type`
- `org.nervos.ckb.type.transaction` -> `org.nervos.ckb.type`
- `org.nervos.ckb.address.Network` -> `org.nervos.ckb.Network`

## Sign transaction

The new sdk introduces `ScriptGroup` to replace `SignatureAction` and `MercuryScriptGroup` for signing transactions. You can get the `ScriptGroup` with a raw transaction from mercury, or construct it by yourself. And after that `TransactionSigner` will sign in the correct place as long as you provide the right secret information (e.g. private key). Please check the example in [README](../README.md#Sign and send a transaction).