package org.nervos.mercury.model.req.item;

import com.google.gson.annotations.SerializedName;

public class Address implements Item {
  @SerializedName("Address")
  public String address;

  public Address(String address) {
    this.address = address;
  }
}
