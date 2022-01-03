package org.example;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Deque;
import java.util.LinkedList;

public class Scanner {

    private final String script;

    // 回溯用的队列
    private final Deque<Token> backQueue = new LinkedList<>();

    private int currentLine = 1;

    private int currentCol = 1;

    private int pos;

    public Scanner(String script) {
        this.script = script;
    }

    public Token next() {
        if (!backQueue.isEmpty()) {
            return backQueue.poll();
        }
        while (!isEnd()) {
            char curChar = advance();
            if (isInVisible(curChar)) {
                // 跳过不可见字符
                continue;
            }
            switch (curChar) {
                // 单字符 token
                case '(':
                    return newToken(TokenType.LPAREN, "(");
                case ')':
                    return newToken(TokenType.RPAREN, ")");
                case '{':
                    return newToken(TokenType.LBRACE, "{");
                case '}':
                    return newToken(TokenType.RBRACE, "}");
                case '.':
                    return newToken(TokenType.DOT, ".");
                case '<':
                    return newToken(TokenType.LT, "<");
                case '>':
                    return newToken(TokenType.GT, ">");
                case ';':
                    return newToken(TokenType.SEMI, ";");
                case ',':
                    return newToken(TokenType.COMMA, ",");
                // + - * /
                case '+':
                    return newToken(TokenType.ADD, "+");
                case '-':
                    return matchAndAdvance('>')?
                                    newToken(TokenType.ARROW, "->"):
                                    newToken(TokenType.ADD, "-");
                case '*':
                    return newToken(TokenType.MUL, "*");
                case '/':
                    if (matchAndAdvance('/')) {
                        // 单行注释, 直接忽略
                        advanceLineComment();
                        continue;
                    } else {
                        return newToken(TokenType.DIV, "/");
                    }
                // == !=
                case '=':
                    return matchAndAdvance('=')? newToken(TokenType.EQUAL, "=="):
                            newToken(TokenType.ASSIGN, "=");
                case '!':
                    return matchAndAdvance('=')? newToken(TokenType.NOTEQUAL, "!="):
                            newToken(TokenType.BANG, "!");
                case '"':
                    // 字符串
                    return string();
            }

            if (isDigit(curChar)) {
                return number();
            }
            // 关键字或者 id
            return identifierOrKeywordOrType();
        }

        return null;
    }

    private void advanceLineComment() {
        while (!isEnd() && advance() != '\n');
    }

    public void giveBack(Token token) {
        backQueue.add(token);
    }

    private Token identifierOrKeywordOrType() {
        int startPos = pos;
        while (!isEnd()) {
            char cur = peek();
            if (isInVisible(cur) || SplitCharsSet.isSplitChar(cur)) {
                return newIdOrKeywordOrTypeToken(script.substring(startPos - 1, pos));
            }
            advance();
        }
        return newIdOrKeywordOrTypeToken(script.substring(startPos - 1, pos));
    }

    private Token newIdOrKeywordOrTypeToken(String tokenCharSeq) {
        return newTokenWithLiteral(
                BuiltInTypesSet.isBuiltInType(tokenCharSeq)? TokenType.TYPE:
                        KeyWordsSet.isKeyWord(tokenCharSeq)?
                                TokenType.KEY_WORD: TokenType.ID,
                        tokenCharSeq, tokenCharSeq);
    }

    private Token string() {
        StringBuilder lexemeBuilder = new StringBuilder().append('"');
        StringBuilder literalBuilder = new StringBuilder();

        final byte init = 0;
        final byte escape = 1;
        byte state = 0;

        while (!isEnd()) {
            char cur = advance();
            switch (state) {
                case init:
                    lexemeBuilder.append(cur);
                    if (cur == '"') {
                        return newTokenWithLiteral(TokenType.STRING,
                                lexemeBuilder.toString(),
                                literalBuilder.toString());
                    } else if (cur == '\\') {
                        // escape
                        state = escape;
                    } else if (cur == '\n') {
                        // not support line break
                        throw new RuntimeException(ErrorReporter.report(currentLine, currentCol,
                                "string line break"));
                    } else {
                        literalBuilder.append(cur);
                    }
                    continue;
                case escape:
                    lexemeBuilder.append(cur);
                    state = init;
                    switch (cur) {
                        case 'n':
                            literalBuilder.append('\n');
                            break;
                        case '\\':
                            literalBuilder.append('\\');
                            break;
                        case '"':
                            literalBuilder.append('"');
                            break;
                        default:
                            throw new RuntimeException(ErrorReporter
                                    .report(currentLine, currentCol, "invalid escape"));
                    }
            }
        }
        throw new RuntimeException(ErrorReporter.report(currentLine, currentCol, "string not close"));
    }

    private Token number() {
        final byte init = 0;
        final byte decimalPart = 1;
        final byte numTypePart = 2;
        final byte end = 3;

        byte state = init;
        StringBuilder numberBuilder = new StringBuilder().append(previous());
        char numType = 0;
        while (!isEnd() && state != end) {
            char cur = peek();
            switch (state) {
                case init:
                    if (isDigit(cur)) {
                        numberBuilder.append(cur);
                        advance();
                    } else if (cur == '.') {
                        numberBuilder.append('.');
                        advance();
                        numType = 'd';
                        state = decimalPart;
                    } else if (isInVisible(cur) || SplitCharsSet.isSplitChar(cur)) {
                        state = end;
                    } else if (isNumberTypeFlag(cur)) {
                        numberBuilder.append(cur);
                        advance();
                        numType = cur;
                        state = numTypePart;
                    } else {
                        numberBuilder.append(cur);
                        advance();
                        throw new RuntimeException(ErrorReporter.report(currentLine, currentCol,
                                "invalid number"));
                    }
                    continue;
                case decimalPart:
                    if (isDigit(cur)) {
                        numberBuilder.append(cur);
                        advance();
                    } else if (isInVisible(cur) || SplitCharsSet.isSplitChar(cur)) {
                        state = end;
                    } else if (isNumberTypeFlag(cur)) {
                        numberBuilder.append(cur);
                        advance();
                        numType = cur;
                        state = numTypePart;
                    } else {
                        numberBuilder.append(cur);
                        advance();
                        throw new RuntimeException(ErrorReporter.report(currentLine, currentCol,
                                "invalid number"));
                    }
                    continue;
                case numTypePart:
                    if (isInVisible(cur)) {
                        state = end;
                    } else {
                        numberBuilder.append(cur);
                        advance();
                        throw new RuntimeException(ErrorReporter.report(currentLine, currentCol,
                                "invalid number"));
                    }
                    continue;
            }
        }

        String lexeme = numberBuilder.toString();
        Number literal;
        if (numType == 0) {
            literal = Integer.parseInt(lexeme);
        } else {
            switch (numType) {
                case 'f':
                case 'F':
                    literal = Float.parseFloat(lexeme);
                    break;
                case 'd':
                case 'D':
                    literal = Double.parseDouble(lexeme);
                    break;
                case 'l':
                case 'L':
                    literal = Long.parseLong(lexeme);
                    break;
                default:
                    throw new RuntimeException(ErrorReporter.report(currentLine,
                            currentCol, "invalid number"));
            }
        }

        return newTokenWithLiteral(TokenType.NUMBER, lexeme, literal);
    }

    private boolean isNumberTypeFlag(char c) {
        return c == 'd' || c == 'D' ||
                c == 'f' || c == 'F' ||
                c == 'l' || c == 'L';
    }

    private char peek() {
        return script.charAt(pos);
    }

    private boolean isEnd() {
        return pos > script.length() - 1;
    }

    private boolean isInVisible(char c) {
        return c <= ' ';
    }

    private Token newToken(TokenType tokenType, String lexeme) {
        return new Token(tokenType, lexeme, pos, currentLine, currentCol);
    }

    private Token newTokenWithLiteral(TokenType tokenType, String lexeme, Object literal) {
        return new Token(tokenType, lexeme, literal, pos, currentLine, currentCol);
    }

    private char previous() {
        return script.charAt(pos-1);
    }

    private char advance() {
        char curChar = script.charAt(pos++);
        if (curChar == '\n') {
            currentLine++;
            currentCol = 1;
        } else {
            currentCol++;
        }
        return curChar;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean matchAndAdvance(char expected) {
        if (isEnd() || script.charAt(pos) != expected) {
            return false;
        }
        advance();
        return true;
    }
}
