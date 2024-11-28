grammar Grammar;

@header {
package com.verifier.parser.generated;
}

// Program structure now includes preconditions and postconditions
program: precondition statementList postcondition EOF;

precondition
    : 'pre' ':' conditionList // Precondition starts with 'pre:'
    ;

postcondition
    : 'post' ':' conditionList // Postcondition starts with 'post:'
    ;

conditionList
    : condition (',' condition)* // A list of conditions separated by commas
    ;

statementList
    : statement (SEMICOLON statement)* SEMICOLON? // Multiple statements separated by semicolons
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
