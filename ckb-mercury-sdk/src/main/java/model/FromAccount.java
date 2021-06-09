package model;

import lombok.Data;

import java.util.List;

public class FromAccount {
    public List<String> idents;
    public Source source;

    public FromAccount(List<String> idents, Source source) {
        this.idents = idents;
        this.source = source;
    }
}
