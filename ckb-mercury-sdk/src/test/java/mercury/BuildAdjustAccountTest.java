package mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Keys;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.mercury.model.AdjustAccountPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class BuildAdjustAccountTest {

  @Test
  void testCreateAsset()
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

    ECKeyPair keyPair = Keys.createEcKeyPair();
    Address newAddress = new Address(Script.generateSecp256K1Blake160SignhashAllScript(keyPair), Network.TESTNET);
    AddressWithKeyHolder.put(newAddress.encode(), Numeric.toHexString(keyPair.getEncodedPrivateKey()));

    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.setItem(ItemFactory.newIdentityItemByCkb(Numeric.toHexString(newAddress.getScript().args)));
    builder.setAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.addFrom(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey3()));
    builder.setAccountNumber(1);
    builder.setExtraCkb(AmountUtils.ckbToShannon(200));

    TransactionCompletionResponse s =
        ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

    Assertions.assertNotNull(s.txView);
    Assertions.assertNotNull(s.scriptGroups);
  }
}
