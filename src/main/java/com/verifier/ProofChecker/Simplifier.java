package com.verifier.ProofChecker;

import java.util.*;
import java.util.stream.Collectors;

public class Simplifier {

    // Simplifies a logical condition
    public static String simplify(String condition) {
        if (condition == null || condition.trim().isEmpty()) {
            return "";
        }

        // Step 1: Normalize the condition (remove unnecessary spaces and brackets)
        condition = normalize(condition);

        // Step 2: Simplify trivial expressions (true/false removal)
        condition = simplifyTrivial(condition);

        // Step 3: Remove duplicates in AND/OR clauses
        condition = removeDuplicates(condition);

        // Step 4: Optimize nested expressions
        condition = simplifyNested(condition);

        return condition.trim();
    }

    // Normalizes the condition string by removing unnecessary spaces
    private static String normalize(String condition) {
        return condition.replaceAll("\\s+", " ").trim();
    }

    // Simplifies trivial conditions like "true && x > 0" or "false || y < 10"
    private static String simplifyTrivial(String condition) {
        condition = condition.replaceAll("\\btrue &&", "").replaceAll("&& true\\b", "");
        condition = condition.replaceAll("\\bfalse \\|\\|", "").replaceAll("\\|\\| false\\b", "");
        return condition;
    }

    // Removes duplicate conditions from AND/OR clauses
    private static String removeDuplicates(String condition) {
        if (condition.contains("&&")) {
            Set<String> uniqueClauses = new LinkedHashSet<>(Arrays.asList(condition.split("&&")));
            return String.join(" && ", uniqueClauses);
        } else if (condition.contains("||")) {
            Set<String> uniqueClauses = new LinkedHashSet<>(Arrays.asList(condition.split("\\|\\|")));
            return String.join(" || ", uniqueClauses);
        }
        return condition;
    }

    // Simplifies nested expressions like "((x > 0 && y < 10))" to "x > 0 && y < 10"
    private static String simplifyNested(String condition) {
        while (condition.startsWith("(") && condition.endsWith(")")) {
            condition = condition.substring(1, condition.length() - 1).trim();
        }
        return condition;
    }
}

