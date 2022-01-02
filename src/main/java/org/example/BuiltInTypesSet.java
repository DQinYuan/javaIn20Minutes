package org.example;

public class BuiltInTypesSet {

    public static final String INT = "int";

    public static final String LONG = "long";

    public static final String FLOAT = "float";

    public static final String DOUBLE = "double";

    public static final String STRING = "String";

    public static final String FUNCTION = "Function";

    public static boolean isBuiltInType(String lexeme) {
        switch (lexeme) {
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case STRING:
            case FUNCTION:
                return true;
            default:
                return false;
        }
    }
}
