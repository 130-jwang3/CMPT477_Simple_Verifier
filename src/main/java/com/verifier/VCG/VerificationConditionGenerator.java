package com.verifier.VCG;

import com.verifier.HoareLogic.AssignmentRule;
import com.verifier.HoareLogic.CompositionRule;
import com.verifier.HoareLogic.ConditionalRule;
import com.verifier.Syntax.*;

import java.util.ArrayList;
import java.util.List;

public class VerificationConditionGenerator {

    public List<VerificationCondition> generateVC(Statement body, List<Condition> preconditions, List<Condition> postconditions) {
        List<VerificationCondition> vcs = new ArrayList<>();

        String pre = preconditions.stream()
                .map(Condition::toString)
                .reduce((a, b) -> a + " && " + b)
                .orElse(""); // Handle empty list

        String post = postconditions.stream()
                .map(Condition::toString)
                .reduce((a, b) -> a + " && " + b)
                .orElse(""); // Handle empty list

        if (body instanceof Assignment) {
            vcs.addAll(new AssignmentRule().apply(body, pre, post));
        } else if (body instanceof Sequence) {
            vcs.addAll(new CompositionRule().apply(body, pre, post));
        } else if (body instanceof Conditional) {
            vcs.addAll(new ConditionalRule().apply(body, pre, post));
        } else {
            throw new IllegalArgumentException("Unsupported statement type.");
        }

        return vcs;
    }
}
