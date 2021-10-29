package org.nervos.mercury.model.resp.signature;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SignatureAction {
  @SerializedName("signature_location")
  public SignatureLocation signatureLocation;

  @SerializedName("signature_info")
  public SignatureInfo signatureInfo;

  @SerializedName("hash_algorithm")
  public HashAlgorithmEnum hashAlgorithmEnum;

  @SerializedName("other_indexes_in_group")
  public List<Integer> otherIndexesInGroup;
}
