package com.verifier.ProofChecker;

import com.verifier.VCG.VerificationCondition;
import com.microsoft.z3.*;

public class ProofChecker {

    // Validates a single verification condition
    public static boolean validate(VerificationCondition vc) {
        String pre = vc.getPrecondition();
        String post = vc.getPostcondition();
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

            // Parse precondition and postcondition as logical expressions
            BoolExpr preExpr = parseCondition(ctx, pre);
            BoolExpr postExpr = parseCondition(ctx, post);

            // Check if precondition implies postcondition
            BoolExpr implication = ctx.mkImplies(preExpr, postExpr);
            solver.add(ctx.mkNot(implication)); // Negate the implication to check for unsatisfiability

            // If the negation is unsatisfiable, implication holds
            return solver.check() == Status.UNSATISFIABLE;
        } catch (Exception e) {
            System.err.println("SMT Solver Error: " + e.getMessage());
            return false;
        }
    }

    // Helper method to parse a logical condition string into a Z3 BoolExpr
    private static BoolExpr parseCondition(Context ctx, String condition) throws Exception 
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

        if (condition.contains(">")) {
            String[] parts = condition.split(">");
            return ctx.mkGt(ctx.mkIntConst(parts[0].trim()), ctx.mkInt(Integer.parseInt(parts[1].trim())));
        } else if (condition.contains("<")) {
            String[] parts = condition.split("<");
            return ctx.mkLt(ctx.mkIntConst(parts[0].trim()), ctx.mkInt(Integer.parseInt(parts[1].trim())));
        } else if (condition.contains("==")) {
            String[] parts = condition.split("==");
            return ctx.mkEq(ctx.mkIntConst(parts[0].trim()), ctx.mkInt(Integer.parseInt(parts[1].trim())));
        } else if (condition.contains("!=")) {
            String[] parts = condition.split("!=");
            return ctx.mkNot(ctx.mkEq(ctx.mkIntConst(parts[0].trim()), ctx.mkInt(Integer.parseInt(parts[1].trim()))));
        }

        throw new Exception("Unsupported condition format: " + condition);
    }
}
