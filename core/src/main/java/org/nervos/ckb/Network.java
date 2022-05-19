package org.nervos.ckb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.nervos.ckb.service.GsonFactory;
import org.nervos.ckb.sign.SystemContract;
import org.nervos.ckb.type.Script;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Network {
  MAINNET,
  TESTNET;

  private Map<SystemContract.Type, SystemContract> contractTypeSystemContractMap = new EnumMap<>(SystemContract.Type.class);
  private Map<Script, SystemContract.Type> scriptSystemContractTypeMap = new HashMap<>();

  static {
    try {
      MAINNET.loadSystemContracts("mainnet.json");
      TESTNET.loadSystemContracts("testnet.json");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void loadSystemContracts(String path) throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(path);
    Reader reader = new java.io.InputStreamReader(inputStream);

    Gson gson = GsonFactory.create();
    Type type = new TypeToken<Map<SystemContract.Type, SystemContract>>() {}.getType();

    Map<SystemContract.Type, SystemContract> contracts = gson.fromJson(reader, type);
    for (Map.Entry<SystemContract.Type, SystemContract> entry : contracts.entrySet()) {
      Objects.requireNonNull(entry.getKey());
      register(entry.getKey(), entry.getValue());
    }
  }

  private void register(SystemContract.Type contractType, SystemContract contract) {
    Objects.requireNonNull(contractType);
    Objects.requireNonNull(contract);
    contractTypeSystemContractMap.put(contractType, contract);
    Script script = contract.createScript(new byte[0]);
    scriptSystemContractTypeMap.put(script, contractType);
  }

  public SystemContract getSystemContract(SystemContract.Type contractType) {
    return contractTypeSystemContractMap.get(contractType);
  }

  public SystemContract getSystemContract(Script script) {
    return getSystemContract(getSystemContractType(script));
  }

  public SystemContract.Type getSystemContractType(Script script) {
    if (script == null) {
      return null;
    }
    Script key = new Script(script.codeHash, new byte[0], script.hashType);
    return scriptSystemContractTypeMap.get(key);
  }
}
