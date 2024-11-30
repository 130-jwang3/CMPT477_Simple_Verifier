package com.verifier.ProofChecker;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Simplifier {

    // Simplifies a logical condition
    public static String simplify(String condition) {
        if (condition == null || condition.trim().isEmpty()) {
            return "";
        }

        condition = normalize(condition); // Normalize the condition (remove unnecessary spaces)
        condition = recursivelySimplifyNestedExpressions(condition); // Simplify nested expressions
        condition = simplifyExpression(condition); // Simplify arithmetic expressions
        condition = removeExtraParenthesesForValues(condition); // Remove redundant parentheses
        condition = simplifyTrivial(condition); // Simplify trivial expressions (true/false removal)
        condition = removeDuplicates(condition); // Remove duplicate AND/OR clauses
        condition = ensureSingleSpace(condition); // Ensure consistent spacing

        return condition.trim();
    }

    // Normalizes the condition string by removing unnecessary spaces
    private static String normalize(String condition) {
        return condition.replaceAll("\\s+", "").trim();
    }

    // Simplify nested arithmetic expressions recursively
    private static String recursivelySimplifyNestedExpressions(String condition) {
        Pattern pattern = Pattern.compile("\\(([^()]+)\\)"); // Match innermost parentheses
        while (pattern.matcher(condition).find()) {
            Matcher matcher = pattern.matcher(condition);
            StringBuffer simplified = new StringBuffer();

            while (matcher.find()) {
                String innerExpression = matcher.group(1); // Extract innermost expression
                String simplifiedInner = simplifyExpression(innerExpression); // Simplify the inner expression
                matcher.appendReplacement(simplified, simplifiedInner); // Replace original with simplified
            }

            matcher.appendTail(simplified);
            condition = simplified.toString(); // Update the condition for further simplification
        }
        return condition;
    }

    // Simplify numerical expressions
    private static String simplifyExpression(String condition) {
        Pattern pattern = Pattern.compile("([a-zA-Z_][a-zA-Z_0-9]*)\\s*([+\\-*/])\\s*(\\d+)\\s*([<>!=]=?|==)\\s*(\\d+)");
        Matcher matcher = pattern.matcher(condition);

        StringBuffer simplified = new StringBuffer();

        while (matcher.find()) {
            String variable = matcher.group(1); // x or y
            String operator = matcher.group(2); // +, -, *, /
            int constant1 = Integer.parseInt(matcher.group(3)); // e.g., 1
            String comparison = matcher.group(4); // >, >=, <=, etc.
            int constant2 = Integer.parseInt(matcher.group(5)); // e.g., 10

            // Perform the operation and simplify
            int result = 0;
            switch (operator) {
                case "+":
                    result = constant2 - constant1;
                    break;
                case "-":
                    result = constant2 + constant1;
                    break;
                case "*":
                    result = constant2 / constant1; // Assume no rounding issues
                    break;
                case "/":
                    result = constant2 * constant1;
                    break;
            }

            // Replace with simplified form (e.g., "x>9")
            String simplifiedExpression = String.format("%s %s %d", variable, comparison, result);
            matcher.appendReplacement(simplified, simplifiedExpression);
        }

        matcher.appendTail(simplified);
        return simplified.toString();
    }

    private static String ensureSingleSpace(String condition) {
        condition = condition.replaceAll("(?<=[^\\s])([()<>!&|=+\\-*/])", " $1 ");
        condition = condition.replaceAll("([()<>!&|=+\\-*/])(?=[^\\s])", " $1 ");

        return normalize(condition);
    }

    private static String removeExtraParenthesesForValues(String condition) {
        return condition.replaceAll("\\((\\d+)\\)", "$1") // For numeric values like (5)
                        .replaceAll("\\(([a-zA-Z_][a-zA-Z_0-9]*)\\)", "$1"); // For variables like (x)
    }

    private static String simplifyTrivial(String condition) {
        condition = condition.replaceAll("\\btrue &&", "").replaceAll("&& true\\b", "");
        condition = condition.replaceAll("\\bfalse \\|\\|", "").replaceAll("\\|\\| false\\b", "");
        return condition;
    }

    private static String removeDuplicates(String condition) {
        if (condition.contains(" && ")) {
            Set<String> uniqueClauses = new LinkedHashSet<>(Arrays.asList(condition.split(" && ")));
            return String.join(" && ", uniqueClauses);
        } else if (condition.contains(" || ")) {
            Set<String> uniqueClauses = new LinkedHashSet<>(Arrays.asList(condition.split(" \\|\\| ")));
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
