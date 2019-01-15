package org.nervos.ckb.exceptions;

import java.io.IOException;

/**
 * Created by duanyytop on 2018-12-26.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class APIErrorException extends IOException {

    public APIErrorException(String message) {
        super(message);
    }

}
