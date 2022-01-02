package org.example;

import org.example.tree.Expr;

public interface BinaryRule {

    Expr parse(Parser parser, Expr left, int currentPrecedence);

}
