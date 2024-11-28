package com.verifier.Syntax;

import java.util.List;

public class Parser extends ASTNode {
    private final List<Condition> preconditions;
    private final Statement body; // Body of the program (could be a Sequence node if multiple statements)
    private final List<Condition> postconditions;

    public Parser(List<Condition> preconditions, Statement body, List<Condition> postconditions) {
        this.preconditions = preconditions;
        this.body = body;
        this.postconditions = postconditions;
    }

    public List<Condition> getPreconditions() {
        return preconditions;
    }

    public Statement getBody() {
        return body;
    }

    public List<Condition> getPostconditions() {
        return postconditions;
    }

    @Override
    public String toString() {
        return "pre: " + preconditions + "\n" +
                body.toString() + "\n" +
                "post: " + postconditions;
    }
}