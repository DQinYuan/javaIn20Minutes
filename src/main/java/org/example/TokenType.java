package org.example;

public enum TokenType {
    // identifier, 变量或者函数名
    ID,
    // 中缀操作符
    // +
    ADD,
    // -
    SUB,
    // *
    MUL,
    // /
    DIV,
    // =
    ASSIGN,
    // ==
    EQUAL,
    // !=
    NOTEQUAL,
    // <
    LT,
    // >
    GT,
    // -> 用于 lambda 表达式
    ARROW,

    // 一元操作符, 先只支持取非
    // !
    BANG,

    // 分隔符
    // (
    LPAREN,
    // )
    RPAREN,
    // {
    LBRACE,
    // }
    RBRACE,
    // ;
    SEMI,
    // ,
    COMMA,
    // .
    DOT,

    // 数字
    NUMBER,
    // 字符串
    STRING,
    // 关键字: if, else, assert
    KEY_WORD,
    // 支持的类型: byte,short,int,long
    // float, double
    // boolean,char,String
    TYPE
}
