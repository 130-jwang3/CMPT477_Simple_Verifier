package com.verifier.VCG;

public class VerificationCondition {
    private final String precondition;
    private final String actualPrecondition;
    private final String statement;
    private final String postcondition;

    public VerificationCondition(String precondition, String statement, String postcondition, String actualPrecondition) {
        this.precondition = precondition;
        this.statement = statement;
        this.postcondition = postcondition;
        this.actualPrecondition = actualPrecondition;
    }

    // Getter for precondition
    public String getPrecondition() {
        return precondition;
    }

    // Getter for statement
    public String getStatement() {
        return statement;
    }

    // Getter for postcondition
    public String getPostcondition() {
        return postcondition;
    }

    public String getActualPrecondition() {
        return actualPrecondition;
    }

    @Override
    public String toString() {
        return "VC: {" +
                "Precondition: " + precondition +
                ", Statement: " + statement +
                ", Postcondition: " + postcondition +
                "}";
    }
}
