package org.example.tree;

import org.example.Token;

import java.util.List;

public class MethodCallPrimary extends Primary {

    /**
     * 方法调用对象
     */
    private final Primary primary;

    /**
     * 方法调用参数列表
     */
    private final List<Expr> arguments;

    /**
     * 方法名
     */
    private final Token methodName;

    public MethodCallPrimary(Token keyToken, Primary primary, List<Expr> arguments, Token methodName) {
        super(keyToken);
        this.primary = primary;
        this.arguments = arguments;
        this.methodName = methodName;
    }

    public Primary getPrimary() {
        return primary;
    }

    public List<Expr> getArguments() {
        return arguments;
    }

    public Token getMethodName() {
        return methodName;
    }
}
