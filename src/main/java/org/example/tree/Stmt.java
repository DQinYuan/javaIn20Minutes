package org.example.tree;

import org.example.Token;

public abstract class Stmt extends TreeNode {

    /**
     * key token in current syntax
     */
    private final Token keyToken;

    protected Stmt(Token keyToken) {
        this.keyToken = keyToken;
    }

    public Token getKeyToken() {
        return keyToken;
    }
}
