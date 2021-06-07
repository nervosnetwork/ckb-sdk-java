package model;

import lombok.Data;

import java.util.List;

@Data
public class FromAccount {
    private List<String> idents;
    private Source source;

    public FromAccount(List<String> idents, Source source) {
        this.idents = idents;
        this.source = source;
    }
}
