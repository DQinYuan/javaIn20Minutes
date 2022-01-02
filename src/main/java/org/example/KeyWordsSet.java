package org.example;

import java.util.HashSet;
import java.util.Set;

public class KeyWordsSet {

    public static final String IF = "if";

    public static final String ELSE = "else";

    public static final String RETURN = "return";

    public static final String NULL = "null";

    public static final String NEW = "new";

    // 内置的基本数据类型
    public static final String BYTE = "byte";

    public static final String SHORT = "short";

    public static final String INT = "int";

    public static final String LONG = "long";

    public static final String FLOAT = "float";

    public static final String DOUBLE = "double";

    public static final String CHAR = "char";

    public static final String BOOL = "boolean";

    public static final String PRINTLN = "println";

    public static boolean isKeyWord(String word) {
        switch (word) {
            case IF:
            case ELSE:
            case RETURN:
            case NULL:
            case NEW:
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case CHAR:
            case BOOL:
            case PRINTLN:
                return true;
            default:
                return false;
        }
    }

}
