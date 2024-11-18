grammar Grammar;

@header {
package com.verifier.parser.generated;
}

program: statementList EOF;

statementList
    : statement (SEMICOLON statement)*
    ;

statement
    : assignment
    | conditionalStatement
    ;

assignment
    : IDENTIFIER ':=' expression
    ;

conditionalStatement
    : IF condition THEN statement (ELSE statement)?
    ;

expression
    : term ((PLUS | MINUS) term)*
    ;

term
    : factor ((MULT | DIV | MOD) factor)*
    ;

factor
    : NUMBER
    | IDENTIFIER
    | '(' expression ')'
    ;

condition
    : expression relOp expression
    ;

relOp
    : '=='
    | '!='
    | '<'
    | '<='
    | '>'
    | '>='
    ;

// Tokens
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
MOD: '%';
SEMICOLON: ';';
IF: 'if';
THEN: 'then';
ELSE: 'else';

IDENTIFIER: [a-zA-Z][a-zA-Z0-9]*;
NUMBER: [0-9]+;
WS: [ \t\r\n]+ -> skip;