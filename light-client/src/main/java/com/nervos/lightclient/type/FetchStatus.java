package com.nervos.lightclient.type;

import com.google.gson.annotations.SerializedName;

public enum FetchStatus {
  @SerializedName("fetched")
  FETCHED,
  @SerializedName("fetching")
  FETCHING,
  @SerializedName("added")
  ADDED,
  @SerializedName("not_found")
  NOT_FOUND
}
