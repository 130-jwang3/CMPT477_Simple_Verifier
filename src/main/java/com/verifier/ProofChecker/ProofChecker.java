package com.verifier.ProofChecker;

import com.verifier.HoareLogic.ConditionUtils;
import com.verifier.VCG.VerificationCondition;
import com.microsoft.z3.*;

public class ProofChecker {

    // Validates a single verification condition
    public static boolean validate(VerificationCondition vc) {
        String pre = Simplifier.simplify(vc.getPrecondition());
        String post = Simplifier.simplify(vc.getPostcondition());
        String actualPre = Simplifier.simplify(vc.getActualPrecondition());

        System.out.println("Checking implication: pre = " + actualPre + ", post = " + pre);
        if (!implies(actualPre, pre)) {
            System.out.println("Validation failed: Actual precondition does not imply derived precondition.");
            return false;
        }

        System.out.println("Validation passed for: " + vc.getStatement());
        return true;
    }

    // Checks if precondition implies postcondition using Z3 SMT solver
    private static boolean implies(String pre, String post) {
        try (Context ctx = new Context()) {
            Solver solver = ctx.mkSolver();
            System.out.println("Checking implication: pre = " + pre + ", post = " + post);

            // Parse precondition and postcondition as logical expressions
            BoolExpr preExpr = parseCondition(ctx, pre);
            BoolExpr postExpr = parseCondition(ctx, post);

            System.out.println("Parsed Pre: " + preExpr);
            System.out.println("Parsed Post: " + postExpr);

            // Create implication: pre → post
            BoolExpr implication = ctx.mkImplies(preExpr, postExpr);

            // Negate the implication to check for unsatisfiability
            solver.add(ctx.mkNot(implication));
            Status status = solver.check();

            System.out.println("SMT Solver Status: " + status);
            return status == Status.UNSATISFIABLE; // UNSATISFIABLE means the implication holds
        } catch (Exception e) {
            System.err.println("SMT Solver Error: " + e.getMessage());
            return false;
        }
    }

    // Helper method to parse a logical condition string into a Z3 BoolExpr
    private static BoolExpr parseCondition(Context ctx, String condition) throws Exception {
        // Debugging output
        System.out.println("Parsing condition: " + condition);

        // Handle conjunction (&&) and disjunction (||)
        if (condition.contains("&&") || condition.contains("||")) {
            String[] parts;
            if (condition.contains("&&")) {
                parts = condition.split("&&");
                return ctx.mkAnd(parseCondition(ctx, parts[0].trim()), parseCondition(ctx, parts[1].trim()));
            } else if (condition.contains("||")) {
                parts = condition.split("\\|\\|");
                return ctx.mkOr(parseCondition(ctx, parts[0].trim()), parseCondition(ctx, parts[1].trim()));
            }
        }

        // Handle negation
        if (condition.startsWith("!")) {
            return ctx.mkNot(parseCondition(ctx, condition.substring(1).trim()));
        }

        // Ensure proper parsing of relational operators
        String[] parts;
        if (condition.contains(">=")) {
            parts = condition.split(">=");
            return ctx.mkGe(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (condition.contains("<=")) {
            parts = condition.split("<=");
            return ctx.mkLe(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (condition.contains(">")) {
            parts = condition.split(">");
            return ctx.mkGt(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (condition.contains("<")) {
            parts = condition.split("<");
            return ctx.mkLt(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (condition.contains("==")) {
            parts = condition.split("==");
            return ctx.mkEq(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (condition.contains("!=")) {
            parts = condition.split("!=");
            return ctx.mkNot(ctx.mkEq(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim())));
        }

        throw new Exception("Unsupported condition format: " + condition);
    }

    private static ArithExpr parseExpression(Context ctx, String expr) throws Exception {
        expr = expr.trim();

        if (expr.isEmpty()) {
            throw new Exception("Unsupported expression: Expression is empty.");
        }

        // Debugging output
        System.out.println("Parsing expression: " + expr);

        // Handle negative numbers
        if (expr.matches("-\\d+")) {
            return ctx.mkInt(Integer.parseInt(expr)); // Parse as a negative integer
        }

        // Handle integer literals
        if (expr.matches("\\d+")) {
            return ctx.mkInt(Integer.parseInt(expr)); // Parse as a positive integer
        }

        // Handle variables
        if (expr.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return ctx.mkIntConst(expr); // Parse as a variable
        }

        // Handle arithmetic operations
        if (expr.contains("+")) {
            String[] parts = expr.split("\\+", 2);
            return ctx.mkAdd(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (expr.contains("-")) {
            String[] parts = expr.split("-", 2);
            return ctx.mkSub(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (expr.contains("*")) {
            String[] parts = expr.split("\\*", 2);
            return ctx.mkMul(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (expr.contains("/")) {
            String[] parts = expr.split("/", 2);
            return ctx.mkDiv(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        }

        throw new Exception("Unsupported expression: " + expr);
    }
}
