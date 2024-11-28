package com.verifier.HoareLogic;

public class ConditionUtils {

    // TODO: Implement a method to strengthen conditions.
    // Strengthens a condition by combining two conditions with "AND" logic.
    // Example: "x > 0" and "y == 5" → "x > 0 && y == 5"
    public static String strengthen(String condition1, String condition2) {
        // TODO: Combine the two conditions logically with "&&".
        if (condition1 == null || condition1.trim().isEmpty()) {
            return condition2;
        }
        if (condition2 == null || condition2.trim().isEmpty()) {
            return condition1;
        }
        return "(" + condition1 + ") && (" + condition2 + ")";
    }

    // TODO: Implement a method to weaken conditions.
    // Weakens a condition by combining two conditions with "OR" logic.
    // Example: "x > 0" or "y == 5" → "x > 0 || y == 5"
    public static String weaken(String condition1, String condition2) {
        // TODO: Combine the two conditions logically with "||".
        if (condition == null || condition.trim().isEmpty()) {
            return "true"; // Negating an empty condition is effectively "true".
        }
        return "!(" + condition + ")";
    }

    // TODO: Implement a method to negate a condition.
    // Negates a condition by inverting its logic.
    // Example: "x > 0" → "x <= 0", "x == 5" → "x != 5", "(x > 0 && y < 5)" → "!(x > 0 && y < 5)"
    public static String negate(String condition) {
        // TODO: Implement negation logic for simple and compound conditions.
        if (condition == null || condition.trim().isEmpty()) {
            return "true"; // Negating an empty condition is effectively "true".
        }
        return "!(" + condition + ")";
    }

    // TODO: Implement a method to simplify conditions.
    // Simplifies a condition by removing trivial or tautological parts.
    // Example: Remove "true && ..." or "false || ...".
    public static String simplify(String condition) {
        // TODO: Parse the condition and remove redundant parts.
        if (condition == null || condition.trim().isEmpty()) {
            return "";
        }

        // Simplify trivial conditions
        condition = condition.replaceAll("\\btrue &&", "")
                             .replaceAll("&& true\\b", "")
                             .replaceAll("\\bfalse \\|\\|", "")
                             .replaceAll("\\|\\| false\\b", "");

        // Trim redundant parentheses
        if (condition.startsWith("(") && condition.endsWith(")")) {
            condition = condition.substring(1, condition.length() - 1);
        }

        return condition.trim();
    }

    public static String substitute(String condition, String variable, String expression) {
        if (condition == null || condition.trim().isEmpty()) {
            return "";
        }
        if (variable == null || variable.trim().isEmpty()) {
            return condition;
        }
        if (expression == null) {
            expression = "";
        }
        return condition.replace(variable, "(" + expression + ")");
    }
}
