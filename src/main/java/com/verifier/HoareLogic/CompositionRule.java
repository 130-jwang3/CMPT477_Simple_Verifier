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
        String currentPost = post; // Start with the global postcondition

        // Process statements in reverse order
        for (int i = statements.size() - 1; i >= 0; i--) {
            Statement currentStatement = statements.get(i);

            Collection<? extends VerificationCondition> currentVCs;
            if (currentStatement instanceof Assignment) {
                AssignmentRule assignmentRule = new AssignmentRule();
                currentVCs = assignmentRule.apply(currentStatement, pre, currentPost);
            } else if (currentStatement instanceof Conditional) {
                ConditionalRule conditionalRule = new ConditionalRule();
                currentVCs = conditionalRule.apply(currentStatement, pre, currentPost);
            } else if (currentStatement instanceof Sequence) {
                CompositionRule compositionRule = new CompositionRule();
                currentVCs = compositionRule.apply(currentStatement, pre, currentPost);
            } else {
                throw new UnsupportedOperationException(
                        "Unsupported statement type: " + currentStatement.getClass().getSimpleName()
                );
            }

            VerificationCondition lastVC = null;
            for (VerificationCondition vc : currentVCs) {
                String actualPre = (i == 0) ? pre : vc.getPrecondition(); // Set actual pre for the first statement
                verificationConditions.add(new VerificationCondition(
                        vc.getPrecondition(),       // Derived precondition
                        vc.getStatement(),          // Statement itself
                        currentPost,                // Postcondition for this statement
                        actualPre                   // Set actual precondition
                ));
                lastVC = vc;
            }

            // Update currentPost to the derived precondition of the last VC
            if (lastVC != null) {
                currentPost = lastVC.getPrecondition();
            }
        }

        return verificationConditions;
    }
}
