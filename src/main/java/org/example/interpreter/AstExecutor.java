package org.example.interpreter;

import org.example.ErrorReporter;
import org.example.Token;
import org.example.TokenType;
import org.example.tree.AssignStmt;
import org.example.tree.BinaryOp;
import org.example.tree.Block;
import org.example.tree.Expr;
import org.example.tree.IdPrimary;
import org.example.tree.IfStmt;
import org.example.tree.LambdaExpr;
import org.example.tree.LiteralPrimary;
import org.example.tree.MethodCallPrimary;
import org.example.tree.Primary;
import org.example.tree.PrintlnStmt;
import org.example.tree.Program;
import org.example.tree.Return;
import org.example.tree.Stmt;
import org.example.tree.TreeVisitor;
import org.example.tree.UnaryOp;
import org.example.tree.VarDecl;
import org.example.tree.VarDeclStmt;
import org.example.util.numbermath.NumberMath;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AstExecutor implements TreeVisitor<TreeNodeResult> {

    private ScopeStack currentScope = new ScopeStack(new HashMap<>(), null);

    @Override
    public TreeNodeResult visitProgram(Program program) {
        for (Stmt stmt : program.getStmtList()) {
            TreeNodeResult stmtRet = stmt.accept(this);
            if (stmtRet.isRet()) {
                return stmtRet;
            }
        }
        return TreeNodeResult.NULL_RESULT;
    }

    @Override
    public TreeNodeResult visitAssignStmt(AssignStmt assignStmt) {
        Token targetToken = assignStmt.getTarget();
        String variableName = targetToken.getLexeme();
        TreeNodeResult exprResult = assignStmt.getExpr().accept(this);
        Value variable = currentScope.getVariableValue(variableName);
        if (variable == null) {
            throw new RuntimeException(ErrorReporter.report(targetToken.getLine(), targetToken.getCol(),
                    "not define variable " + variableName));
        }
        variable.set(exprResult.getValueObj());
        return exprResult;
    }

    @Override
    public TreeNodeResult visitBinaryOp(BinaryOp binaryOp) {
        Object leftResult = binaryOp.getLeft().accept(this).getValueObj();
        Object rightResult = binaryOp.getRight().accept(this).getValueObj();
        switch (binaryOp.getOp().getType()) {
            case ADD:
                if (leftResult instanceof Number && rightResult instanceof Number) {
                    // 数字运算
                    return TreeNodeResult.newResultWithValue(NumberMath.add((Number) leftResult,
                            (Number) rightResult));
                }
                // 字符串运算
                return TreeNodeResult.newResultWithValue(leftResult + String.valueOf(rightResult));
            case SUB:
                if (leftResult instanceof Number && rightResult instanceof Number) {
                    return TreeNodeResult.newResultWithValue(NumberMath.subtract((Number) leftResult,
                            (Number) rightResult));
                }
                throw new RuntimeException(ErrorReporter.report(binaryOp.getOp().getLine(),
                        binaryOp.getOp().getCol(), "'-' can only apply to number"));
            case MUL:
                if (leftResult instanceof Number && rightResult instanceof Number) {
                    return TreeNodeResult.newResultWithValue(NumberMath.multiply((Number) leftResult,
                            (Number) rightResult));
                }
                throw new RuntimeException(ErrorReporter.report(binaryOp.getOp().getLine(),
                        binaryOp.getOp().getCol(), "'*' can only apply to number"));
            case DIV:
                if (leftResult instanceof Number && rightResult instanceof Number) {
                    return TreeNodeResult.newResultWithValue(NumberMath.divide((Number) leftResult,
                            (Number) rightResult));
                }
                throw new RuntimeException(ErrorReporter.report(binaryOp.getOp().getLine(),
                        binaryOp.getOp().getCol(), "'/' can only apply to number"));
            case GT:
                if (leftResult instanceof Number && rightResult instanceof Number) {
                    return TreeNodeResult.newResultWithValue(NumberMath.compareTo((Number) leftResult,
                            (Number) rightResult) > 0);
                }
                throw new RuntimeException(ErrorReporter.report(binaryOp.getOp().getLine(),
                        binaryOp.getOp().getCol(), "'>' can only apply to number"));
            case LT:
                if (leftResult instanceof Number && rightResult instanceof Number) {
                    return TreeNodeResult.newResultWithValue(NumberMath.compareTo((Number) leftResult,
                            (Number) rightResult) < 0);
                }
                throw new RuntimeException(ErrorReporter.report(binaryOp.getOp().getLine(),
                        binaryOp.getOp().getCol(), "'<' can only apply to number"));
            case EQUAL:
                return TreeNodeResult.newResultWithValue(Objects.equals(leftResult, rightResult));
            case NOTEQUAL:
                return TreeNodeResult.newResultWithValue(!Objects.equals(leftResult, rightResult));
            default:
                throw new RuntimeException(ErrorReporter.report(binaryOp.getOp().getLine(),
                        binaryOp.getOp().getCol(),
                        "unknown binary operator " + binaryOp.getOp().getLexeme()));
        }
    }

    @Override
    public TreeNodeResult visitBlock(Block block) {
        for (Stmt stmt : block.getStmtList()) {
            TreeNodeResult stmtRes = stmt.accept(this);
            if (stmtRes.isRet()) {
                return stmtRes;
            }
        }
        return TreeNodeResult.NULL_RESULT;
    }

    public Value executeLambdaBody(LambdaExpr lambdaExpr, ScopeStack lambdaScope) {
        ScopeStack pre = currentScope;
        currentScope = lambdaScope;

        Value result;
        if (lambdaExpr.getExprBody() != null) {
            result = lambdaExpr.getExprBody().accept(this).getValue();
        } else if (lambdaExpr.getBlockBody() != null) {
            result = lambdaExpr.getBlockBody().accept(this).getValue();
        } else {
            throw new RuntimeException(ErrorReporter.report(lambdaExpr.getKeyToken().getLine(),
                    lambdaExpr.getKeyToken().getCol(), "invalid lambda body"));
        }

        currentScope = pre;
        return result;
    }

    @Override
    public TreeNodeResult visitPrimary(Primary primary) {
        if (primary instanceof LiteralPrimary) {
            return TreeNodeResult.newResultWithValue(((LiteralPrimary) primary).getLiteral());
        } else if (primary instanceof IdPrimary) {
            IdPrimary idPrimary = (IdPrimary) primary;
            Value variableValue = currentScope.getVariableValue(idPrimary.getKeyToken().getLexeme());
            if (variableValue == null) {
                throw new RuntimeException(ErrorReporter.report(primary.getKeyToken().getLine(),
                        primary.getKeyToken().getCol(),
                        "undefined variable " + idPrimary.getKeyToken().getLexeme()));
            }
            return TreeNodeResult.newResultWithValue(variableValue);
        } else if (primary instanceof MethodCallPrimary) {
            MethodCallPrimary methodCallPrimary = (MethodCallPrimary) primary;
            Object obj = visitPrimary(methodCallPrimary.getPrimary()).getValueObj();
            // 为了简单起见, 目前只支持 MyLambda 的 apply 方法调用
            if (!(obj instanceof MyLambda)) {
                throw new RuntimeException(ErrorReporter.report(primary.getKeyToken().getLine(),
                        primary.getKeyToken().getCol(),
                        "obj with class " + obj + " without method"));
            }
            String methodName = methodCallPrimary.getMethodName().getLexeme();
            if (!"apply".equals(methodName)) {
                throw new RuntimeException(ErrorReporter.report(primary.getKeyToken().getLine(),
                        primary.getKeyToken().getCol(),
                        "unknown method " + methodName));
            }
            List<Value> argumentsValue = methodCallPrimary.getArguments().stream()
                    .map(expr -> expr.accept(this))
                    .map(TreeNodeResult::getValue)
                    .collect(Collectors.toList());
            MyLambda lambda = (MyLambda) obj;
            return TreeNodeResult.newResultWithValue(lambda.execute(argumentsValue, this));
        }
        throw new RuntimeException(ErrorReporter.report(primary.getKeyToken().getLine(),
                primary.getKeyToken().getCol(),
                "unknown primary " + primary.getClass().getSimpleName()));
    }

    @Override
    public TreeNodeResult visitIfStmt(IfStmt ifStmt) {
        Object conditionValue = ifStmt.getCondition().accept(this).getValueObj();
        if (!(conditionValue instanceof Boolean)) {
            throw new RuntimeException(ErrorReporter.report(ifStmt.getKeyToken().getLine(),
                    ifStmt.getKeyToken().getCol(), "if condition must return boolean"));
        }
        Boolean condition = (Boolean) conditionValue;
        TreeNodeResult treeNodeResult;
        if (condition) {
            treeNodeResult = ifStmt.getThenBranch().accept(this);
        } else {
            treeNodeResult = ifStmt.getElseBranch().accept(this);
        }

        return treeNodeResult.isRet()? treeNodeResult: TreeNodeResult.NULL_RESULT;
    }

    @Override
    public TreeNodeResult visitLambdaExpr(LambdaExpr lambdaExpr) {
        return TreeNodeResult.newResultWithValue(new MyLambda(currentScope, lambdaExpr));
    }

    @Override
    public TreeNodeResult visitReturn(Return ret) {
        Expr expr = ret.getExpr();
        if (expr == null) {
            return TreeNodeResult.NULL_RESULT;
        }
        return TreeNodeResult.newReturnResult(expr.accept(this).getValue());
    }

    @Override
    public TreeNodeResult visitUnaryOp(UnaryOp unaryOp) {
        Object exprResult = unaryOp.getExpr().accept(this).getValueObj();
        switch (unaryOp.getOp().getType()) {
            case BANG:
                if (!(exprResult instanceof Boolean)) {
                    throw new RuntimeException(ErrorReporter.report(unaryOp.getOp().getLine(),
                            unaryOp.getOp().getCol(),
                            "'!' can only apply to boolean"));
                }
                return TreeNodeResult.newResultWithValue(!((Boolean) exprResult));
            case ADD:
                if (!(exprResult instanceof Number)) {
                    throw new RuntimeException(ErrorReporter.report(unaryOp.getOp().getLine(),
                            unaryOp.getOp().getCol(),
                            "'+' can only apply to number"));
                }
                return TreeNodeResult.newResultWithValue(exprResult);
            case SUB:
                if (!(exprResult instanceof Number)) {
                    throw new RuntimeException(ErrorReporter.report(unaryOp.getOp().getLine(),
                            unaryOp.getOp().getCol(),
                            "'-' can only apply to number"));
                }
                return TreeNodeResult.newResultWithValue(NumberMath.multiply(-1,
                        (Number) exprResult));
            default:
                throw new RuntimeException(ErrorReporter.report(unaryOp.getOp().getLine(),
                        unaryOp.getOp().getCol(),
                        "unknown unary operator " + unaryOp.getOp().getLexeme()));
        }
    }

    @Override
    public TreeNodeResult visitVarDeclStmt(VarDeclStmt varDeclStmt) {
        Object initValue = varDeclStmt.getInit().accept(this).getValueObj();
        VarDecl varDecl = varDeclStmt.getVarDecl();
        String varName = varDecl.getTarget().getKeyToken().getLexeme();
        if (currentScope.existInCurrentScope(varName)) {
            // 变量不允许重复定义
            throw new RuntimeException(ErrorReporter.report(
                    varDeclStmt.getKeyToken().getLine(),
                    varDeclStmt.getKeyToken().getCol(),
                    "duplicate variable definition"
            ));
        }
        currentScope.put(varName, new LeftValue(initValue));

        return TreeNodeResult.newResultWithValue(initValue);
    }

    @Override
    public TreeNodeResult visitPrintlnStmt(PrintlnStmt printlnStmt) {
        System.out.println(printlnStmt.getExpr().accept(this).getValueObj());
        return TreeNodeResult.NULL_RESULT;
    }
}
