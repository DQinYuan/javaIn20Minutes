package org.example;

public class SplitCharsSet {

    public static final char LPAREN = '(';

    public static final char RPAREN = ')';

    public static final char LBRACE = '{';

    public static final char RBRACE = '}';

    public static final char SEMI = ';';

    public static final char COMMA = ',';

    public static final char DOT = '.';

    public static final char ADD = '+';

    public static final char MINUS = '-';

    public static final char MULTI = '*';

    public static final char DIVIDE = '/';

    public static final char EQ = '=';

    public static final char LE = '<';

    public static final char GE = '>';

    public static final char DQUOTE = '"';

    public static boolean isSplitChar(char c) {
        switch (c) {
            case LPAREN:
            case RPAREN:
            case LBRACE:
            case RBRACE:
            case SEMI:
            case COMMA:
            case DOT:
            case ADD:
            case MINUS:
            case MULTI:
            case EQ:
            case LE:
            case GE:
            case DIVIDE:
            case DQUOTE:
                return true;
            default:
                return false;
        }
    }

}
