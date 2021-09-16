package org.nervos.mercury.model.resp.info;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellDep;

public class Extension {
  public String name;

  public List<Script> scripts;

  @SerializedName("cell_deps")
  public List<CellDep> cellDeps;
}
