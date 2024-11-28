package com.verifier.Syntax;

public class Condition extends ASTNode {
    private final String leftExpression;
    private final String operator;  // Relational operator: "==", ">", etc.
    private final String rightExpression;

    public Condition(String leftExpression, String operator, String rightExpression) {
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    public String getLeftExpression() {
        return leftExpression;
    }

    public String getOperator() {
        return operator;
    }

    public String getRightExpression() {
        return rightExpression;
    }

    @Override
    public String toString() {
        return leftExpression + " " + operator + " " + rightExpression;
    }
}
