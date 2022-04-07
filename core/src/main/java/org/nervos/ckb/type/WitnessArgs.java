package org.nervos.ckb.type;

public class WitnessArgs {
    private byte[] lock;
    private byte[] inputType;
    private byte[] outputType;

    public byte[] getLock() {
        return lock;
    }

    public void setLock(byte[] lock) {
        this.lock = lock;
    }

    public byte[] getInputType() {
        return inputType;
    }

    public void setInputType(byte[] inputType) {
        this.inputType = inputType;
    }

    public byte[] getOutputType() {
        return outputType;
    }

    public void setOutputType(byte[] outputType) {
        this.outputType = outputType;
    }
}
