package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;
import java.util.Set;

/** @author zjh @Created Date: 2021/7/22 @Description: @Modify by: */
public class FromKeyAddresses implements FromAddresses {

  @SerializedName("key_addresses")
  public KeyAddresses keyAddresses;

  public FromKeyAddresses(Set<String> keyAddresses, Source source) {
    this.keyAddresses = new KeyAddresses(keyAddresses, source);
  }

  private static class KeyAddresses {
    @SerializedName("key_addresses")
    public Set<String> keyAddresses;

    public Source source;

    public KeyAddresses(Set<String> keyAddresses, Source source) {
      this.keyAddresses = keyAddresses;
      this.source = source;
    }
  }
}
