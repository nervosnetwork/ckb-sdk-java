package org.nervos.ckb.utils;

import org.junit.Test;

import static org.nervos.ckb.utils.Assertions.verifyPrecondition;

public class AssertionsTest {

    @Test
    public void testVerifyPrecondition() {
        verifyPrecondition(true, "");
    }

    @Test(expected = RuntimeException.class)
    public void testVerifyPreconditionFailure() {
        verifyPrecondition(false, "");
    }
}
