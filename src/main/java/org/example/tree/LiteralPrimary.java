package org.example.tree;

import org.example.Token;

public class LiteralPrimary extends Primary {

    private final Object literal;

    public LiteralPrimary(Token keyToken, Object literal) {
        super(keyToken);
        this.literal = literal;
    }

    public Object getLiteral() {
        return literal;
    }
}
