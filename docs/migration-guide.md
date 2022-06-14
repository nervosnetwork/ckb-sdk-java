We roll out a brand new ckb-java-sdk after refactor work. The new sdk brings plenty of BREAKING CHANGES incompatible with the deprecated sdk `v1.0.*` and earlier releases.

The breaking changes include

- Type or name change of quite a few fields in RPC type representation.
- Unified address representation and operation.
- Transaction signing mechanism by `ScriptGroup`, `ScriptSigner`, and `TransactionSigner`.
- `TransactionBuilder` and `ScriptHandler` for transaction construction by manual.
- Operation support for most common scripts.
- Clean some utils classes and unused classes.

Other underlying breaking changes that are possibly transparent to you

- Molecule serialization in a code-generated way.
- Unify type adapter for Gson serialization.
- More robust test.

In the following part, we list the aspects you need to care about most if you are ready to migrate from deprecated sdk to the new one.

## Address

The deprecated sdk uses several classes to represent, generate or process ckb address, including `AddressUtils`, `AddressGenerator`, `AddressParser`, `AddressTools`, and so on. The new sdk unifies all of them and represents ckb address by a single class `Address`.

Parse / decode address

```java
String encoded = "ckt1qzda..."
// Before:
AddressParseResult result = AddressParser.parse(encoded);
Script script = result.script;
Network network = result.network;

// Now:
Address address = Address.decode(encoded);
Script script = address.getScript();
Network network = address.getNetwork();
```

Encode address from script and network

```java
// Before:
String encoded = AddressGenerator.generate(network, script);

// Now:
Address address = new Address(script, network);
String encoded = address.encode();
```

## data representation

### hash

The 32-byte hash is widely used in ckb world. The deprecated sdk uses `String` to represent the hash, while the new sdk represents it by `byte[]`.

```java
// Before:
String hash = tx.computeHash();

// Now:
byte[] hash = tx.computeHash(); 
```

Type should be changed like this in all places where the hash is used.

### Number

To align with [CKB RPC Types](https://github.com/nervosnetwork/ckb/tree/develop/rpc#rpc-types), the new sdk represents `Uint32` by `int`, `Uint64` by `long` and `Uint128` by `BigInteger` for corresponding data. The deprecated sdk does not obey this rule strictly.

Because the number is signed in java but is unsigned in CKB RPC, you should operate these numeric variables in an unsigned way in sdk.

Let's take [Capacity](https://github.com/nervosnetwork/ckb/tree/develop/rpc#type-capacity), one of the most frequently used fields, for example. The deprecated sdk represents capacity by `BigInteger`, while the new sdk represents it by `long`. The `long` here should be regarded as a **unsigned** `long` in numerical processing. That is to say, for safety you had better use the following methods for the specific purpose of capacity operation

- `Long#compare`
- `Long#divideUnsigned`
- `Long#remainderUnsigned`

Except for these operations, you can use `+` `-` and `*` operators for capacity as usual.

```java
// Before: capacity is a BigInteger
if (capacity.compareTo(BigInteger.valueOf(140L)) >= 0) {
    // do something
}

// Now: capacity is a long
if (Long.compareUnsigned(capacity, 140L) >= 0) {
    // do something  
}
```

## Package/class migration

Some important packages /class moves

- `org.nervos.ckb.type.cell` -> `org.nervos.ckb.type`
- `org.nervos.ckb.type.param` -> `org.nervos.ckb.type`
- `org.nervos.ckb.type.transaction` -> `org.nervos.ckb.type`
- `org.nervos.ckb.address.Network` -> `org.nervos.ckb.Network`

```java
// Before:
import org.nervos.ckb.type.transaction;
// Now:
import org.nervos.ckb.type;

// Transaction class is in `org.nervos.ckb.type.transaction` before but now in `org.nervos.ckb.type`.
Transaction tx = api.getTransaction(txHash).transaction;
```

You can find and replace all `org.nervos.ckb.type.transaction` with `org.nervos.ckb.type`. Same for other moved packages / classes.

## Sign transaction

The new sdk introduces `ScriptGroup` to replace `SignatureAction` and `MercuryScriptGroup` for signing transactions. You can get the `ScriptGroup` with a raw transaction from mercury, or construct it by yourself. And after that `TransactionSigner` will sign in the correct place as long as you provide the right secret information (e.g. private key).

```java
// Before:
static Map<String, String> addressWithKey = new HashMap<>();
// omit: put private key to `addressWithKey`
TransactionCompletionResponse txResponse = api.buildSimpleTransferTransaction(payload);
List<MercuryScriptGroup> scriptGroups = txResponse.getScriptGroup();
Secp256k1SighashBuilder signBuilder = new Secp256k1SighashBuilder(txResponse.txView);
for (MercuryScriptGroup sg : scriptGroups) {
    signBuilder.sign(sg, addressWithKey.get(sg.getAddress()));
}
Transaction tx = signBuilder.buildTx();
String hash = api.sendTransaction(tx);

// Now:
TransactionWithScriptGroups txWithScriptGroups = api.buildSimpleTransferTransaction(payload);
TransactionSigner.getInstance(Network.TESTNET)
                 .signTransaction(txWithGroup, "0x6c9ed038....", "0x369dfe...");  // sign with private key
byte[] txHash = api.sendTransaction(txWithScriptGroups.txView);
```