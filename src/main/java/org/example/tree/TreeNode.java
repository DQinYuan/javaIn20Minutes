package org.example.tree;

public abstract class TreeNode {

    public abstract <R> R accept(TreeVisitor<R> treeVisitor);

}
