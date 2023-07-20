package org.nervos.ckb.transaction.handler;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;

public interface ScriptHandler {
  boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context);

  void init(Network network);

  default boolean postBuild(int index, AbstractTransactionBuilder txBuilder, Object context) {return false;}
}
