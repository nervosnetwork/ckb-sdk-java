package mercury;

import constant.ApiFactory;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.Numeric;
import org.nervos.mercury.model.GetBlockInfoPayloadBuilder;

import java.io.IOException;

public class BlockInfoTest {

  @Test
  void testGetBlockInfoWithBlockNumber() throws IOException {
    GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
    builder.blockNumber(2172093L);
    ApiFactory.getApi().getBlockInfo(builder.build());
  }

  @Test
  void testGetBlockInfoWithBlockHash() throws IOException {
    GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
    builder.blockHash(Numeric.hexStringToByteArray("0xee8adba356105149cb9dc1cb0d09430a6bd01182868787ace587961c0d64e742"));

    ApiFactory.getApi().getBlockInfo(builder.build());
  }
}
