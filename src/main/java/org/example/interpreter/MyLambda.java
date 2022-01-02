package org.example.interpreter;

import org.example.tree.LambdaExpr;
import org.example.tree.VarDecl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyLambda {

    /**
     * lambda 所在作用域
     */
    private final ScopeStack scope;

    private final LambdaExpr lambdaDefine;

    public MyLambda(ScopeStack scope, LambdaExpr lambdaDefine) {
        this.scope = scope;
        this.lambdaDefine = lambdaDefine;
    }

    /**
     * @return 参数数量
     */
    public int arity() {
        return lambdaDefine.getParameters().size();
    }

    public Value execute(List<Value> arguments, AstExecutor astExecutor) {
        Map<String, Value> context = new HashMap<>();
        for (int i = 0; i < arguments.size(); i++) {
            Value argI = arguments.get(i);
            VarDecl paramIDef = lambdaDefine.getParameters().get(i);
            context.put(paramIDef.getTarget().getKeyToken().getLexeme(), argI);
        }
        ScopeStack lambdaExecuteScope = new ScopeStack(context, scope);
        return astExecutor.executeLambdaBody(lambdaDefine, lambdaExecuteScope);
    }
}
