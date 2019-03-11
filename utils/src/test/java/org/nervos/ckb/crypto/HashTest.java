package org.nervos.ckb.crypto;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.nervos.ckb.utils.Numeric.asByte;

public class HashTest {

    @Test
    public void testBlake2b() {
        assertThat(Hash.blake2b(""),
                is("0x44f4c69744d5f8c55d642062949dcae49bc4e7ef43d388c5a12f42b5633d163e"));

        assertThat(Hash.blake2bString("The quick brown fox jumps over the lazy dog"),
                is("0xabfa2c08d62f6f567d088d6ba41d3bbbb9a45c241a8e3789ef39700060b5cee2"));
    }

    @Test
    public void testByte() {
        assertThat(asByte(0x0, 0x0), is((byte) 0x0));
        assertThat(asByte(0x1, 0x0), is((byte) 0x10));
        assertThat(asByte(0xf, 0xf), is((byte) 0xff));
        assertThat(asByte(0xc, 0x5), is((byte) 0xc5));
    }
}
