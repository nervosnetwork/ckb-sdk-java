package utils;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.service.GsonFactory;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;

public class MoleculeSerializationTest {
  @Test
  public void testTransaction() {
    Transaction transaction = readData("transaction.json", Transaction.class);

    assertByteArray(
        "0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d370000000001",
        transaction.cellDeps.get(0).pack().toByteArray());
    assertByteArray(
        "0x8f8c79eb6671709633fe6a46de93c0fedc9c1b8a6527a18d3983879542635c9f0200000000",
        transaction.cellDeps.get(1).pack().toByteArray());

    assertByteArray(
        "0x000000000000000079ba9678bfc25bf8c812cc96edd2c6937cebd518c2be0dd6a1c76978a2c18fee02000000",
        transaction.inputs.get(0).pack().toByteArray());

    assertByteArray(
        "0x96000000100000001800000061000000005847f80d000000490000001000000030000000310000009bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce80114000000da9a32bf89fd6b4d031c6d810c8deee0871491633500000010000000300000003100000082d76d1b75fe2fd9a27dfbaa65a039221a380d76c926f378d3f81cf3e7e13f2e0100000000",
        transaction.outputs.get(0).pack().toByteArray());
    assertByteArray(
        "0x6100000010000000180000006100000002e2fa8dd7000000490000001000000030000000310000009bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce80114000000b1354a801d92dd987486488b728154bcfbd809e1",
        transaction.outputs.get(1).pack().toByteArray());

    assertByteArray(
        "0xc10100001c000000200000006e00000072000000a2000000a50100000000000002000000f8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d3700000000018f8c79eb6671709633fe6a46de93c0fedc9c1b8a6527a18d3983879542635c9f02000000000000000001000000000000000000000079ba9678bfc25bf8c812cc96edd2c6937cebd518c2be0dd6a1c76978a2c18fee02000000030100000c000000a200000096000000100000001800000061000000005847f80d000000490000001000000030000000310000009bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce80114000000da9a32bf89fd6b4d031c6d810c8deee0871491633500000010000000300000003100000082d76d1b75fe2fd9a27dfbaa65a039221a380d76c926f378d3f81cf3e7e13f2e01000000006100000010000000180000006100000002e2fa8dd7000000490000001000000030000000310000009bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce80114000000b1354a801d92dd987486488b728154bcfbd809e11c0000000c0000001800000008000000000000000000000000000000",
        transaction.getRawTransaction().pack().toByteArray());

    assertByteArray(
        "0x2e0200000c000000cd010000c10100001c000000200000006e00000072000000a2000000a50100000000000002000000f8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d3700000000018f8c79eb6671709633fe6a46de93c0fedc9c1b8a6527a18d3983879542635c9f02000000000000000001000000000000000000000079ba9678bfc25bf8c812cc96edd2c6937cebd518c2be0dd6a1c76978a2c18fee02000000030100000c000000a200000096000000100000001800000061000000005847f80d000000490000001000000030000000310000009bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce80114000000da9a32bf89fd6b4d031c6d810c8deee0871491633500000010000000300000003100000082d76d1b75fe2fd9a27dfbaa65a039221a380d76c926f378d3f81cf3e7e13f2e01000000006100000010000000180000006100000002e2fa8dd7000000490000001000000030000000310000009bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce80114000000b1354a801d92dd987486488b728154bcfbd809e11c0000000c00000018000000080000000000000000000000000000006100000008000000550000005500000010000000550000005500000041000000cf9f8238a9def5d1bfd3172535a67cd567bb57af993431f0484393ed95d8a9581775e5f64cb0abb1a18b4c3b124b1ec898f18d189223ddd9aa95a9639293db3500",
        transaction.pack().toByteArray());
  }

  @Test
  public void testHeader() {
    Header header = readData("header.json", Header.class);
    assertByteArray(
        "0x000000005555011e4c2aeb3b7201000000040000000000000100001800f40100dc48626c5c978044c5055f316d395e74d0209b427091e4f5dd506ac849d23f2682988b971735834ea0c2766764647822ce0229cd16c0e7fb0b17248a445ec3b0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000007a063f88f10fa22ec7758d0b1f8723008670b170d23100000004a174a800ff06105d371a90473c1d07646cf03051a6b0",
        header.pack().toByteArray());
  }

  @Test
  public void testWitnessArgs() {
    byte[] bytes =
        Numeric.hexStringToByteArray(
            "0x55000000100000005500000055000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
    WitnessArgs witnessArgs = new WitnessArgs();
    witnessArgs.setLock(new byte[65]);
    Assertions.assertArrayEquals(bytes, witnessArgs.pack().toByteArray());
    Assertions.assertEquals(witnessArgs, WitnessArgs.unpack(bytes));
  }

  private void assertByteArray(String expected, byte[] actual) {
    String message =
        String.format("Expected: %s\nActual: %s\n", expected, Numeric.toHexString(actual));
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray(expected), actual, message);
  }

  private static <T> T readData(String fileName, Class<T> clazz) {
    String filePath = "src/test/resources/serialization/" + fileName;
    try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
      Gson gson = GsonFactory.create();
      return gson.fromJson(reader, clazz);
    } catch (IOException ex) {
      ex.printStackTrace();
      Assertions.fail(ex.getMessage());
    }
    return null;
  }
}
