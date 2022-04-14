package org.nervos.ckb.newtype.base;

public class MoleculeException extends IllegalArgumentException {
  public MoleculeException(String message) {
    super(message);
  }

  public MoleculeException(Throwable cause) {
    super(cause);
  }

  public static MoleculeException invalidByteSize(int expectedLength, int actualLength,
                                                  Class clazz) {
    return new MoleculeException(String.format(
        "Expect %d but receive %d bytes for molecule class %s",
        expectedLength, actualLength, clazz.getSimpleName()));
  }

  public static MoleculeException invalidItemCount(int expectedItemCount, int actualItemCount,
                                                   Class clazz) {
    return new MoleculeException(String.format(
        "Expect %d but receive %d items for molecule class %s",
        expectedItemCount, actualItemCount, clazz.getSimpleName()));
  }

  public static MoleculeException invalidFieldCount(int expectedFieldCount, int actualFieldCount,
                                                    Class clazz) {
    return new MoleculeException(String.format(
        "Expect %d but find %d fields in bytes header for molecule class %s",
        expectedFieldCount, actualFieldCount, clazz.getSimpleName()));
  }
}
