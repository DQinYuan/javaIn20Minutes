package org.example;

import org.example.tree.Program;
import org.junit.Test;

public class ParserTest {

    @Test
    public void parseExpressionTest() {
        Parser parser = new Parser(new Scanner("int a = 1+6*3+2*(4+5);"));
        Program program = parser.parse();
        program.accept(new AstPrinter(System.out));
    }

    @Test
    public void parseExpressionTest2() {
        Parser parser = new Parser(new Scanner("a = 1+6*3+2*(4+5);"));
        Program program = parser.parse();
        program.accept(new AstPrinter(System.out));
    }

    @Test
    public void parseTest() {
        Parser parser = new Parser(new Scanner("Function a = (int b) -> b + 1;"));
        Program program = parser.parse();
        program.accept(new AstPrinter(System.out));
    }

}