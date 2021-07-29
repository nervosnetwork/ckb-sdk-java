package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.util.Set;

/** @author zjh @Created Date: 2021/7/22 @Description: @Modify by: */
public class FromNormalAddresses implements FromAddresses {

  @SerializedName("normal_addresses")
  public Set<String> normalAddresses;

  public FromNormalAddresses(Set<String> normalAddresses) {
    this.normalAddresses = normalAddresses;
  }
}
