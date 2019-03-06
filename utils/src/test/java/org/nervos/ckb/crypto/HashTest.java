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
                is("0x0e5751c026e543b2e8ab2eb06099daa1d1e5df47778f7787faab45cdf12fe3a8"));

        assertThat(Hash.blake2bString("The quick brown fox jumps over the lazy dog"),
                is("0x01718cec35cd3d796dd00020e0bfecb473ad23457d063b75eff29c0ffa2e58a9"));

        Hash.update("".getBytes(StandardCharsets.UTF_8));
        assertThat(Hash.doFinalString(),
                is("0x0e5751c026e543b2e8ab2eb06099daa1d1e5df47778f7787faab45cdf12fe3a8"));

        Hash.update("The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.UTF_8));
        assertThat(Hash.doFinalString(),
                is("0x01718cec35cd3d796dd00020e0bfecb473ad23457d063b75eff29c0ffa2e58a9"));
    }

    @Test
    public void testByte() {
        assertThat(asByte(0x0, 0x0), is((byte) 0x0));
        assertThat(asByte(0x1, 0x0), is((byte) 0x10));
        assertThat(asByte(0xf, 0xf), is((byte) 0xff));
        assertThat(asByte(0xc, 0x5), is((byte) 0xc5));
    }
}
