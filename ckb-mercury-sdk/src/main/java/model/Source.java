package model;

import com.google.gson.annotations.SerializedName;

public enum Source {
  @SerializedName("owned")
  unconstrained,
  @SerializedName("claimable")
  fleeting,
}
