# This contract needs 1 signed arguments:
# 0. pubkey, used to identify token owner
# This contracts also accepts 2 required unsigned arguments and 1
# optional unsigned argument:
# 1. signature, signature used to present ownership
# 2. type, SIGHASH type
# 3. cellOutput(s), this is only used for SIGHASH_SINGLE and SIGHASH_MULTIPLE types,
# for SIGHASH_SINGLE, it stores an integer denoting the index of cellOutput to be
# signed; for SIGHASH_MULTIPLE, it stores a string of `,` separated array denoting
# outputs to sign
if ARGV.length != 3 && ARGV.length != 4
  raise "Wrong number of arguments!"
end

SIGHASH_ALL = 0x1
SIGHASH_NONE = 0x2
SIGHASH_SINGLE = 0x3
SIGHASH_MULTIPLE = 0x4
SIGHASH_ANYONECANPAY = 0x80

def hex_to_bin(s)
  if s.start_with?("0x")
    s = s[2..-1]
  end
  [s].pack("H*")
end


tx = CKB.load_tx
sha3 = Sha3.new

sha3.update(ARGV[2])
sighash_type = ARGV[2].to_i

if sighash_type & SIGHASH_ANYONECANPAY != 0
  # Only hash current cellInput
  out_point = CKB.load_input_out_point(0, CKB::Source::CURRENT)
  sha3.update(out_point["hash"])
  sha3.update(out_point["index"].to_s)
  sha3.update(CKB::CellField.new(CKB::Source::CURRENT, 0, CKB::CellField::LOCK_HASH).readall)
else
  # Hash all inputs
  tx["inputs"].each_with_index do |cellInput, i|
    sha3.update(cellInput["hash"])
    sha3.update(cellInput["index"].to_s)
    sha3.update(CKB.load_script_hash(i, CKB::Source::INPUT, CKB::Category::LOCK))
  end
end

case sighash_type & (~SIGHASH_ANYONECANPAY)
when SIGHASH_ALL
  tx["outputs"].each_with_index do |cellOutput, i|
    sha3.update(cellOutput["capacity"].to_s)
    sha3.update(cellOutput["lock"])
    if hash = CKB.load_script_hash(i, CKB::Source::OUTPUT, CKB::Category::TYPE)
      sha3.update(hash)
    end
  end
when SIGHASH_SINGLE
  raise "Not enough arguments" unless ARGV[3]
  output_index = ARGV[3].to_i
  cellOutput = tx["outputs"][output_index]
  sha3.update(cellOutput["capacity"].to_s)
  sha3.update(cellOutput["lock"])
  if hash = CKB.load_script_hash(output_index, CKB::Source::OUTPUT, CKB::Category::TYPE)
    sha3.update(hash)
  end
when SIGHASH_MULTIPLE
  raise "Not enough arguments" unless ARGV[3]
  ARGV[3].split(",").each do |output_index|
    output_index = output_index.to_i
    cellOutput = tx["outputs"][output_index]
    sha3.update(cellOutput["capacity"].to_s)
    sha3.update(cellOutput["lock"])
    if hash = CKB.load_script_hash(output_index, CKB::Source::OUTPUT, CKB::Category::TYPE)
      sha3.update(hash)
    end
  end
end
hash = sha3.final

pubkey = ARGV[0]
signature = ARGV[1]

unless Secp256k1.verify(hex_to_bin(pubkey), hex_to_bin(signature), hash)
  raise "Signature verification error!"
end
