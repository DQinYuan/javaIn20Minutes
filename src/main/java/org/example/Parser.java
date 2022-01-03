package org.example;

import org.example.tree.LambdaExpr;
import org.example.tree.PrintlnStmt;
import org.example.tree.VarDecl;
import org.example.tree.VarDeclStmt;
import org.example.tree.BinaryOp;
import org.example.tree.Block;
import org.example.tree.Expr;
import org.example.tree.MethodCallPrimary;
import org.example.tree.IdPrimary;
import org.example.tree.IfStmt;
import org.example.tree.LiteralPrimary;
import org.example.tree.Primary;
import org.example.tree.Program;
import org.example.tree.Return;
import org.example.tree.Stmt;
import org.example.tree.UnaryOp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Parser {

    private final Scanner scanner;

    private Token pre;

    private Token cur;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        cur = scanner.next();
    }

    public Program parse() {
        List<Stmt> stmtList = new ArrayList<>();
        while (!isEnd()) {
            stmtList.add(statement());
        }

        return new Program(stmtList);
    }

    private Stmt statement() {
        if (matchAndAdvance(TokenType.LBRACE)) {
            // block
            return block();
        } else if (matchKeyWordAndAdvance(KeyWordsSet.IF)) {
            return ifStmt();
        } else if (matchKeyWordAndAdvance(KeyWordsSet.RETURN)) {
            return returnStmt();
        } else if (matchKeyWordAndAdvance(KeyWordsSet.PRINTLN)) {
            return printlnStmt();
        } else if (matchAndAdvance(TokenType.TYPE)) {
            return varDeclStmt();
        } else {
            Expr expr = expr();
            advanceOrReportError(TokenType.SEMI, "statement must end with ';'");
            return expr;
        }
    }

    private Stmt returnStmt() {
        Token keyToken = pre;
        if (matchAndAdvance(TokenType.SEMI)) {
            return new Return(keyToken, null);
        }

        Expr expr = expr();
        advanceOrReportError(TokenType.SEMI, "return statement must end with ';'");
        return new Return(keyToken, expr);
    }

    private PrintlnStmt printlnStmt() {
        Token keyToken = pre;

        Expr expr = expr();
        advanceOrReportError(TokenType.SEMI, "return statement must end with ';'");
        return new PrintlnStmt(keyToken, expr);
    }

    private Stmt varDeclStmt() {
        Token typeToken = this.pre;
        IdPrimary target = idOrReportError("invalid assign variable name");
        if (matchAndAdvance(TokenType.ASSIGN)) {
            Expr init = expr();
            advanceOrReportError(TokenType.SEMI, "statement must end with ';'");
            return new VarDeclStmt(typeToken, new VarDecl(typeToken, target), init);
        } else {
            advanceOrReportError(TokenType.SEMI, "statement must end with ';'");
            return new VarDeclStmt(typeToken, new VarDecl(typeToken, target), null);
        }
    }

    private IfStmt ifStmt() {
        Token keyToken = pre;
        advanceOrReportError(TokenType.LPAREN, "expect '(' in if statement");
        Expr condition = expr();
        advanceOrReportError(TokenType.RPAREN, "expect ')' in if statement");

        Stmt thenBranch = statement();

        if (matchKeyWordAndAdvance(KeyWordsSet.ELSE)) {
            Stmt elseBranch = statement();
            return new IfStmt(keyToken, condition, thenBranch, elseBranch);
        } else {
            return new IfStmt(keyToken, condition, thenBranch, null);
        }
    }

    private IdPrimary idOrReportError(String reason) {
        if (cur.getType() != TokenType.ID) {
            throw new RuntimeException(ErrorReporter.report(cur.getLine(), cur.getCol(), reason));
        }
        IdPrimary id = new IdPrimary(cur);
        advance();
        return id;
    }

    private void advanceOrReportError(TokenType expectType, String reason) {
        if (isEnd() || !Objects.equals(cur.getType(), expectType)) {
            throw new RuntimeException(ErrorReporter.report(pre.getLine(), pre.getCol(), reason));
        }
        advance();
    }

    private boolean matchKeyWordAndAdvance(String keyword) {
        if (cur == null) {
            return false;
        }
        if (Objects.equals(TokenType.KEY_WORD, cur.getType()) && Objects.equals(keyword, cur.getLexeme())) {
            advance();
            return true;
        }
        return false;
    }

    private Block block() {
        Token keyToken = pre;
        List<Stmt> stmtList = new ArrayList<>();
        while (!matchAndAdvance(TokenType.RBRACE) && !isEnd()) {
            stmtList.add(statement());
        }
        return new Block(keyToken, stmtList);
    }

    private boolean matchAndAdvance(TokenType expectType) {
        if (cur == null) {
            return false;
        }
        if (Objects.equals(expectType, cur.getType())) {
            advance();
            return true;
        }
        return false;
    }

    /**
     * @return true 表示文本遍历还没结束
     */
    private boolean advance() {
        pre = cur;
        cur = scanner.next();
        return cur != null;
    }

    private boolean isEnd() {
        return cur == null;
    }

    /*
     * 表达式解析
     */

    private static final Map<TokenType, ExprParseRule> parseRuleMap = new HashMap<>();

    static {
        parseRuleMap.put(TokenType.NUMBER, new ExprParseRule(Parser::numberOrStrPrimary, null,
                ExprParseRule.PREC_GROUP));
        parseRuleMap.put(TokenType.STRING, new ExprParseRule(Parser::numberOrStrPrimary, null,
                ExprParseRule.PREC_GROUP));
        parseRuleMap.put(TokenType.KEY_WORD, new ExprParseRule(Parser::keyWordPrimary, null,
                ExprParseRule.PREC_GROUP));
        parseRuleMap.put(TokenType.LPAREN, new ExprParseRule(Parser::group, null,
                ExprParseRule.PREC_GROUP));
        parseRuleMap.put(TokenType.ID, new ExprParseRule(Parser::idPrimary, null,
                ExprParseRule.PREC_GROUP));

        ExprParseRule unaryRule = new ExprParseRule(Parser::unary, null,
                ExprParseRule.PREC_UNARY);
        parseRuleMap.put(TokenType.ADD, unaryRule);
        parseRuleMap.put(TokenType.SUB, unaryRule);
        parseRuleMap.put(TokenType.BANG, unaryRule);

        // * /
        ExprParseRule multiBinaryRule = new ExprParseRule(null, Parser::binary,
                ExprParseRule.PREC_MULTI);
        parseRuleMap.put(TokenType.MUL, multiBinaryRule);
        parseRuleMap.put(TokenType.DIV, multiBinaryRule);

        // + -
        ExprParseRule addBinaryRule = new ExprParseRule(null, Parser::binary,
                ExprParseRule.PREC_ADD);
        parseRuleMap.put(TokenType.ADD, addBinaryRule);
        parseRuleMap.put(TokenType.SUB, addBinaryRule);
        parseRuleMap.put(TokenType.GT, addBinaryRule);
        parseRuleMap.put(TokenType.LT, addBinaryRule);

        // 赋值
        parseRuleMap.put(TokenType.ASSIGN, new ExprParseRule(null, Parser::binary,
                ExprParseRule.PREC_ASSIGN));
    }

    private static Expr idPrimary(Parser parser, int currentPrecedence) {
        Primary leftPrimary = new IdPrimary(parser.pre);
        while (parser.matchAndAdvance(TokenType.DOT)) {
            Token dotToken = parser.pre;
            if (parser.matchAndAdvance(TokenType.ID)) {
                Token idToken = parser.pre;
                // 解析参数列表
                if (parser.matchAndAdvance(TokenType.LPAREN)) {
                    leftPrimary = new MethodCallPrimary(dotToken, leftPrimary, parser.arguments(), idToken);
                } else {
                    throw new RuntimeException();
                }
            } else {
                throw new RuntimeException(ErrorReporter.report(dotToken.getLine(), dotToken.getCol(),
                        "invalid method call"));
            }
        }

        return leftPrimary;
    }

    private List<Expr> arguments() {
        List<Expr> arguments = new ArrayList<>();
        while (!matchAndAdvance(TokenType.RPAREN)) {
            if (isEnd()) {
                throw new RuntimeException(ErrorReporter.report(pre.getLine(), pre.getCol(),
                        "method arguments must close with ')'"));
            }
            if (!arguments.isEmpty()) {
                advanceOrReportError(TokenType.COMMA, "method arguments must be separated with comma");
            }
            arguments.add(expr());
        }
        return arguments;
    }

    private static Expr numberOrStrPrimary(Parser parser, int currentPrecedence) {
        return new LiteralPrimary(parser.pre, parser.pre.getLiteral());
    }

    private static Expr keyWordPrimary(Parser parser, int currentPrecedence) {
        Token pre = parser.pre;
        if (KeyWordsSet.NULL.equals(pre.getLexeme())) {
            return new LiteralPrimary(parser.pre, null);
        }
        throw new RuntimeException(ErrorReporter.report(pre.getLine(), pre.getCol(),
                "invalid expression"));
    }

    private static Expr group(Parser parser, int currentPrecedence) {
        Expr inGroupExpr = parser.expr();
        parser.advanceOrReportError(TokenType.RPAREN, "expression miss ')'");
        return inGroupExpr;
    }

    private Expr expr() {
        // 前看一个字符
        if (!isEnd() && cur.getType() == TokenType.LPAREN) {
            Token next = scanner.next();
            if (next != null && (next.getType() == TokenType.TYPE || next.getType() == TokenType.RPAREN)) {
                // lambda 表达式
                scanner.giveBack(next);
                advance();
                return lambdaExpr();
            } else if (next != null) {
                // 回溯
                scanner.giveBack(next);
            }
        }

        return parsePrecedence(ExprParseRule.PREC_ASSIGN);
    }

    private static Expr unary(Parser parser, int currentPrecedence) {
        Token opToken = parser.pre;
        Expr expr = parser.parsePrecedence(currentPrecedence + 1);
        return new UnaryOp(opToken, opToken, expr);
    }

    private static Expr binary(Parser parser, Expr left, int currentPrecedence) {
        Token opToken = parser.pre;
        return new BinaryOp(opToken, left, opToken, parser.parsePrecedence(currentPrecedence + 1));
    }

    private Expr parsePrecedence(int precedence) {
        if (isEnd()) {
            throw new RuntimeException(ErrorReporter.report(pre.getLine(), pre.getCol(),
                    "invalid expression"));
        }
        ExprParseRule exprParseRule = parseRuleMap.get(cur.getType());
        UnaryRule unaryRule = exprParseRule.getUnaryRule();
        if (unaryRule == null) {
            throw new RuntimeException(ErrorReporter.report(pre.getLine(), pre.getCol(),
                    "invalid expression"));
        }

        advance();
        Expr left = unaryRule.parse(this, exprParseRule.getPrecedence());

        while (gePrecedenceAndAdvance(precedence)) {
            ExprParseRule inExprParseRule = parseRuleMap.get(pre.getType());
            BinaryRule binaryRule = inExprParseRule.getBinaryRule();
            left = binaryRule.parse(this, left, inExprParseRule.getPrecedence());
        }
        return left;
    }

    private LambdaExpr lambdaExpr() {
        List<VarDecl> params = new ArrayList<>();
        while (!matchAndAdvance(TokenType.RPAREN)) {
            if (!params.isEmpty()) {
                advanceOrReportError(TokenType.COMMA, "parameters must be separated with ','");
            }
            if (matchAndAdvance(TokenType.TYPE)) {
                Token type = pre;
                if (matchAndAdvance(TokenType.ID)) {
                    Token id = pre;
                    params.add(new VarDecl(type, new IdPrimary(id)));
                } else {
                    throw new RuntimeException(ErrorReporter.report(cur.getLine(), cur.getCol(),
                            "invalid parameter declare"));
                }
            } else {
                throw new RuntimeException(ErrorReporter.report(cur.getLine(), cur.getCol(),
                        "invalid type"));
            }
        }

        advanceOrReportError(TokenType.ARROW, "expect '->' in lambda");
        Token lambdaKeyToken = pre;

        Block blockBody = null;
        Expr exprBody = null;
        if (matchAndAdvance(TokenType.LBRACE)) {
            blockBody = block();
        } else {
            exprBody = expr();
        }
        return new LambdaExpr(lambdaKeyToken, params, blockBody, exprBody);
    }

    private boolean gePrecedenceAndAdvance(int precedence) {
        if (isEnd()) {
            return false;
        }
        ExprParseRule exprParseRule = parseRuleMap.get(cur.getType());
        if (exprParseRule == null) {
            return false;
        }
        boolean geAdvance = exprParseRule.getPrecedence() >= precedence;
        if (geAdvance) {
            advance();
        }
        return geAdvance;
    }
}
