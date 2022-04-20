package org.nervos.mercury.model.resp.signature;

import java.util.List;

public class SignatureAction {
  public SignatureLocation signatureLocation;
  public SignatureInfo signatureInfo;
  public HashAlgorithmEnum hashAlgorithmEnum;
  public List<Integer> otherIndexesInGroup;
}
