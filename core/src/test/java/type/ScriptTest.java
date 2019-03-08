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

    @Test
    public void testAlwaysSuccessTypeHash() {
        String data = "0x7F454C46 02010100 00000000 00000000 0200F300 01000000 78000100 00000000 40000000 00000000 " +
                "98000000 00000000 05000000 40003800 01004000 03000200 01000000 05000000 00000000 00000000 00000100 " +
                "00000000 00000100 00000000 82000000 00000000 82000000 00000000 00100000 00000000 01459308 D0057300 " +
                "0000002E 73687374 72746162 002E7465 78740000 00000000 00000000 00000000 00000000 00000000 00000000 " +
                "00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 " +
                "0B000000 01000000 06000000 00000000 78000100 00000000 78000000 00000000 0A000000 00000000 00000000 " +
                "00000000 02000000 00000000 00000000 00000000 01000000 03000000 00000000 00000000 00000000 00000000 " +
                "82000000 00000000 11000000 00000000 00000000 00000000 01000000 00000000 00000000 00000000";
        data = data.replace(" ", "");
        Script script = new Script(0, data, null, Collections.emptyList(), Collections.emptyList());
        Assert.assertEquals("0x9f94d2511b787387638faa4a5bfd448baf21aa5fde3afaa54bb791188b5cf002", script.getTypeHash());
    }

    @Test
    public void testEmptyScriptTypeHash() {
        Script script = new Script(0, null, null, Collections.emptyList(), Collections.emptyList());
        Assert.assertEquals("0x4b29eb5168ba6f74bff824b15146246109c732626abd3c0578cbf147d8e28479", script.getTypeHash());
    }

    @Test
    public void testScriptTypeHash() {
        Script script = new Script(0, "0x01", "0x0000000000000000000000000000000000000000000000000000000000000000", Arrays.asList("0x01"),
                Arrays.asList("0x01"));
        Assert.assertEquals("0xafb140d0673571ed5710d220d6146d41bd8bc18a3a4ff723dad4331da5af5bb6", script.getTypeHash());
    }

}
