package com.verifier.HoareLogic;

import com.verifier.Syntax.Statement;
import com.verifier.Syntax.Sequence;
import com.verifier.Syntax.Assignment;
import com.verifier.Syntax.Conditional;
import com.verifier.VCG.VerificationCondition;

import java.util.List;
import java.util.ArrayList;

import java.util.Collection;

public class CompositionRule {
    public Collection<? extends VerificationCondition> apply(Statement statement, String pre, String post) {
        if (!(statement instanceof Sequence)) {
            throw new IllegalArgumentException("Statement must be of type Sequence.");
        }

        Sequence sequence = (Sequence) statement;
        List<Statement> statements = sequence.getStatements();
        List<VerificationCondition> verificationConditions = new ArrayList<>();
        String currentPost = post;

        for (int i = statements.size() - 1; i >= 0; i--) {
            Statement currentStatement = statements.get(i);

            if (currentStatement instanceof Assignment) {
                AssignmentRule assignmentRule = new AssignmentRule();
                verificationConditions.addAll(
                        assignmentRule.apply(currentStatement, "", currentPost)
                );
            } else if (currentStatement instanceof Conditional) {
                ConditionalRule conditionalRule = new ConditionalRule();
                verificationConditions.addAll(
                        conditionalRule.apply(currentStatement, "", currentPost)
                );
            } else {
                throw new UnsupportedOperationException(
                        "Unsupported statement type: " + currentStatement.getClass().getSimpleName()
                );
            }
            currentPost = ConditionUtils.simplify(verificationConditions.get(verificationConditions.size() - 1).getPrecondition());
        }

        return verificationConditions;
    }
}
