package com.verifier.ProofChecker;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Simplifier {

    // Simplifies a logical condition
    public static String simplify(String condition) {
        // TODO: Parse the condition and remove redundancies
        String simplified = condition;

        // Example simplifications (extend as needed):
        simplified = simplified.replace("true &&", "").replace("&& true", ""); // Remove "true"
        simplified = simplified.replace("false ||", "").replace("|| false", ""); // Remove "false"
        simplified = removeDuplicates(simplified);

        return simplified;
    }

    // Helper method to remove duplicate conditions
    private static String removeDuplicates(String condition) {
        // TODO: Implement logic to remove duplicate clauses
        // Example (naive approach): Split by "&&" or "||" and deduplicate
        String[] parts = condition.split("&&");
        Set<String> uniqueParts = new LinkedHashSet<>(Arrays.asList(parts));
        return String.join(" && ", uniqueParts);
    }
}
