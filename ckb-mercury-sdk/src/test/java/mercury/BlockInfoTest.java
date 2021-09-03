package mercury;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import mercury.constant.MercuryApiFactory;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.GetBlockInfoPayloadBuilder;
import org.nervos.mercury.model.resp.BlockInfoResponse;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class BlockInfoTest {

  @Test
  void testGetBlockInfoWithBlockNumber() {
    try {

      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockNumber(new BigInteger("2172093"));

      System.out.println(new Gson().toJson(builder.build()));

      BlockInfoResponse blockInfo = MercuryApiFactory.getApi().getBlockInfo(builder.build());

      System.out.println(new Gson().toJson(blockInfo));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGetBlockInfoWithBlockHash() {
    try {

      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockHash("0xee8adba356105149cb9dc1cb0d09430a6bd01182868787ace587961c0d64e742");

      System.out.println(new Gson().toJson(builder.build()));

      BlockInfoResponse blockInfo = MercuryApiFactory.getApi().getBlockInfo(builder.build());

      System.out.println(new Gson().toJson(blockInfo));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGetBlockInfoWithBlockHashAndBlockNumber() {
    try {

      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockNumber(new BigInteger("2172093"));
      builder.blockHash("0xee8adba356105149cb9dc1cb0d09430a6bd01182868787ace587961c0d64e742");

      System.out.println(new Gson().toJson(builder.build()));

      BlockInfoResponse blockInfo = MercuryApiFactory.getApi().getBlockInfo(builder.build());

      System.out.println(new Gson().toJson(blockInfo));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testTipBlockInfo() {
    try {

      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();

      // Request parameter is empty
      System.out.println(new Gson().toJson(builder.build()));

      BlockInfoResponse blockInfo = MercuryApiFactory.getApi().getBlockInfo(builder.build());

      System.out.println(new Gson().toJson(blockInfo));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testBlockHashAndBlockNumberDoNotMatch() {
    try {

      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockNumber(new BigInteger("2172092"));
      builder.blockHash("0xee8adba356105149cb9dc1cb0d09430a6bd01182868787ace587961c0d64e742");

      System.out.println(new Gson().toJson(builder.build()));

      MercuryApiFactory.getApi().getBlockInfo(builder.build());

    } catch (IOException e) {
      //      assertEquals("block number and hash mismatch", e.getMessage());
    }
  }

  @Test
  void testCannotFind() {
    try {

      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockHash("0xee8adba356105149cb9dc1cb0d09430a6bd01182868787ace587961c0d64e741");

      System.out.println(new Gson().toJson(builder.build()));

      MercuryApiFactory.getApi().getBlockInfo(builder.build());
      // TODO: 2021/7/22 error handle
    } catch (IOException e) {
      //      Assert.assertTrue(
      //          e.getMessage()
      //              .startsWith(
      //                  "RpcService method get_generic_block error
      // {\"code\":-32602,\"message\":\"Cannot get block by hash H256"));
    }
  }

  @Test
  void testWrongHeight() {
    try {

      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockNumber(new BigInteger("217209233"));

      System.out.println(new Gson().toJson(builder.build()));

      // error: invalid block number
      MercuryApiFactory.getApi().getBlockInfo(builder.build());

    } catch (IOException e) {
      //      assertEquals(
      //          "RpcService method get_generic_block error {\"code\":-32602,\"message\":\"invalid
      // block number\"}",
      //          e.getMessage());
    }
  }
}
