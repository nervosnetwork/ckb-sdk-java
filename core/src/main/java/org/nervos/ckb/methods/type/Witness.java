package org.nervos.ckb.methods.type;

import java.util.List;

public class Witness {
    public int index;
    public List<String> data;

    public Witness(int index, List<String> data) {
        this.index = index;
        this.data = data;
    }
}
