package com.verifier.HoareLogic;

import com.verifier.Syntax.Assignment;
import com.verifier.Syntax.Conditional;
import com.verifier.Syntax.Sequence;
import com.verifier.Syntax.Statement;
import com.verifier.VCG.VerificationCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConditionalRule {

    public Collection<? extends VerificationCondition> apply(Statement statement, String pre, String post) {
        if (!(statement instanceof Conditional)) {
            throw new IllegalArgumentException("Statement must be of type Conditional.");
        }

        Conditional conditional = (Conditional) statement;
        String condition = conditional.getCondition().toString(); // Convert condition to String
        Statement thenBranch = conditional.getThenBranch();
        Statement elseBranch = conditional.getElseBranch();

        List<VerificationCondition> verifications = new ArrayList<>();

        String thenPre = ConditionUtils.strengthen(pre, condition);
        Collection<? extends VerificationCondition> thenVCs = processBranch(thenBranch, thenPre, post);
        verifications.addAll(thenVCs);

        if (elseBranch != null) {
            String negatedCondition = ConditionUtils.negate(condition);
            String elsePre = ConditionUtils.strengthen(pre, negatedCondition);
            Collection<? extends VerificationCondition> elseVCs = processBranch(elseBranch, elsePre, post);
            verifications.addAll(elseVCs);
        }

        return verifications;
    }

    private Collection<? extends VerificationCondition> processBranch(Statement branch, String pre, String post) {
        if (branch instanceof Assignment) {
            AssignmentRule assignmentRule = new AssignmentRule();
            return assignmentRule.apply(branch, pre, post);
        } else if (branch instanceof Conditional) {
            ConditionalRule conditionalRule = new ConditionalRule();
            return conditionalRule.apply(branch, pre, post);
        } else if (branch instanceof Sequence) {
            CompositionRule compositionRule = new CompositionRule();
            return compositionRule.apply(branch, pre, post);
        } else {
            throw new UnsupportedOperationException(
                "Unsupported statement type in branch: " + branch.getClass().getSimpleName()
            );
        }
    }
}