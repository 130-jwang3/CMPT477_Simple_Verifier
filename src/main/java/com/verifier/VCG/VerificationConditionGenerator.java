package com.verifier.VCG;

import com.verifier.HoareLogic.AssignmentRule;
import com.verifier.HoareLogic.CompositionRule;
import com.verifier.HoareLogic.ConditionalRule;
import com.verifier.Syntax.*;

import java.util.ArrayList;
import java.util.List;

public class VerificationConditionGenerator {

    // Entry point to generate VCs
    public List<VerificationCondition> generateVC(Statement body, List<Condition> preconditions, List<Condition> postconditions) {
        List<VerificationCondition> vcs = new ArrayList<>();

        // Combine preconditions and postconditions into strings
        String pre = preconditions.stream()
                .map(Condition::toString)
                .reduce((a, b) -> a + " && " + b)
                .orElse(""); // Handle empty list
        String post = postconditions.stream()
                .map(Condition::toString)
                .reduce((a, b) -> a + " && " + b)
                .orElse(""); // Handle empty list

        // Process the program body
        applyHoareRule(body, pre, post, vcs);

        return vcs;
    }

    // Apply the appropriate Hoare Rule based on the statement type
    private void applyHoareRule(Statement statement, String pre, String post, List<VerificationCondition> vcs) {
        if (statement instanceof Assignment) {
            vcs.addAll(new AssignmentRule().apply(statement, pre, post));
        } else if (statement instanceof Sequence) {
            vcs.addAll(new CompositionRule().apply(statement, pre, post));
        } else if (statement instanceof Conditional) {
            vcs.addAll(new ConditionalRule().apply(statement, pre, post));
        } else {
            throw new IllegalArgumentException("Unsupported statement type: " + statement.getClass().getSimpleName());
        }
    }
}

