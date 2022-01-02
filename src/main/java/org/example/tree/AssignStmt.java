package org.example.tree;

import org.example.Token;

public class AssignStmt extends Stmt {

    private final Token target;

    private final Expr expr;

    public AssignStmt(Token keyToken, Token target, Expr expr) {
        super(keyToken);
        this.target = target;
        this.expr = expr;
    }

    public Token getTarget() {
        return target;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public <R> R accept(TreeVisitor<R> treeVisitor) {
        return treeVisitor.visitAssignStmt(this);
    }
}
