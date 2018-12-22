package org.nervos.ckb;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.nervos.ckb.response.item.Seal;

import java.util.Arrays;

public class SealTest {

    @Test
    public void testSeal() {
        String json = "{\"nonce\": 10545468520399447721, \"proof\": [240, 2400]}";
        Seal seal = new Gson().fromJson(json, Seal.class);

        Assert.assertEquals("10545468520399447721", seal.nonce);
        Assert.assertEquals(seal.getProof(), Arrays.asList(240, 96));
    }

}
