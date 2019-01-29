package org.nervos.ckb.wallet;

import org.nervos.ckb.methods.type.Input;
import org.nervos.ckb.methods.type.Output;
import org.nervos.ckb.utils.Numeric;

import java.util.List;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Utils {

    public void signSighashAllInputs(List<Input> inputs, List<Output> outputs, String privateKey) {
        StringBuilder inputsHash = new StringBuilder("0x1");
        for (Input input: inputs) {
            inputsHash.append(Numeric.cleanHexPrefix(input.previousOutput.hash));
            inputsHash.append(input.previousOutput.index);
            inputsHash.append(input.unlock);
        }

    }

}
