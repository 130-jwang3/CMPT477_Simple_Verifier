package com.verifier.ProofChecker;

import com.verifier.VCG.VerificationCondition;
import com.microsoft.z3.*;

public class ProofChecker {

    // Validates a single verification condition
    public static boolean validate(VerificationCondition vc) {

        System.out.println("original: "+vc.getPrecondition()+" "+vc.getPostcondition());
        String pre = Simplifier.simplify(vc.getPrecondition());
        String post = Simplifier.simplify(vc.getPostcondition());
        String statement = vc.getStatement();

        if (pre == null || post == null || pre.isEmpty() || post.isEmpty()) {
            System.out.println("Validation failed: Precondition or postcondition is empty.");
            return false;
        }

        // Use an SMT solver to validate
        return implies(pre, post);
    }

    // Checks if precondition implies postcondition using Z3 SMT solver
    private static boolean implies(String pre, String post) {
        try (Context ctx = new Context()) {
            Solver solver = ctx.mkSolver();
            System.out.println("pre: " + pre);
            System.out.println("post: " + post);
            // Parse precondition and postcondition as logical expressions
            BoolExpr preExpr = parseCondition(ctx, pre);
            BoolExpr postExpr = parseCondition(ctx, post);
            System.out.println("Simplified pre: " + preExpr.toString());
            System.out.println("Simplified post: " + postExpr.toString());


            // Check if precondition implies postcondition
            BoolExpr implication = ctx.mkImplies(preExpr, postExpr);
            System.out.println("Simplified Implication: " + implication.simplify());
//            validateSolver();
            solver.add(ctx.mkNot(implication)); // Negate the implication to check for unsatisfiability
//            System.out.println("SMT Solver Status: " + solver.check());
            // If the negation is unsatisfiable, implication holds
            return solver.check() == Status.UNSATISFIABLE;
        } catch (Exception e) {
            System.err.println("SMT Solver Error: " + e.getMessage());
            return false;
        }
    }

//    private static void validateSolver(){
//        try (Context ctx = new Context()) {
//            BoolExpr pre = ctx.mkEq(ctx.mkIntConst("x"), ctx.mkInt(2)); // x > 0
//            BoolExpr post = ctx.mkEq(ctx.mkIntConst("x"), ctx.mkIntConst("y")); // 5 > 4
//            BoolExpr implication = ctx.mkImplies(post, pre);
//
//            Solver solver = ctx.mkSolver();
//            solver.add(ctx.mkNot(implication)); // Negate the implication
//            System.out.println();
//            System.out.println("SMT Solver Status: " + solver.check());
//        }
//    }

    // Helper method to parse a logical condition string into a Z3 BoolExpr
    private static BoolExpr parseCondition(Context ctx, String condition) throws Exception {
        condition = condition.trim();

        // Handle conjunction (&&) and disjunction (||)
        if (condition.contains("&&") || condition.contains("||")) {
            String[] parts;
            if (condition.contains("&&")) {
                parts = condition.split("&&");
                BoolExpr left = parseCondition(ctx, parts[0].trim());
                BoolExpr right = parseCondition(ctx, parts[1].trim());
                return ctx.mkAnd(left, right);
            } else if (condition.contains("||")) {
                parts = condition.split("\\|\\|");
                BoolExpr left = parseCondition(ctx, parts[0].trim());
                BoolExpr right = parseCondition(ctx, parts[1].trim());
                return ctx.mkOr(left, right);
            }
        }

        // Handle relational operators
        if (condition.contains(">")) {
            String[] parts = condition.split(">");
            return ctx.mkGt(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (condition.contains("<")) {
            String[] parts = condition.split("<");
            return ctx.mkLt(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (condition.contains("==")) {
            String[] parts = condition.split("==");
            return ctx.mkEq(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim()));
        } else if (condition.contains("!=")) {
            String[] parts = condition.split("!=");
            return ctx.mkNot(ctx.mkEq(parseExpression(ctx, parts[0].trim()), parseExpression(ctx, parts[1].trim())));
        }

        throw new Exception("Unsupported condition format: " + condition);
    }

    private static ArithExpr parseExpression(Context ctx, String expr) throws Exception {
        expr = expr.trim();

        // Handle integer literals
        if (expr.matches("\\d+")) {
            return ctx.mkInt(Integer.parseInt(expr));
        }

        // Handle variables
        if (expr.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return ctx.mkIntConst(expr);
        }

        throw new Exception("Unsupported expression: " + expr);
    }
}
