package org.example.tree;

import org.example.Token;

public class PrintlnStmt extends Stmt {

    private final Expr expr;

    public PrintlnStmt(Token keyToken, Expr expr) {
        super(keyToken);
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public <R> R accept(TreeVisitor<R> treeVisitor) {
        return treeVisitor.visitPrintlnStmt(this);
    }
}
