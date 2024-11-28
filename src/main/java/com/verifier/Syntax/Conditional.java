package com.verifier.Syntax;

// Conditional Statement
public class Conditional extends Statement {
    private final Condition condition;
    private final Statement thenBranch;
    private final Statement elseBranch;

    public Conditional(Condition condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Condition getCondition() {
        return condition;
    }

    public Statement getThenBranch() {
        return thenBranch;
    }

    public Statement getElseBranch() {
        return elseBranch;
    }

    @Override
    public String toString() {
        return "if " + condition.toString() +
                " then " + thenBranch.toString() +
                (elseBranch != null ? " else " + elseBranch.toString() : "");
    }
}
