package org.nervos.ckb.sign.omnilock;

import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.utils.address.Address;

import java.util.Arrays;

public class OmnilockConfig {
  private AuthenticationArgs authenticationArgs;
  private OmnilockArgs omnilockArgs;
  private Mode mode;

  // For Auth mode with flag 0x06 (multisig)
  private Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript;

  // For Administrator mode
  private CellDep adminListCell;
  private OmnilockIdentity omnilockIdentity;

  public OmnilockConfig(String address, Mode mode) {
    this(Address.decode(address).getScript().args, mode);
  }

  public OmnilockConfig(byte[] args, Mode mode) {
    authenticationArgs = AuthenticationArgs.decode(args);
    omnilockArgs = OmnilockArgs.decode(Arrays.copyOfRange(args, 21, args.length));
    this.mode = mode;
  }

  public OmnilockConfig(AuthenticationArgs authenticationArgs, OmnilockArgs omnilockArgs, Mode mode) {
    this.authenticationArgs = authenticationArgs;
    this.omnilockArgs = omnilockArgs;
    this.mode = mode;
  }

  public byte[] encode() {
    byte[] args1 = authenticationArgs.encode();
    byte[] args2 = omnilockArgs.encode();

    byte[] args = new byte[args1.length + args2.length];
    System.arraycopy(args1, 0, args, 0, args1.length);
    System.arraycopy(args2, 0, args, args1.length, args2.length);
    return args;
  }

  public AuthenticationArgs getAuthenticationArgs() {
    return authenticationArgs;
  }

  public void setAuthenticationArgs(AuthenticationArgs authenticationArgs) {
    this.authenticationArgs = authenticationArgs;
  }

  public OmnilockArgs getOmnilockArgs() {
    return omnilockArgs;
  }

  public void setOmnilockArgs(OmnilockArgs omnilockArgs) {
    this.omnilockArgs = omnilockArgs;
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  public Secp256k1Blake160MultisigAllSigner.MultisigScript getMultisigScript() {
    return multisigScript;
  }

  public void setMultisigScript(Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript) {
    this.multisigScript = multisigScript;
  }

  public CellDep getAdminListCell() {
    return adminListCell;
  }

  public void setAdminListCell(CellDep adminListCell) {
    this.adminListCell = adminListCell;
  }

  public OmnilockIdentity getOmnilockIdentity() {
    return omnilockIdentity;
  }

  public void setOmnilockIdentity(OmnilockIdentity omnilockIdentity) {
    this.omnilockIdentity = omnilockIdentity;
  }

  public enum Mode {
    AUTH,
    ADMINISTRATOR
  }
}
