package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.util.Set;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.common.ExtraFilter;
import org.nervos.mercury.model.common.PaginationRequest;
import org.nervos.mercury.model.common.Range;
import org.nervos.mercury.model.common.ViewType;
import org.nervos.mercury.model.req.item.Item;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryTransactionsPayload {

  public Item item;

  @SerializedName("asset_infos")
  public Set<AssetInfo> assetInfos;

  @SerializedName("extra")
  public ExtraFilter extraFilter;

  @SerializedName("block_range")
  public Range blockRange;

  public PaginationRequest pagination;

  @SerializedName("structure_type")
  public ViewType viewType;

  protected QueryTransactionsPayload() {}
}
