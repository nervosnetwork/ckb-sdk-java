package org.nervos.ckb.newtype.base;

public class MoleculeException extends IllegalArgumentException {
    public MoleculeException(String message) {
        super(message);
    }

    public MoleculeException(int expectedLength, int actualLength, Class clazz) {
        super(String.format(
                "Expect %d-byte but receive %d-byte raw data for molecule class %s.",
                expectedLength, actualLength, clazz.getSimpleName()));
    }

    public MoleculeException(Throwable cause) {
        super(cause);
    }
}
