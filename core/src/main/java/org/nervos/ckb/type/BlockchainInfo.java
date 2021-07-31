package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class BlockchainInfo {

  @SerializedName("is_initial_block_download")
  public boolean isInitialBlockDownload;

  public String epoch;
  public String difficulty;

  @SerializedName("median_time")
  public String medianTime;

  public String chain;

  public List<AlertMessage> alerts;
}
