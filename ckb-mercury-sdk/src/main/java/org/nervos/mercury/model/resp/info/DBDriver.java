package org.nervos.mercury.model.resp.info;

import com.google.gson.annotations.SerializedName;

public enum DBDriver {
  @SerializedName("PostgreSQL")
  POSTGRES_SQL,
  @SerializedName("MySQL")
  MY_SQL,
  @SerializedName("SQLite")
  SQLITE,
}
