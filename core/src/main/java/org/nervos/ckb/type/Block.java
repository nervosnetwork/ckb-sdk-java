package org.nervos.ckb.type;

import java.util.List;

import org.nervos.ckb.utils.MoleculeConverter;
import org.nervos.ckb.utils.Numeric;

public class Block {
  public Header header;

  public List<Transaction> transactions;

  public List<byte[]> proposals;

  public List<Uncle> uncles;

  public String extension;

  public static class Uncle {

    public Header header;

    public List<byte[]> proposals;
  }

  public byte[] getExtensionBytes() {
    if (this.extension == null) {
      return null;
    }
    return Numeric.hexStringToByteArray(this.extension);
  }

  public org.nervos.ckb.type.concrete.Block pack() {
    if (this.extension != null) {
      return org.nervos.ckb.type.concrete.BlockV1.builder().setHeader(header.pack())
          .setTransactions(MoleculeConverter.packTransactionVec(transactions))
          .setProposals(MoleculeConverter.packProposalShortIdVec(proposals))
          .setUncles(MoleculeConverter.packUncleBlockVec(uncles))
          .setExtension(MoleculeConverter.packBytes(this.getExtensionBytes()))
          .build()
          .asV0();
    } else {
      return org.nervos.ckb.type.concrete.Block.builder()
          .setHeader(header.pack())
          .setTransactions(MoleculeConverter.packTransactionVec(transactions))
          .setProposals(MoleculeConverter.packProposalShortIdVec(proposals))
          .setUncles(MoleculeConverter.packUncleBlockVec(uncles))
          .build();
    }
  }
}
