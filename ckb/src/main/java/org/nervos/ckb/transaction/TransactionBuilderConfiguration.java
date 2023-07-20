package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.transaction.handler.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionBuilderConfiguration {
  private Network network;
  private List<ScriptHandler> scriptHandlers = new ArrayList<>();
  private long feeRate = 1000;

  public TransactionBuilderConfiguration() {
  }

  public TransactionBuilderConfiguration(Network network) {
    Objects.requireNonNull(network);
    this.network = network;
    registerScriptHandler(Secp256k1Blake160SighashAllScriptHandler.class);
    registerScriptHandler(Secp256k1Blake160MultisigAllScriptHandler.class);
    registerScriptHandler(SudtScriptHandler.class);
    registerScriptHandler(DaoScriptHandler.class);
    registerScriptHandler(OmnilockScriptHandler.class);
    registerScriptHandler(TypeIdHandler.class);
  }

  public Network getNetwork() {
    return network;
  }

  public void setNetwork(Network network) {
    this.network = network;
  }

  public List<ScriptHandler> getScriptHandlers() {
    return scriptHandlers;
  }

  public void setScriptHandlers(List<ScriptHandler> scriptHandlers) {
    this.scriptHandlers = scriptHandlers;
  }

  public void registerScriptHandler(ScriptHandler scriptHandler) {
    this.scriptHandlers.add(scriptHandler);
  }

  public void registerScriptHandler(Class<? extends ScriptHandler> clazz) {
    try {
      ScriptHandler instance = clazz.newInstance();
      instance.init(network);
      registerScriptHandler(instance);
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public long getFeeRate() {
    return feeRate;
  }

  public void setFeeRate(long feeRate) {
    this.feeRate = feeRate;
  }
}
