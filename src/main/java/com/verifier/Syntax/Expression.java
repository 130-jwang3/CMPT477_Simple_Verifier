package com.verifier.Syntax;

public class Expression extends ASTNode {
    private final String leftOperand;
    private final String operator;  // Arithmetic operator: "+", "-", "*", etc.
    private final String rightOperand;

    public Expression(String leftOperand, String operator, String rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public String getOperator() {
        return operator;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
        if (operator == null) {
            return leftOperand; // Single term
        }
        return leftOperand + " " + operator + " " + rightOperand;
    }
}
