package org.example;

import java.util.HashSet;
import java.util.Set;

public class KeyWordsSet {

    public static final String IF = "if";

    public static final String ELSE = "else";

    public static final String RETURN = "return";

    public static final String NULL = "null";

    public static final String NEW = "new";

    public static final String PRINTLN = "println";

    public static boolean isKeyWord(String word) {
        switch (word) {
            case IF:
            case ELSE:
            case RETURN:
            case NULL:
            case NEW:
            case PRINTLN:
                return true;
            default:
                return false;
        }
    }

}
