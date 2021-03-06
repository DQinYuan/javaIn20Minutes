package org.example.tree;

import org.example.Token;

import java.util.List;

public class Block extends Stmt {

    private final List<Stmt> stmtList;

    public Block(Token keyToken, List<Stmt> stmtList) {
        super(keyToken);
        this.stmtList = stmtList;
    }

    public List<Stmt> getStmtList() {
        return stmtList;
    }

    @Override
    public <R> R accept(TreeVisitor<R> treeVisitor) {
        return treeVisitor.visitBlock(this);
    }
}
