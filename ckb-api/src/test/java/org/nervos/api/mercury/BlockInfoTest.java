package org.nervos.api.mercury;

import constant.ApiFactory;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.Numeric;
import org.nervos.mercury.model.GetBlockInfoPayloadBuilder;
import org.nervos.mercury.model.resp.BlockInfoResponse;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class BlockInfoTest {

  @Test
  void testGetBlockInfoWithBlockNumber() {
    try {
      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockNumber(2172093);

      BlockInfoResponse blockInfo = ApiFactory.getApi().getBlockInfo(builder.build());
      System.out.println();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGetBlockInfoWithBlockHash() {
    try {
      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockHash(
          Numeric.hexStringToByteArray(
              "0xee8adba356105149cb9dc1cb0d09430a6bd01182868787ace587961c0d64e742"));

      BlockInfoResponse blockInfo = ApiFactory.getApi().getBlockInfo(builder.build());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGetBlockInfoWithBlockHashAndBlockNumber() {
    try {
      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockNumber(2172093);
      builder.blockHash(
          Numeric.hexStringToByteArray(
              "0xee8adba356105149cb9dc1cb0d09430a6bd01182868787ace587961c0d64e742"));

      BlockInfoResponse blockInfo = ApiFactory.getApi().getBlockInfo(builder.build());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testTipBlockInfo() {
    try {
      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();

      // Request parameter is empty
      BlockInfoResponse blockInfo = ApiFactory.getApi().getBlockInfo(builder.build());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testBlockHashAndBlockNumberDoNotMatch() {
    try {
      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockNumber(2172092);
      builder.blockHash(
          Numeric.hexStringToByteArray(
              "0xee8adba356105149cb9dc1cb0d09430a6bd01182868787ace587961c0d64e742"));

      ApiFactory.getApi().getBlockInfo(builder.build());

    } catch (IOException e) {
      //      assertEquals("block number and hash mismatch", e.getMessage());
    }
  }

  @Test
  void testCannotFind() {
    try {
      GetBlockInfoPayloadBuilder builder = new GetBlockInfoPayloadBuilder();
      builder.blockHash(
          Numeric.hexStringToByteArray(
              "0xee8adba356105149cb9dc1cb0d09430a6bd01182868787ace587961c0d64e741"));

      ApiFactory.getApi().getBlockInfo(builder.build());
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
      builder.blockNumber(217209233);

      // error: invalid block number
      ApiFactory.getApi().getBlockInfo(builder.build());

    } catch (IOException e) {
      //      assertEquals(
      //          "RpcService method get_generic_block error {\"code\":-32602,\"message\":\"invalid
      // block number\"}",
      //          e.getMessage());
    }
  }
}
