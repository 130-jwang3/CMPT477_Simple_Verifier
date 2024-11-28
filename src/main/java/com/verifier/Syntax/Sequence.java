package com.verifier.Syntax;

import java.util.List;

// Sequence of Statements
public class Sequence extends Statement {
    private final List<Statement> statements;

    public Sequence(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return String.join("; ", statements.stream().map(Statement::toString).toList());
    }
}
