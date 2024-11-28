package com.verifier;

import com.verifier.ProofChecker.ProofChecker;
import com.verifier.ProofChecker.Simplifier;
import com.verifier.Syntax.Parser;
import com.verifier.VCG.VerificationCondition;
import com.verifier.VCG.VerificationConditionGenerator;
import com.verifier.parser.ProgramParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class Verifier {

    public String verify(String program) {
        try {
            // Step 1: Parse the program into a ParseTree
            ParseTree tree = ProgramParser.parse(program);

            // Step 2: Convert the ParseTree to the AST (Parser object)
            Parser parserAST = ProgramParser.toAST(tree);

            // Step 2.5: Validate the AST
            if (parserAST.getBody() == null || parserAST.getPreconditions().isEmpty() || parserAST.getPostconditions().isEmpty()) {
                return "Invalid program: Missing preconditions, body, or postconditions.";
            }

            // Step 3: Generate Verification Conditions
            VerificationConditionGenerator vcg = new VerificationConditionGenerator();
            List<VerificationCondition> vcs = vcg.generateVC(
                    parserAST.getBody(),
                    parserAST.getPreconditions(),
                    parserAST.getPostconditions()
            );

            // Step 3.5: Handle empty VCs
            if (vcs.isEmpty()) {
                return "Program parsed successfully, but no verification conditions were generated.";
            }

            // Step 4: Validate and simplify the Verification Conditions
            StringBuilder results = new StringBuilder("Program parsed successfully!\n\n");
            results.append("Verification Conditions:\n");
            boolean allValid = true;

            for (VerificationCondition vc : vcs) {
                String simplifiedPre = Simplifier.simplify(vc.getPrecondition());
                String simplifiedPost = Simplifier.simplify(vc.getPostcondition());
                boolean isValid = ProofChecker.validate(vc);
                allValid = allValid && isValid;

                results.append("VC: {Precondition: ").append(simplifiedPre)
                        .append(", Statement: ").append(vc.getStatement())
                        .append(", Postcondition: ").append(simplifiedPost)
                        .append("} : ").append(isValid ? "Valid" : "Invalid").append("\n");
            }

            results.append("\nFinal Result: ").append(allValid ? "All conditions satisfied!" : "Some conditions failed.");
            return results.toString();

        } catch (Exception e) {
            return "Error verifying program: " + e.getMessage();
        }
    }
}
