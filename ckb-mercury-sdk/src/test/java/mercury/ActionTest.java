package mercury;

import com.google.gson.Gson;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbHolder;
import mercury.constant.MercuryApiHolder;
import model.*;
import model.resp.MercuryScriptGroup;
import model.resp.TransferCompletionResponse;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.transaction.Transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActionTest {

    Gson g = new Gson();


    @Test
    void transferCompletionCkbWithPayByFrom() {
        TransferPayloadBuilder builder = new TransferPayloadBuilder();
        builder.addFrom(new FromAccount(Arrays.asList(AddressWithKeyHolder.testAddress1()), Source.owned));
        builder.addItem(new ToAccount(AddressWithKeyHolder.testAddress2(), Action.pay_by_from), new BigInteger("100"));
        builder.addFee(new BigInteger("464"));

        try {
            TransferCompletionResponse s = MercuryApiHolder.getApi().transferCompletion(builder.build());
            Transaction tx = sign(s);

            String result = CkbHolder.getApi().sendTransaction(tx);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void transferCompletionSudtWithPayByFrom() {
        TransferPayloadBuilder builder = new TransferPayloadBuilder();
        builder.addUdtHash("0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd");
        builder.addFrom(new FromAccount(Arrays.asList(AddressWithKeyHolder.testAddress1()), Source.owned));
        builder.addItem(new ToAccount(AddressWithKeyHolder.testAddress2(), Action.pay_by_from), new BigInteger("100"));
        builder.addFee(new BigInteger("464"));

        try {
            TransferCompletionResponse s = MercuryApiHolder.getApi().transferCompletion(builder.build());
            Transaction tx = sign(s);

            String result = CkbHolder.getApi().sendTransaction(tx);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void transferCompletionCkbWithLendByFrom() {
        TransferPayloadBuilder builder = new TransferPayloadBuilder();
        builder.addFrom(new FromAccount(Arrays.asList(AddressWithKeyHolder.testAddress1()), Source.owned));
        builder.addItem(new ToAccount(AddressWithKeyHolder.testAddress2(), Action.lend_by_from), new BigInteger("100"));
        builder.addFee(new BigInteger("464"));

        try {
            TransferCompletionResponse s = MercuryApiHolder.getApi().transferCompletion(builder.build());
        } catch (Exception e) {
            assertEquals("The transaction does not support ckb", e.getMessage());
        }
    }

    @Test
    void transferCompletionSudtWithLendByFrom() {
        TransferPayloadBuilder builder = new TransferPayloadBuilder();
        builder.addUdtHash("0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd");
        builder.addFrom(new FromAccount(Arrays.asList(AddressWithKeyHolder.testAddress1()), Source.owned));
        builder.addItem(new ToAccount(AddressWithKeyHolder.testAddress2(), Action.lend_by_from), new BigInteger("100"));
        builder.addFee(new BigInteger("464"));

        try {
            TransferCompletionResponse s = MercuryApiHolder.getApi().transferCompletion(builder.build());
            Transaction tx = sign(s);
            System.out.println(g.toJson(s.txView));

            String result = CkbHolder.getApi().sendTransaction(tx);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void transferCompletionCkbWithPayByTo() {
        TransferPayloadBuilder builder = new TransferPayloadBuilder();
        builder.addFrom(new FromAccount(Arrays.asList(AddressWithKeyHolder.testAddress1()), Source.owned));
        builder.addItem(new ToAccount(AddressWithKeyHolder.testAddress1(), Action.pay_by_to), new BigInteger("100"));
        builder.addFee(new BigInteger("464"));

        try {
            TransferCompletionResponse s = MercuryApiHolder.getApi().transferCompletion(builder.build());
        } catch (Exception e) {
            assertEquals("The transaction does not support ckb", e.getMessage());
        }
    }

    @Test
    void transferCompletionSudtWithPayByTo() {
        TransferPayloadBuilder builder = new TransferPayloadBuilder();
        builder.addUdtHash("0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd");
        builder.addFrom(new FromAccount(Arrays.asList(AddressWithKeyHolder.testAddress1()), Source.owned));
        builder.addItem(new ToAccount(AddressWithKeyHolder.testAddress4(), Action.pay_by_to), new BigInteger("100"));
        builder.addFee(new BigInteger("464"));

        System.out.println(g.toJson(builder.build()));

        try {
            TransferCompletionResponse s = MercuryApiHolder.getApi().transferCompletion(builder.build());
            Transaction tx = sign(s);
            System.out.println(g.toJson(s.txView));

            String result = CkbHolder.getApi().sendTransaction(tx);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Transaction sign(TransferCompletionResponse s) throws IOException {
        List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
        Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.txView);
        for (MercuryScriptGroup sg : scriptGroups) {
            signBuilder.sign(sg, AddressWithKeyHolder.getKey(sg.pubKey));
        }

        Transaction tx = signBuilder.buildTx();
        return tx;
    }
}
