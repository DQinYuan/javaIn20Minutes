package org.example;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CodeExecutorTest {

    @Test
    public void expressionTest() throws IOException, URISyntaxException {
        // 1+2*(4+5)
        assertEquals(19, executeFile("0_expression"));
    }

    @Test
    public void stringEscapeTest() throws IOException, URISyntaxException {
        // "a\"bbc\nddd"
        executeFile("1_string_escape");
    }

    @Test
    public void variableTest() throws IOException, URISyntaxException {
        Object res = executeFile("2_variable");
        assertEquals(21, res);
    }

    @Test
    public void ifElseTest() throws IOException, URISyntaxException {
        Object res = executeFile("3_if_else");
        assertEquals(19, res);
    }

    @Test
    public void closureTest() throws IOException, URISyntaxException {
        Object res = executeFile("4_closure");
        assertEquals(3, res);
    }

    /**
     * 值传递测试
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void passByValueTest() throws IOException, URISyntaxException {
        executeFile("5_pass_by_value");
    }

    @Test
    public void closureBugTest() throws IOException, URISyntaxException {
        executeFile("6_closure_bug");
    }

    private Object executeFile(String fileName) throws IOException, URISyntaxException {
        String script = getContentFromClassPath(fileName);
        return CodeExecutor.execute(script);
    }

    private String getContentFromClassPath(String fileName) throws URISyntaxException, IOException {
        return new String(Files.readAllBytes(Paths.get(getClass().
                getResource("/" + fileName).toURI())));
    }
}