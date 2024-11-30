package com.verifier.ProofChecker;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Simplifier {

    // Simplifies a logical condition
    public static String simplify(String condition) {
        if (condition == null || condition.trim().isEmpty()) {
            return "";
        }

        condition = normalize(condition); // Normalize the condition (remove unnecessary spaces)
        condition = simplifyTrivial(condition);
        condition = removeDuplicates(condition);
        condition = recursivelyNegationExpressions(condition);
        condition = recursivelySimplifyNestedExpressions(condition); // Simplify nested expression
        condition = simplifyExpression(condition);
        condition = removeExtraParenthesesForValues(condition); // Remove redundant parentheses
        condition = simplifyTrivial(condition); // Simplify trivial expressions (true/false removal)
        condition = removeDuplicates(condition); // Remove duplicate AND/OR clauses
        condition = ensureSingleSpace(condition); // Ensure consistent spacing
        condition = normalize(condition);

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
    private static String recursivelyNegationExpressions(String condition) {
        Pattern pattern = Pattern.compile("\\!\\(([^()]+)\\)"); // Match negation followed by parentheses
        while (pattern.matcher(condition).find()) {
            Matcher matcher = pattern.matcher(condition);
            StringBuffer simplified = new StringBuffer();

            while (matcher.find()) {
                String innerExpression = matcher.group(1); // Extract the expression inside the negated parentheses
                String simplifiedInner = simplifyExpression(innerExpression); // Simplify the inner expression

                // Apply negation handling to the inner expression
                String negationHandedInner = handleNegation(simplifiedInner);

                matcher.appendReplacement(simplified, negationHandedInner); // Replace negation with simplified expression
            }

            matcher.appendTail(simplified);
            condition = simplified.toString(); // Update the condition for further processing
        }
        return condition;
    }


    private static String handleNegation(String expression) {
        // Handle logical negations like !(A && B) -> !A || !B, !(A || B) -> !A && !B
        if (expression.contains("&&")) {
            String[] clauses = expression.split("\\s*&&\\s*");
            return Arrays.stream(clauses)
                    .map(clause -> handleNegation(clause.trim()))
                    .collect(Collectors.joining("||"));
        } else if (expression.contains("||")) {
            String[] clauses = expression.split("\\s*\\|\\|\\s*");
            return Arrays.stream(clauses)
                    .map(clause -> handleNegation(clause.trim()))
                    .collect(Collectors.joining("&&"));
        }

        // Handle single negation like !(x > 5) -> x <= 5 or !(5 < x) -> x >= 5
        Pattern comparisonPattern = Pattern.compile(
            "([a-zA-Z_][a-zA-Z_0-9]*|-?\\d+)\\s*([<>!=]=?|==)\\s*([a-zA-Z_][a-zA-Z_0-9]*|-?\\d+)"
        );
        Matcher matcher = comparisonPattern.matcher(expression);
        if (matcher.matches()) {
            String left = matcher.group(1); // Left operand (variable or constant)
            String operator = matcher.group(2); // Operator
            String right = matcher.group(3); // Right operand (variable or constant)

            // Check if variable is on the right side
            if (left.matches("\\d+") && right.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
                // Swap operands and adjust operator
                String temp = left;
                left = right;
                right = temp;
                operator = invertOperator(operator); // Invert the operator
            }

            // Invert the comparison operator
            switch (operator) {
                case ">": return String.format("%s <= %s", left, right);
                case ">=": return String.format("%s < %s", left, right);
                case "<": return String.format("%s >= %s", left, right);
                case "<=": return String.format("%s > %s", left, right);
                case "==": return String.format("%s != %s", left, right);
                case "!=": return String.format("%s == %s", left, right);
            }
        }

        return expression; // Return as-is if no match
    }

    // Helper method to invert operators when operands are swapped
    private static String invertOperator(String operator) {
        switch (operator) {
            case ">": return "<";
            case ">=": return "<=";
            case "<": return ">";
            case "<=": return ">=";
            default: return operator; // == and != remain the same
        }
    }

    private static String simplifyExpression(String condition) {
        // Enhanced regex to handle both reversed and normal conditions
        Pattern pattern = Pattern.compile(
            "(\\d+)\\s*([<>!=]=?|==)\\s*([a-zA-Z_][a-zA-Z_0-9]*)(\\s*(?:[+\\-]\\s*\\d+)+)?" + // Case 1: "9 < x + 1 + 2"
            "|([a-zA-Z_][a-zA-Z_0-9]*)(\\s*(?:[+\\-]\\s*\\d+)+)?\\s*([<>!=]=?|==)\\s*(\\d+)"   // Case 2: "x + 1 + 2 > 9"
        );
        Matcher matcher = pattern.matcher(condition);

        StringBuffer simplified = new StringBuffer();

        while (matcher.find()) {
            String simplifiedExpression = null;

            if (matcher.group(1) != null) {
                // Case 1: "9 < x + 1 + 2"
                int constant1 = Integer.parseInt(matcher.group(1)); // e.g., 9
                String comparison = matcher.group(2); // <, <=, etc.
                String variable = matcher.group(3); // x
                String operationChain = matcher.group(4); // e.g., + 1 + 2

                // Reverse comparison and evaluate
                comparison = reverseComparison(comparison);
                int constantSum = evaluateOperationChain(operationChain);
                int result = constant1 - constantSum;

                simplifiedExpression = String.format("%s %s %d", variable, comparison, result);
            } else {
                // Case 2: "x + 1 + 2 > 9"
                String variable = matcher.group(5); // x
                String operationChain = matcher.group(6); // e.g., + 1 + 2
                String comparison = matcher.group(7); // >, >=, etc.
                int constant2 = Integer.parseInt(matcher.group(8)); // e.g., 9

                // Evaluate and simplify
                int constantSum = evaluateOperationChain(operationChain);
                int result = constant2 - constantSum;

                simplifiedExpression = String.format("%s %s %d", variable, comparison, result);
            }

            matcher.appendReplacement(simplified, simplifiedExpression);
        }

        matcher.appendTail(simplified);
        return simplified.toString();
    }

    // Helper method to evaluate an operation chain like "+ 1 + 2"
    private static int evaluateOperationChain(String operationChain) {
        if (operationChain == null || operationChain.trim().isEmpty()) {
            return 0;
        }
        int constantSum = 0;
        Matcher opMatcher = Pattern.compile("[+\\-]\\s*\\d+").matcher(operationChain);
        while (opMatcher.find()) {
            String operation = opMatcher.group().trim();
            int value = Integer.parseInt(operation.replaceAll("[+\\-]", "").trim());
            constantSum += operation.startsWith("+") ? value : -value;
        }
        return constantSum;
    }

    // Helper method to reverse comparison operators
    private static String reverseComparison(String comparison) {
        switch (comparison) {
            case "<": return ">";
            case "<=": return ">=";
            case ">": return "<";
            case ">=": return "<=";
            default: return comparison; // == or != remains unchanged
        }
    }


    // Helper method to calculate the simplified value
    private static int calculateSimplifiedValue(String operator, int constant1, int constant2) {
        switch (operator) {
            case "+": return constant1 - constant2;
            case "-": return constant1 + constant2;
            case "*": return constant1 / constant2; // Assume no rounding issues
            case "/": return constant1 * constant2;
            default: throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
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
        condition = condition.replaceAll("\\btrue&&", "");
        condition = condition.replaceAll("&&true\\b", "");
        condition = condition.replaceAll("\\bfalse\\|\\|", "");
        condition = condition.replaceAll("\\|\\|false\\b", "");
        return condition;
    }

    private static String removeDuplicates(String condition) {
        // Normalize spaces to make parsing easier
        condition = condition.replaceAll("\\s+", " ").trim();

        // Handle "&&" expressions
        if (condition.contains("&&")) {
            List<String> clauses = Arrays.asList(condition.split("\\s*&&\\s*"));
            Set<String> uniqueClauses = new LinkedHashSet<>(clauses);

            // Simplify clauses inside "&&"
            List<String> simplifiedClauses = uniqueClauses.stream()
                .map(Simplifier::simplifyNestedLogic) // Recursively simplify nested logic
                .collect(Collectors.toList());

            // If the result is a single clause, return it
            if (simplifiedClauses.size() == 1) {
                return simplifiedClauses.get(0);
            }

            return String.join(" && ", simplifiedClauses);
        }
        // Handle "||" expressions
        else if (condition.contains("||")) {
            List<String> clauses = Arrays.asList(condition.split("\\s*\\|\\|\\s*"));
            Set<String> uniqueClauses = new LinkedHashSet<>(clauses);

            // Simplify clauses inside "||"
            List<String> simplifiedClauses = uniqueClauses.stream()
                .map(Simplifier::simplifyNestedLogic) // Recursively simplify nested logic
                .collect(Collectors.toList());

            // If the result is a single clause, return it
            if (simplifiedClauses.size() == 1) {
                return simplifiedClauses.get(0);
            }

            return String.join(" || ", simplifiedClauses);
        }
        return condition;
    }

    // Simplify nested logical expressions
    private static String simplifyNestedLogic(String expression) {
        // Check for nested parentheses and simplify them
        if (expression.startsWith("(") && expression.endsWith(")")) {
            return simplify(expression.substring(1, expression.length() - 1).trim());
        }
        return expression;
    }
    
    // Simplifies nested expressions like "((x > 0 && y < 10))" to "x > 0 && y < 10"
    private static String simplifyNested(String condition) {
        while (condition.startsWith("(") && condition.endsWith(")")) {
            condition = condition.substring(1, condition.length() - 1).trim();
        }
        return condition;
    }
}
