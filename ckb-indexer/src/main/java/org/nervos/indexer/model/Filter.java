package org.nervos.indexer.model;

import org.nervos.ckb.type.Script;

import java.util.List;

public class Filter {
  public Script script;
  public List<Integer> outputDataLenRange;
  public List<Long> outputCapacityRange;
  public List<Integer> blockRange;
}
