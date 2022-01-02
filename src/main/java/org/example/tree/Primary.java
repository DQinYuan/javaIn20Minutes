package org.example.tree;

import org.example.Token;

public abstract class Primary extends Expr {
    public Primary(Token keyToken) {
        super(keyToken);
    }

    @Override
    public <R> R accept(TreeVisitor<R> treeVisitor) {
        return treeVisitor.visitPrimary(this);
    }
}
