package org.nervos.indexer.model.resp;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.List;

public class TxWithCells {
  public byte[] txHash;
  public int blockNumber;
  public int txIndex;
  public List<Cell> cells;

  @JsonAdapter(Deserializer.class)
  public static class Cell {
    public IoType ioType;
    public int ioIndex;
  }

  public static class Deserializer implements JsonDeserializer<Cell> {

    @Override
    public Cell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonArray jsonArray = json.getAsJsonArray();
      Cell cell = new Cell();
      cell.ioType = context.deserialize(jsonArray.get(0), IoType.class);
      cell.ioIndex = context.deserialize(jsonArray.get(1), Integer.class);
      return cell;
    }
  }
}
