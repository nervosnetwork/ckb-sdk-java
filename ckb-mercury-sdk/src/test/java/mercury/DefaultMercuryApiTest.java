package mercury;

import com.google.gson.Gson;
import model.*;
import model.resp.TransferCompletionResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.ScriptGroup;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.transaction.Transaction;


import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMercuryApiTest {

    private static final String MERCURY_URL = "http://8.210.169.63:8116";

    public static final String NODE_URL = "http://8.210.169.63:8114";

    private static MercuryApi api;

    private static Api ckbApi;

    private static Gson g = new Gson();

    @BeforeAll
    static void initAll() {
        api = new DefaultMercuryApi(MERCURY_URL, false);
        ckbApi = new Api(NODE_URL, false);
    }

    @Test
    void getCkbBalance() {
        try {
//            ckt1qypzse99vquwj6t32xyt6s7p25ymjlslam7kg6nz58t
            // 1205400000000
           String balance = this.api.getCkbBalance("ckt1qyqzse99vquwj6t32xyt6s7p25ymjlslam7s583h63");
            System.out.println(balance);
           assertNotNull(balance, "Balance is not empty");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSudtBalance() {
        try {
            String balance = this.api.getSudtBalance("0x21f8128fab9c4b6093afd8bff495821cb0ff9e13be5ad1b0ba4c70457460d95c", "ckt1qyqd3ygn34kjgkh59tlzygdke3nulp6856msqnkzet");
            assertNotNull(balance, "Balance is not empty");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void transferCompletion() {
        TransferPayloadBuilder builder = new TransferPayloadBuilder();
//        builder.addUdtHash("0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd");
        builder.addFrom(new FromAccount(Arrays.asList("ckt1qyqd3ygn34kjgkh59tlzygdke3nulp6856msqnkzet"), Source.owned));
        builder.addItem(new ToAccount("ckt1qyqzse99vquwj6t32xyt6s7p25ymjlslam7s583h63", Action.pay_by_from), new BigInteger("1000"));
        builder.addFee(new BigInteger("464"));

        try {
//            System.out.println(g.toJson(builder.build()));
            TransferCompletionResponse s = this.api.transferCompletion(builder.build());
            List<ScriptGroup> scriptGroups = s.getScriptGroup();
            Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.getTx_view());

            for (ScriptGroup sg : scriptGroups) {
                signBuilder.sign(sg, "3c04883b003824c965d6779141b3b8cc5681e7f205a453fa36ad2d6f698518a1");
            }

            Transaction tx = signBuilder.buildTx();

            System.out.println(g.toJson(tx));
            String result = ckbApi.sendTransaction(tx);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createWallet() {
        CreateWalletPayloadBuilder builder = new CreateWalletPayloadBuilder();
        builder.address("ckt1qyqzse99vquwj6t32xyt6s7p25ymjlslam7s583h63");
        builder.addWalletInfo(new WalletInfo("0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd", new BigInteger("100"), new BigInteger("100")));
        builder.fee(new BigInteger("100"));

        try {
            TransferCompletionResponse s = api.createWallet(builder.build());
            System.out.println(s);

//            s.getTx_view().cellDeps.get(0).outPoint.txHash = "0xec26b0f85ed839ece5f11c4c4e837ec359f5adc4420410f6453b1f6b60fb96a6";
            List<ScriptGroup> scriptGroups = s.getScriptGroup();
            Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.getTx_view());

            for (ScriptGroup sg : scriptGroups) {
                signBuilder.sign(sg, "5997dcc69ae4949508adfd40e179e8d35209f33e47be9f162c023e7fb0a12c26");
            }


            Transaction tx = signBuilder.buildTx();

            System.out.println(g.toJson(tx));
            String result = ckbApi.sendTransaction(tx);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    void getAcpAddress() {
//        String address = getAcpAddress("ckt1qyqzse99vquwj6t32xyt6s7p25ymjlslam7s583h63", new BigInteger("100"), null);
//        System.out.println(address);
//
//        Script receiverScript = AddressParser.parse("ckt1qyqzse99vquwj6t32xyt6s7p25ymjlslam7s583h63").script;
//
//    }
//
//
//    private static final String ACP_CODE_HASH = "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356";
//
//    private String getAcpAddress(String address, BigInteger ckbMin, BigInteger sdutMin) {
//        Script receiverScript = AddressParser.parse(address).script;
//        receiverScript.codeHash = ACP_CODE_HASH;
//        if(ckbMin != null) {
//            receiverScript.args = receiverScript.args + ckbMin.toString();
//
//        }
//
//        if(sdutMin != null) {
//            receiverScript.args = receiverScript.args + sdutMin.toString();
//        }
//
//        return AddressGenerator.generate(Network.TESTNET, receiverScript);
//    }
}