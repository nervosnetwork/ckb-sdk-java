package org.nervos.mercury.model.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExtraFilter implements JsonSerializer<ExtraFilter>, JsonDeserializer<ExtraFilter> {
  public FilterType type;
  public DaoInfo daoInfo;

  @Override
  public ExtraFilter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    String type = json.getAsJsonObject().get("type").getAsString();
    if (Objects.equals(type, FilterType.DAO.toString())) {
      return RpcExtraFilter.toExtraFilter(json);
    }

    ExtraFilter extraFilter = new ExtraFilter();
    if (Objects.equals(type, FilterType.CELL_BASE.toString())) {
      extraFilter.type = FilterType.CELL_BASE;
    } else if (Objects.equals(type, FilterType.FREEZE.toString())) {
      extraFilter.type = FilterType.FREEZE;
    } else {
      throw new JsonParseException("Unknown extra filter type " + type);
    }
    return extraFilter;
  }

  @Override
  public JsonElement serialize(ExtraFilter src, Type typeOfSrc, JsonSerializationContext context) {

    if (Objects.equals(src.type, FilterType.CELL_BASE)) {
      return new JsonPrimitive(FilterType.CELL_BASE.toString());
    } else if (Objects.equals(src.type, FilterType.FREEZE)) {
      return new JsonPrimitive(FilterType.FREEZE.toString());
    } else {
      return context.serialize(RpcExtraFilter.build(src));
    }
  }

  enum FilterType {
    @SerializedName("Dao")
    DAO,
    @SerializedName("CellBase")
    CELL_BASE,
    @SerializedName("Freeze")
    FREEZE,
  }

  static class RpcExtraFilter {
    public RpcDaoInfo Dao;

    public static RpcExtraFilter build(ExtraFilter src) {
      RpcDaoState state;
      if (Objects.equals(src.daoInfo.state, DaoState.DEPOSIT)) {
        state = new RpcDeposit(src.daoInfo.depositBlockNumber);
      } else {
        state =
            new RpcWithdraw(
                Arrays.asList(src.daoInfo.depositBlockNumber, src.daoInfo.withdrawBlockNumber));
      }

      RpcDaoInfo info = new RpcDaoInfo();
      info.reward = src.daoInfo.reward;
      info.state = state;

      RpcExtraFilter e = new RpcExtraFilter();
      e.Dao = info;

      return e;
    }

    public static ExtraFilter toExtraFilter(JsonElement json) {
      JsonObject filter = json.getAsJsonObject();

      JsonObject dao = filter.getAsJsonObject("value");
      JsonObject state = dao.getAsJsonObject("state");

      DaoInfo info = new DaoInfo();
      String type = state.get("type").getAsString();
      if (Objects.equals(type, "Deposit")) {
        info.state = DaoState.DEPOSIT;
        info.depositBlockNumber = state.get("value").getAsBigInteger();
      } else {
        info.state = DaoState.WITHDRAW;
        JsonArray withdraw = state.get("value").getAsJsonArray();
        info.depositBlockNumber = withdraw.get(0).getAsBigInteger();
        info.withdrawBlockNumber = withdraw.get(0).getAsBigInteger();
      }

      info.reward = dao.get("reward").getAsBigInteger();

      ExtraFilter e = new ExtraFilter();
      e.type = FilterType.DAO;
      e.daoInfo = info;

      return e;
    }
  }

  static class RpcDaoInfo {
    public RpcDaoState state;
    public BigInteger reward;
  }

  interface RpcDaoState {}

  static class RpcDeposit implements RpcDaoState {
    public BigInteger Deposit;

    public RpcDeposit() {}

    public RpcDeposit(BigInteger deposit) {
      Deposit = deposit;
    }
  }

  static class RpcWithdraw implements RpcDaoState {
    public List<BigInteger> Withdraw;

    public RpcWithdraw() {}

    public RpcWithdraw(List<BigInteger> withdraw) {
      Withdraw = withdraw;
    }
  }
}
