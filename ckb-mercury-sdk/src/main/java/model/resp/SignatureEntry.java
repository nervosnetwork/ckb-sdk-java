package model.resp;

import lombok.Data;

@Data
public class SignatureEntry {
    private String type;
    private Integer index;
    private String pub_key;
    private Byte[] message;
}
