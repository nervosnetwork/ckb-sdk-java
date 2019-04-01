package type;

import org.junit.Assert;
import org.junit.Test;
import org.nervos.ckb.methods.type.Script;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by duanyytop on 2019-03-08.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ScriptTest {

    private static final String ZERO_HASH = "0000000000000000000000000000000000000000000000000000000000000000";

    @Test
    public void testEmptyScriptTypeHash() {
        Script script = new Script(0, ZERO_HASH, Collections.emptyList());
        Assert.assertEquals("0x266cec97cbede2cfbce73666f08deed9560bdf7841a7a5a51b3a3f09da249e21", script.getTypeHash());
    }

    @Test
    public void testScriptTypeHash() {
        Script script = new Script(0, ZERO_HASH, Collections.singletonList("0x01"));
        Assert.assertEquals("0xdade0e507e27e2a5995cf39c8cf454b6e70fa80d03c1187db7a4cb2c9eab79da", script.getTypeHash());
    }

}
