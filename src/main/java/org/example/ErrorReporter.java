package org.example;

import java.text.MessageFormat;

public class ErrorReporter {

    private static final String ERR_TEMPLATE = "Line: {0}, Col: {1}, {2}";

    public static String report(int line, int col, String reason) {
        return MessageFormat.format(ERR_TEMPLATE, line, col, reason);
    }

}
