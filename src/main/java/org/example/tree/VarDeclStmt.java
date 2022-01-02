package org.example.tree;

import org.example.Token;

public class VarDeclStmt extends Stmt {

    private final VarDecl varDecl;

    private final Expr init;

    public VarDeclStmt(Token keyToken, VarDecl varDecl, Expr init) {
        super(keyToken);
        this.varDecl = varDecl;
        this.init = init;
    }

    public VarDecl getVarDecl() {
        return varDecl;
    }

    public Expr getInit() {
        return init;
    }

    @Override
    public <R> R accept(TreeVisitor<R> treeVisitor) {
        return treeVisitor.visitVarDeclStmt(this);
    }
}
