package org.example.tree;

import org.example.Token;

public abstract class Expr extends Stmt {
    public Expr(Token keyToken) {
        super(keyToken);
    }
}
