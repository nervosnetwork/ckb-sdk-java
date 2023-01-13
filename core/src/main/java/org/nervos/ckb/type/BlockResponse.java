package org.nervos.ckb.type;

import java.util.List;

public class BlockResponse {
    /**
     * infomation of the block
     */
    public Block block;

    /**
     * The cycles of each transaction.
     * Note: cell base transaction has no cycle, so cycles' size is one less than block.transactions.
     */
    public List<Long> cycles;
}
