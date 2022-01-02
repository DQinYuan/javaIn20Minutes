package org.example.tree;

import org.example.Token;

public class UnaryOp extends Expr {

    private final Token op;

    private final Expr expr;

    public UnaryOp(Token keyToken, Token op, Expr expr) {
        super(keyToken);
        this.op = op;
        this.expr = expr;
    }

    public Token getOp() {
        return op;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public <R> R accept(TreeVisitor<R> treeVisitor) {
        return treeVisitor.visitUnaryOp(this);
    }
}
