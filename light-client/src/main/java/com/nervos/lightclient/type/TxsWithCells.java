package com.nervos.lightclient.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.nervos.ckb.type.Transaction;
import org.nervos.indexer.model.resp.IoType;

import java.lang.reflect.Type;
import java.util.List;

public class TxsWithCells {
  public byte[] lastCursor;
  public List<TxWithCells> objects;

  public class TxWithCells {
    public Transaction transaction;
    public int blockNumber;
    public int txIndex;
    public List<Cell> cells;
  }

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
