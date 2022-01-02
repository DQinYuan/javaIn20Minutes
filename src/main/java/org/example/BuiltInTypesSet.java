package org.example;

public class BuiltInTypesSet {

    public static final String BYTE = "byte";

    public static final String SHORT = "short";

    public static final String INT = "int";

    public static final String LONG = "long";

    public static final String FLOAT = "float";

    public static final String DOUBLE = "double";

    public static final String BOOLEAN = "boolean";

    public static final String CHAR = "char";

    public static final String STRING = "String";

    public static final String FUNCTION = "Function";

    public static boolean isBuiltInType(String lexeme) {
        switch (lexeme) {
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case BOOLEAN:
            case CHAR:
            case STRING:
            case FUNCTION:
                return true;
            default:
                return false;
        }
    }
}
