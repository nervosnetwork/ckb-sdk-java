package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class BlockchainInfo {

  @JsonProperty("is_initial_block_download")
  public boolean isInitialBlockDownload;

  public String epoch;
  public String difficulty;

  @JsonProperty("median_time")
  public String medianTime;

  public String chain;

  public List<AlertMessage> alerts;
}
