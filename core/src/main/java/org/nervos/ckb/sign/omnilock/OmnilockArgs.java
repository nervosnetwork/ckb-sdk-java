package org.nervos.ckb.sign.omnilock;

import org.nervos.ckb.utils.address.Address;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class OmnilockArgs {
  private Authentication authentication;
  private OmniConfig omniConfig;

  public OmnilockArgs(String address) {
    this(Address.decode(address).getScript().args);
  }

  public OmnilockArgs(byte[] args) {
    authentication = Authentication.decode(args);
    omniConfig = OmniConfig.decode(Arrays.copyOfRange(args, 21, args.length));
  }

  public OmnilockArgs(Authentication authentication, OmniConfig omniConfig) {
    this.authentication = authentication;
    this.omniConfig = omniConfig;
  }

  public byte[] encode() {
    byte[] args1 = authentication.encode();
    byte[] args2 = omniConfig.encode();

    byte[] args = new byte[args1.length + args2.length];
    System.arraycopy(args1, 0, args, 0, args1.length);
    System.arraycopy(args2, 0, args, args1.length, args2.length);
    return args;
  }

  public Authentication getAuthenticationArgs() {
    return authentication;
  }

  public void setAuthenticationArgs(Authentication authentication) {
    this.authentication = authentication;
  }

  public OmniConfig getOmniArgs() {
    return omniConfig;
  }

  public void setOmniArgs(OmniConfig omniConfig) {
    this.omniConfig = omniConfig;
  }


  public static class Authentication {
    private static int AUTH_CONTENT_LENGTH = 20;
    private AuthFlag flag;
    private byte[] authContent;

    public AuthFlag getFlag() {
      return flag;
    }

    public void setFlag(AuthFlag flag) {
      this.flag = flag;
    }

    public byte[] getAuthContent() {
      return authContent;
    }

    public void setAuthContent(byte[] authContent) {
      this.authContent = authContent;
    }

    public static Authentication decode(byte[] args) {
      Authentication authentication = new Authentication();
      authentication.flag = AuthFlag.valueOf(args[0]);
      authentication.authContent = new byte[AUTH_CONTENT_LENGTH];
      System.arraycopy(args, 1, authentication.authContent, 0, AUTH_CONTENT_LENGTH);
      return authentication;
    }

    public byte[] encode() {
      byte[] encoded = new byte[21];
      encoded[0] = flag.getValue();
      System.arraycopy(authContent, 0, encoded, 1, AUTH_CONTENT_LENGTH);
      return encoded;
    }
  }


  public static class OmniConfig {
    private int flag;
    private byte[] adminListCellTypeId;
    private Integer minimumCKBExponentInAcp;
    private Integer minimumSUDTExponentInAcp;
    // TODO: parse time lock
    private Long sinceForTimeLock;
    private byte[] typeScriptHashForSupply;

    public static OmniConfig decode(byte[] raw) {
      OmniConfig args = new OmniConfig();
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

    public byte[] encode() {
      byte[] out = new byte[1];
      out[0] = (byte) flag;
      // TODO: complete other fields encoding
      return out;
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
}
