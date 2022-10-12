package org.nervos.ckb.sign.omnilock;

import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import java.io.ByteArrayOutputStream;
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
    return Numeric.concatBytes(args1, args2);
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
        args.typeScriptHashForSupply = Arrays.copyOfRange(raw, 43, 75);
      }
      return args;
    }

    private void writeBytes(ByteArrayOutputStream out, byte[] bytes) {
      out.write(bytes, 0, bytes.length);
    }

    public byte[] encode() {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      writeBytes(out, new byte[]{(byte) flag});
      if (isAdminModeEnabled()) {
        writeBytes(out, adminListCellTypeId);
      } else {
        return out.toByteArray();
      }
      if (isAnyoneCanPayModeEnabled()) {
        writeBytes(out, new byte[]{minimumCKBExponentInAcp.byteValue()});
        if (minimumSUDTExponentInAcp != null) {
          writeBytes(out, new byte[]{minimumSUDTExponentInAcp.byteValue()});
        } else {
          return out.toByteArray();
        }
      } else {
        return out.toByteArray();
      }
      if (isTimeLockModeEnabled()) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(sinceForTimeLock);
        writeBytes(out, buffer.array());
      } else {
        return out.toByteArray();
      }
      if (isSupplyModeEnabled()) {
        writeBytes(out, typeScriptHashForSupply);
      }
      return out.toByteArray();
    }

    public boolean isAdminModeEnabled() {
      return (flag & 0b1) != 0;
    }

    public boolean isAnyoneCanPayModeEnabled() {
      return (flag & 0b10) != 0;
    }

    public boolean isTimeLockModeEnabled() {
      return (flag & 0b100) != 0;
    }

    public boolean isSupplyModeEnabled() {
      return (flag & 0b1000) != 0;
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
