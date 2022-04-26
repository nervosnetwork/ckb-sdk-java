package org.nervos.mercury.model.resp.info;

import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.Script;

import java.util.List;

public class Extension {
  public String name;
  public List<Script> scripts;
  public List<CellDep> cellDeps;
}
