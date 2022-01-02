package org.example;

import org.example.interpreter.AstExecutor;
import org.example.tree.Program;

public class CodeExecutor {

    public static Object execute(String script) {
        Scanner scanner = new Scanner(script);
        Parser parser = new Parser(scanner);
        Program program = parser.parse();
        return program.accept(new AstExecutor()).getValueObj();
    }

}
