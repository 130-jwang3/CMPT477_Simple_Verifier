package com.verifier.HoareLogic;

import com.verifier.Syntax.Statement;
import com.verifier.Syntax.Assignment;
import com.verifier.VCG.VerificationCondition;
import com.verifier.HoareLogic.ConditionUtils;

import java.util.Collection;
import java.util.Collections;

public class AssignmentRule {
    public Collection<? extends VerificationCondition> apply(Statement statement, String pre, String post) {
        Assignment assignment = (Assignment) statement;
        String variable = assignment.getVariable();
        String expression = assignment.getExpression().toString();
        String derivedPrecondition = ConditionUtils.substitute(post, variable, expression);
        derivedPrecondition = ConditionUtils.simplify(derivedPrecondition); // Simplify derivedPrecondition

        VerificationCondition vc = new VerificationCondition(
                derivedPrecondition,           // Derived precondition
                statement.toString(),          // The assignment statement as a string
                post                           // The original postcondition
        );

        return Collections.singleton(vc);
    }
}
