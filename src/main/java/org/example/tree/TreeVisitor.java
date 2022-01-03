package org.example.tree;

public interface TreeVisitor<R> {

    R visitProgram(Program program);

    R visitBinaryOp(BinaryOp binaryOp);

    R visitBlock(Block block);

    R visitPrimary(Primary primary);

    R visitIfStmt(IfStmt ifStmt);

    R visitLambdaExpr(LambdaExpr lambdaExpr);

    R visitReturn(Return ret);

    R visitUnaryOp(UnaryOp unaryOp);

    R visitVarDeclStmt(VarDeclStmt varDeclStmt);

    R visitPrintlnStmt(PrintlnStmt printlnStmt);
}
