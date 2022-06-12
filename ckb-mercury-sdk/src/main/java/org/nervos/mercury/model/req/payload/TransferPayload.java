package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.since.SinceConfig;

import java.util.List;

public class TransferPayload {
  public AssetInfo assetInfo;
  public List<Item> from;
  public List<ToInfo> to;
  public CapacityProvider outputCapacityProvider;
  public PayFee payFee;
  public Long feeRate = 1000L;
  public SinceConfig since;

  public enum PayFee {
    @SerializedName("From")
    FROM,
    @SerializedName("To")
    TO
  }

}
