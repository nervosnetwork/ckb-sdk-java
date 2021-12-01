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
    if (Objects.equals(type, FilterType.Dao.toString())) {
      return RpcExtraFilter.toExtraFilter(json);
    }

    ExtraFilter extraFilter = new ExtraFilter();
    if (Objects.equals(type, FilterType.CellBase.toString())) {
      extraFilter.type = FilterType.CellBase;
    } else if (Objects.equals(type, FilterType.Freeze.toString())) {
      extraFilter.type = FilterType.Freeze;
    } else {
      throw new JsonParseException("Unknown extra filter type " + type);
    }
    return extraFilter;
  }

  @Override
  public JsonElement serialize(ExtraFilter src, Type typeOfSrc, JsonSerializationContext context) {

    if (Objects.equals(src.type, FilterType.CellBase)) {
      return new JsonPrimitive(FilterType.CellBase.toString());
    } else if (Objects.equals(src.type, FilterType.Freeze)) {
      return new JsonPrimitive(FilterType.Freeze.toString());
    } else {
      return context.serialize(RpcExtraFilter.build(src));
    }
  }

  enum FilterType {
    Dao,
    CellBase,
    Freeze,
  }

  static class RpcExtraFilter {
    public RpcDaoInfo Dao;

    public static RpcExtraFilter build(ExtraFilter src) {
      RpcDaoState state;
      if (Objects.equals(src.daoInfo.state, DaoState.Deposit)) {
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
        info.state = DaoState.Deposit;
        info.depositBlockNumber = state.get("value").getAsBigInteger();
      } else {
        info.state = DaoState.Withdraw;
        JsonArray withdraw = state.get("value").getAsJsonArray();
        info.depositBlockNumber = withdraw.get(0).getAsBigInteger();
        info.withdrawBlockNumber = withdraw.get(0).getAsBigInteger();
      }

      info.reward = dao.get("reward").getAsBigInteger();

      ExtraFilter e = new ExtraFilter();
      e.type = FilterType.Dao;
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
