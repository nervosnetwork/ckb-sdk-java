package model;

import lombok.Data;

@Data
public class ToAccount {
    private String ident;
    private Action action;

    public ToAccount(String ident, Action action) {
        this.ident = ident;
        this.action = action;
    }
}
