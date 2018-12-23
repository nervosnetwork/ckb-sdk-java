package org.nervos.ckb.crypto;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.nervos.ckb.utils.Numeric.asByte;

public class HashTest {

    @Test
    public void testSha3() {
        byte[] input = new byte[] {
                asByte(0x6, 0x8),
                asByte(0x6, 0x5),
                asByte(0x6, 0xc),
                asByte(0x6, 0xc),
                asByte(0x6, 0xf),
                asByte(0x2, 0x0),
                asByte(0x7, 0x7),
                asByte(0x6, 0xf),
                asByte(0x7, 0x2),
                asByte(0x6, 0xc),
                asByte(0x6, 0x4)
        };

        byte[] expected = new byte[]{
                asByte(0x6, 0x4),
                asByte(0x4, 0xb),
                asByte(0xc, 0xc),
                asByte(0x7, 0xe),
                asByte(0x5, 0x6),
                asByte(0x4, 0x3),
                asByte(0x7, 0x3),
                asByte(0x0, 0x4),
                asByte(0x0, 0x9),
                asByte(0x9, 0x9),
                asByte(0xa, 0xa),
                asByte(0xc, 0x8),
                asByte(0x9, 0xe),
                asByte(0x7, 0x6),
                asByte(0x2, 0x2),
                asByte(0xf, 0x3),
                asByte(0xc, 0xa),
                asByte(0x7, 0x1),
                asByte(0xf, 0xb),
                asByte(0xa, 0x1),
                asByte(0xd, 0x9),
                asByte(0x7, 0x2),
                asByte(0xf, 0xd),
                asByte(0x9, 0x4),
                asByte(0xa, 0x3),
                asByte(0x1, 0xc),
                asByte(0x3, 0xb),
                asByte(0xf, 0xb),
                asByte(0xf, 0x2),
                asByte(0x4, 0xe),
                asByte(0x3, 0x9),
                asByte(0x3, 0x8)
        };

        byte[] result = Hash.sha3(input);
        assertThat(result, is(expected));
    }


    @Test
    public void testSha3HashHex() {
        assertThat(Hash.sha3(""),
                is("0xa7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a"));

        assertThat(Hash.sha3("68656c6c6f20776f726c64"),
                is("0x644bcc7e564373040999aac89e7622f3ca71fba1d972fd94a31c3bfbf24e3938"));
    }

    @Test
    public void testSha3String() {
        assertThat(Hash.sha3String(""),
                is("0xa7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a"));

        assertThat(Hash.sha3String("hello ckb"),
                is("0xf3ac0212947c7de8cea1a48c8b5534e771d1644ab77b8dede1d8e7af0f83791c"));
    }

    @Test
    public void testByte() {
        assertThat(asByte(0x0, 0x0), is((byte) 0x0));
        assertThat(asByte(0x1, 0x0), is((byte) 0x10));
        assertThat(asByte(0xf, 0xf), is((byte) 0xff));
        assertThat(asByte(0xc, 0x5), is((byte) 0xc5));
    }
}
