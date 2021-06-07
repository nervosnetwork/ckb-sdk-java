package mercury;

import com.google.gson.Gson;
import model.*;
import model.resp.TransferCompletionResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.ScriptGroup;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.transaction.Transaction;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
           String balance = this.api.getCkbBalance("ckt1qyqr79tnk3pp34xp92gerxjc4p3mus2690psf0dd70");
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
        TransferPayloadBuilder payload = new TransferPayloadBuilder();
//        payload.addUdtHash("0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd");
        payload.addFrom(new FromAccount(Arrays.asList("ckt1qyqzse99vquwj6t32xyt6s7p25ymjlslam7s583h63"), Source.owned));
        payload.addItem(new ToAccount("ckt1qyqd3ygn34kjgkh59tlzygdke3nulp6856msqnkzet", Action.pay_by_from), new BigInteger("1000"));
        payload.addFee(new BigInteger("0"));

        try {
            System.out.println(g.toJson(payload.build()));
            TransferCompletionResponse s = this.api.transferCompletion(payload.build());

            for (int i = 0; i < s.getTx_view().inputs.size(); i++) {
                s.getTx_view().witnesses.add(i == 0 ? new Witness(Witness.SIGNATURE_PLACEHOLDER) : "0x");
            }

            Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.getTx_view());

            ScriptGroup sg = new ScriptGroup(regionToList(s.getSigs_entry().get(0).getIndex(), s.getTx_view().inputs.size()));
            signBuilder.sign(sg, "5997dcc69ae4949508adfd40e179e8d35209f33e47be9f162c023e7fb0a12c26");

            Transaction tx = signBuilder.buildTx();

            System.out.println(g.toJson(tx));
            ckbApi.sendTransaction(tx);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> regionToList(int start, int length) {
        List<Integer> integers = new ArrayList<>();
        for (int i = start; i < (start + length); i++) {
            integers.add(i);
        }
        return integers;
    }
}