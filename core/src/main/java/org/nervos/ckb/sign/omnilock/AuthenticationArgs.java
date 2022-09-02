package org.nervos.ckb.sign.omnilock;

public class AuthenticationArgs {
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

  public static AuthenticationArgs decode(byte[] args) {
    AuthenticationArgs authenticationArgs = new AuthenticationArgs();
    authenticationArgs.flag = AuthFlag.valueOf(args[0]);
    authenticationArgs.authContent = new byte[AUTH_CONTENT_LENGTH];
    System.arraycopy(args, 1, authenticationArgs, 0, AUTH_CONTENT_LENGTH);
    return authenticationArgs;
  }

  public byte[] encode() {
    byte[] encoded = new byte[21];
    encoded[0] = flag.getValue();
    System.arraycopy(authContent, 0, encoded, 1, AUTH_CONTENT_LENGTH);
    return encoded;
  }
}
