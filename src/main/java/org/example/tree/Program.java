package org.example.tree;

import java.util.List;

public class Program {

    private final List<Stmt> stmtList;

    public Program(List<Stmt> stmtList) {
        this.stmtList = stmtList;
    }

    public List<Stmt> getStmtList() {
        return stmtList;
    }

    public <R> R accept(TreeVisitor<R> treeVisitor) {
        return treeVisitor.visitProgram(this);
    }
}
