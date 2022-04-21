package org.nervos.ckb.type.base;

public class MoleculeUtils {
  public static void setBytes(byte[] from, byte[] to, int start) {
    System.arraycopy(from, 0, to, start, from.length);
  }

  public static void setInt(int size, byte[] to, int start) {
    byte[] from = new byte[4];
    from[3] = (byte) ((size >> 24) & 0xff);
    from[2] = (byte) ((size >> 16) & 0xff);
    from[1] = (byte) ((size >> 8) & 0xff);
    from[0] = (byte) (size & 0xff);
    setBytes(from, to, start);
  }

  public static int littleEndianBytes4ToInt(byte[] buf, int start) {
    return ((0xFF & buf[start + 3]) << 24)
        | ((0xFF & buf[start + 2]) << 16)
        | ((0xFF & buf[start + 1]) << 8)
        | (0xFF & buf[start]);
  }

  public static int[] getOffsets(byte[] buf) {
    if (buf.length < 4) {
      throw new MoleculeException("Byte length is less than 4.");
    }
    if (buf.length == 4) {
      return new int[]{4};
    }
    int headerEnd = littleEndianBytes4ToInt(buf, 4);
    if (headerEnd % 4 != 0) {
      throw new MoleculeException("Byte length of header in raw data should be multiples of 4.");
    }
    int count = headerEnd / 4 - 1;
    int[] offsets = new int[count + 1];
    for (int i = 0; i < count; i++) {
      offsets[i] = littleEndianBytes4ToInt(buf, 4 + i * 4);
    }
    offsets[count] = buf.length;
    return offsets;
  }
}
