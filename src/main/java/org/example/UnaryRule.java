package org.example;

import org.example.tree.Expr;

public interface UnaryRule {

    Expr parse(Parser parser, int currentPrecedence);

}
