package com.verifier.Syntax;

// Assignment Statement
public class Assignment extends Statement {
    private final String variable;
    private final Expression expression;

    public Assignment(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public String getVariable() {
        return variable;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return variable + " := " + expression.toString();
    }
}
