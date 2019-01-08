package org.nervos.ckb.methods.type;

/**
 * Created by duanyytop on 2019-01-08.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class OutPoint {

    /**
     * hash : 0xbddb7c2559c2c3cdfc8f3cae2697ca75489521c352265cc9e60b4b2416ad5929
     * index : 0
     */

    public String hash;
    public int index;

    public OutPoint() {}

    public OutPoint(String hash, int index) {
        this.hash = hash;
        this.index = index;
    }

}
