package org.nervos.ckb.transaction.handler;

import org.nervos.ckb.Network;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;
import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

import java.util.Arrays;
import java.util.List;

import static org.nervos.ckb.utils.MoleculeConverter.packUint64;

public class TypeIdHandler implements ScriptHandler {
  public static final byte[] TYPE_ID_CODE_HASH = Numeric.hexStringToByteArray("0x00000000000000000000000000000000000000000000000000545950455f4944");
  public static final byte[] ZERO_ARGS = new byte[32];

  private boolean isMatched(Script script) {
    if (script == null) {
      return false;
    }
    return Arrays.equals(script.codeHash, TYPE_ID_CODE_HASH);
  }

  @Override
  public boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context) {
    if (scriptGroup == null || !isMatched(scriptGroup.getScript()) || scriptGroup.getOutputIndices().isEmpty()) {
      return false;
    }
    List<Integer> outputIndices = scriptGroup.getOutputIndices();
    int index = outputIndices.get(outputIndices.size() - 1);
    CellOutput output = txBuilder.getOutput(index);
    if (isMatched(output.type)) {
      if (output.type.args == null || output.type.args.length != 32) {
        output.type.args = ZERO_ARGS.clone();
      }
      return true;
    }

    return false;
  }

  @Override
  public void init(Network network) {

  }

  public static byte[] calculateTypeId(CellInput input, int index) {
    Blake2b blake2b = new Blake2b();
    blake2b.update(input.pack().toByteArray());
    blake2b.update(packUint64(index).toByteArray());
    return blake2b.doFinal();
  }

  @Override
  public boolean postBuild(int index, AbstractTransactionBuilder txBuilder, Object context) {
    CellOutput output = txBuilder.getOutput(index);
    if (null == output || !isMatched(output.type) || !Arrays.equals(output.type.args, ZERO_ARGS)) {
      return false;
    }
    CellInput input = txBuilder.getInput(0);
    if (input == null) {
      return false;
    }
    output.type.args = calculateTypeId(input, index);
    return true;
  }
}
