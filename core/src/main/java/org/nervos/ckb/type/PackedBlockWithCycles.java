package org.nervos.ckb.type;

import org.nervos.ckb.utils.Numeric;

import java.util.List;

public class PackedBlockWithCycles {
    /**
     * infomation of the block
     */
    public String block;

    /**
     * The cycles of each transaction.
     * Note: cell base transaction has no cycle, so cycles' size is one less than block.transactions.
     */
    public List<Long> cycles;

    /**
     * @return parsed bytes from block string
     */
    public byte[] getBlockBytes() {
        return this.block == null ? null : Numeric.hexStringToByteArray(this.block);
    }
}
