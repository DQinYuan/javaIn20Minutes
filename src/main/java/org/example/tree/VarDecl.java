package org.example.tree;

import org.example.Token;

public class VarDecl {

    private final Token type;

    private final IdPrimary target;

    public VarDecl(Token type, IdPrimary target) {
        this.type = type;
        this.target = target;
    }

    public Token getType() {
        return type;
    }

    public IdPrimary getTarget() {
        return target;
    }
}
