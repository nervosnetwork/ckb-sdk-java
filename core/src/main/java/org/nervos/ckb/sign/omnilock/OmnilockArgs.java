package org.nervos.ckb.sign.omnilock;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class OmnilockArgs {
  private int flag;
  private byte[] adminListCellTypeId;
  private Integer minimumCKBExponentInAcp;
  private Integer minimumSUDTExponentInAcp;
  // TODO: parse time lock
  private Long sinceForTimeLock;
  private byte[] typeScriptHashForSupply;

  public static OmnilockArgs decode(byte[] raw) {
    OmnilockArgs args = new OmnilockArgs();
    args.flag = raw[0];
    if (args.isAdminModeEnabled()) {
      args.adminListCellTypeId = Arrays.copyOfRange(raw, 1, 33);
    }
    if (args.isAnyoneCanPayModeEnabled()) {
      args.minimumCKBExponentInAcp = Byte.toUnsignedInt(raw[33]);
      args.minimumSUDTExponentInAcp = Byte.toUnsignedInt(raw[34]);
    }
    if (args.isTimeLockModeEnabled()) {
      ByteBuffer buffer = ByteBuffer.wrap(raw, 35, 8);
      args.sinceForTimeLock = buffer.getLong();
    }
    if (args.isSupplyModeEnabled()) {
      args.typeScriptHashForSupply = Arrays.copyOfRange(raw, 43, 74);
    }
    return args;
  }

  // TODO: complete
  public byte[] encode() {
    return new byte[0];
  }

  public boolean isAdminModeEnabled() {
    return (flag & 0x1) != 0;
  }

  public boolean isAnyoneCanPayModeEnabled() {
    return (flag & 0x10) != 0;
  }

  public boolean isTimeLockModeEnabled() {
    return (flag & 0x100) != 0;
  }

  public boolean isSupplyModeEnabled() {
    return (flag & 0x1000) != 0;
  }

  public int getFlag() {
    return flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  public byte[] getAdminListCellTypeId() {
    return adminListCellTypeId;
  }

  public void setAdminListCellTypeId(byte[] adminListCellTypeId) {
    this.adminListCellTypeId = adminListCellTypeId;
  }

  public Integer getMinimumCKBExponentInAcp() {
    return minimumCKBExponentInAcp;
  }

  public void setMinimumCKBExponentInAcp(Integer minimumCKBExponentInAcp) {
    this.minimumCKBExponentInAcp = minimumCKBExponentInAcp;
  }

  public Integer getMinimumSUDTExponentInAcp() {
    return minimumSUDTExponentInAcp;
  }

  public void setMinimumSUDTExponentInAcp(Integer minimumSUDTExponentInAcp) {
    this.minimumSUDTExponentInAcp = minimumSUDTExponentInAcp;
  }

  public Long getSinceForTimeLock() {
    return sinceForTimeLock;
  }

  public void setSinceForTimeLock(Long sinceForTimeLock) {
    this.sinceForTimeLock = sinceForTimeLock;
  }

  public byte[] getTypeScriptHashForSupply() {
    return typeScriptHashForSupply;
  }

  public void setTypeScriptHashForSupply(byte[] typeScriptHashForSupply) {
    this.typeScriptHashForSupply = typeScriptHashForSupply;
  }
}
