package org.nervos.ckb.transaction.scriptHandler;

import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.Script;

import java.util.List;

public interface ScriptHandler {
  // Return true if this WitnessHandler can set witness placeholder for given script
  // It should return false if script is null.
  boolean isMatched(Script script);

  // return cellDeps list for the processed script
  List<CellDep> getCellDeps();

  // get witness placeholder on top of original witness
  byte[] getWitnessPlaceholder(byte[] originalWitness);
}
