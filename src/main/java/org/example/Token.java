package org.example;

public class Token {
    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    /**
     * token 结束位置 + 1, 从 0 开始
     */
    private final int pos;
    /**
     * 从 1 开始
     */
    private final int line;
    /**
     * 从 1 开始
     * token 结束的列位置 + 1
     */
    private final int col;

    Token(TokenType type, String lexeme, int pos, int line, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = null;
        this.pos = pos;
        this.line = line;
        this.col = col;
    }

    Token(TokenType type, String lexeme, Object literal, int pos, int line, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.pos = pos;
        this.line = line;
        this.col = col;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Object getLiteral() {
        return literal;
    }

    public int getPos() {
        return pos;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
        return pos + ":Line " + line + ":Col " + col + " " + type + " " + lexeme + " " +
                (literal == null? "": literal);
    }
}
