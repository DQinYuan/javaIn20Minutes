package org.example.tree;

import org.example.Token;

public class IfStmt extends Stmt {

    private final Expr condition;

    private final Stmt thenBranch;

    private final Stmt elseBranch;

    public IfStmt(Token keyToken, Expr condition, Stmt thenBranch, Stmt elseBranch) {
        super(keyToken);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expr getCondition() {
        return condition;
    }

    public Stmt getThenBranch() {
        return thenBranch;
    }

    public Stmt getElseBranch() {
        return elseBranch;
    }

    @Override
    public <R> R accept(TreeVisitor<R> treeVisitor) {
        return treeVisitor.visitIfStmt(this);
    }
}
