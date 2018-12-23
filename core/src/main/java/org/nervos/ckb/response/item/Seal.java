package org.nervos.ckb.response.item;

import java.util.List;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Seal {

    /**
     * nonce : 10545468520399447721
     * proof : [163,13,0,0,12,17,0,0,98,28,0,0,240,60,0,0,200,62,0,0,12,76,0,0,6,93,0,0,247,93,0,0,107,97,0,0,230,100,0,0,16,103,0,0,244,107,0,0]
     */

    public String nonce;

    // data type: uint8
    private List<Integer> proof;

    public List<Integer> getProof() {
        int size = proof.size();
        for (int i = 0; i < size; i++) {
            proof.set(i, proof.get(i) & 0xff);
        }
        return proof;
    }

    public void setProof(List<Integer> proof) {
        this.proof = proof;
    }
}
