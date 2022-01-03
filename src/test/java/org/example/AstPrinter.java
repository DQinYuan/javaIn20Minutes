package org.example;

import org.example.tree.BinaryOp;
import org.example.tree.Block;
import org.example.tree.Expr;
import org.example.tree.IfStmt;
import org.example.tree.LambdaExpr;
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

import java.io.PrintStream;

public class AstPrinter implements TreeVisitor<Void> {

    private final PrintStream printStream;

    private int depth = 0;

    public AstPrinter(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public Void visitProgram(Program program) {
        for (Stmt stmt : program.getStmtList()) {
            stmt.accept(this);
        }
        return null;
    }

    @Override
    public Void visitBinaryOp(BinaryOp binaryOp) {
        printByCurDepth(binaryOp.getClass().getSimpleName());
        depth++;
        binaryOp.getLeft().accept(this);
        printByCurDepth(binaryOp.getOp().getLexeme());
        binaryOp.getRight().accept(this);
        depth--;
        return null;
    }

    @Override
    public Void visitBlock(Block block) {
        printByCurDepth(block.getClass().getSimpleName());
        depth++;
        for (Stmt stmt : block.getStmtList()) {
            stmt.accept(this);
        }
        depth--;
        return null;
    }

    @Override
    public Void visitPrimary(Primary primary) {
        printByCurDepth(primary.getClass().getSimpleName());
        depth++;
        if (primary instanceof MethodCallPrimary) {
            MethodCallPrimary methodCallPrimary = (MethodCallPrimary) primary;
            methodCallPrimary.getPrimary().accept(this);
            printByCurDepth("methodName " + methodCallPrimary.getMethodName().getLexeme());
            printByCurDepth("arguments");
            depth++;
            for (Expr argument : methodCallPrimary.getArguments()) {
                argument.accept(this);
            }
            depth--;
        } else {
            printByCurDepth(primary.getKeyToken().getLexeme());
        }
        depth--;
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt ifStmt) {
        printByCurDepth(ifStmt.getClass().getSimpleName());
        depth++;
        ifStmt.getCondition().accept(this);
        ifStmt.getThenBranch().accept(this);
        ifStmt.getElseBranch().accept(this);
        depth--;
        return null;
    }

    @Override
    public Void visitLambdaExpr(LambdaExpr lambdaExpr) {
        printByCurDepth(lambdaExpr.getClass().getSimpleName());
        depth++;
        // 打印参数
        printByCurDepth("parameters");
        depth++;
        for (VarDecl parameter : lambdaExpr.getParameters()) {
            printByCurDepth(parameter.getType().getLexeme() + " " +
                    parameter.getTarget().getKeyToken().getLexeme());
        }
        depth--;

        if (lambdaExpr.getExprBody() != null) {
            lambdaExpr.getExprBody().accept(this);
        } else if (lambdaExpr.getBlockBody() != null) {
            lambdaExpr.getBlockBody().accept(this);
        }

        depth--;
        return null;
    }

    @Override
    public Void visitReturn(Return ret) {
        printByCurDepth(ret.getClass().getSimpleName());
        depth++;
        ret.getExpr().accept(this);
        depth--;
        return null;
    }

    @Override
    public Void visitUnaryOp(UnaryOp unaryOp) {
        printByCurDepth(unaryOp.getClass().getSimpleName());
        depth++;
        printByCurDepth("op " + unaryOp.getOp().getLexeme());
        unaryOp.getExpr().accept(this);
        depth--;
        return null;
    }

    @Override
    public Void visitVarDeclStmt(VarDeclStmt varDeclStmt) {
        printByCurDepth(varDeclStmt.getClass().getSimpleName());
        depth++;
        printByCurDepth("type " + varDeclStmt.getVarDecl().getType().getLexeme());
        printByCurDepth("target " + varDeclStmt.getVarDecl().getTarget().getKeyToken().getLexeme());
        if (varDeclStmt.getInit() != null) {
            varDeclStmt.getInit().accept(this);
        }
        depth--;
        return null;
    }

    @Override
    public Void visitPrintlnStmt(PrintlnStmt printlnStmt) {
        printByCurDepth(printlnStmt.getClass().getSimpleName());
        depth++;
        printlnStmt.getExpr().accept(this);
        depth--;
        return null;
    }

    private void printByCurDepth(String str) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            if (i == depth - 1) {
                builder.append("| ");
            } else {
                builder.append("  ");
            }
        }
        builder.append(str);
        printStream.println(builder.toString());
    }
}
