package org.example;

public class ExprParseRule {

    // 优先级
    // = 赋值运算符优先级最低
    public static final int PREC_ASSIGN = 0;
    // + -
    public static final int PREC_ADD = 1;
    // * /
    public static final int PREC_MULTI = 2;
    // ++ -- + - !
    public static final int PREC_UNARY = 3;
    // () 超过一切运算符的优先级
    public static final int PREC_GROUP = 4;

    private final UnaryRule unaryRule;

    private final BinaryRule binaryRule;

    private final int precedence;

    public ExprParseRule(UnaryRule unaryRule, BinaryRule binaryRule, int precedence) {
        this.unaryRule = unaryRule;
        this.binaryRule = binaryRule;
        this.precedence = precedence;
    }

    public UnaryRule getUnaryRule() {
        return unaryRule;
    }

    public BinaryRule getBinaryRule() {
        return binaryRule;
    }

    public int getPrecedence() {
        return precedence;
    }
}
