// package org.nervos.mercury.model;
//
// import org.nervos.mercury.FeeConstant;
// import org.nervos.mercury.model.req.SmartTo;
// import org.nervos.mercury.model.req.SmartTransferPayload;
//
// import java.math.BigInteger;
// import java.util.ArrayList;
//
// public class SmartTransferPayloadBuilder extends SmartTransferPayload {
//  public SmartTransferPayloadBuilder() {
//    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
//    this.from = new ArrayList<>(1);
//    this.to = new ArrayList<>(1);
//  }
//
//  public void assetInfo(AssetInfo assetInfo) {
//    this.assetInfo = assetInfo;
//  }
//
//  public void addFrom(String address) {
//    this.from.add(address);
//  }
//
//  public void addTo(SmartTo to) {
//    this.to.add(to);
//  }
//
//  public void change(String address) {
//    this.change = address;
//  }
//
//  public void feeRate(BigInteger feeRate) {
//    this.feeRate = feeRate;
//  }
//
//  public SmartTransferPayload build() {
//    return this;
//  }
// }
