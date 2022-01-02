package org.example.tree;

import org.example.Token;

public class BinaryOp extends Expr {

    private final Expr left;

    private final Token op;

    private final Expr right;

    public BinaryOp(Token keyToken, Expr left, Token op, Expr right) {
        super(keyToken);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Token getOp() {
        return op;
    }

    public Expr getRight() {
        return right;
    }

    @Override
    public <R> R accept(TreeVisitor<R> treeVisitor) {
        return treeVisitor.visitBinaryOp(this);
    }
}
