package com.verifier.HoareLogic;

import com.verifier.Syntax.Statement;
import com.verifier.VCG.VerificationCondition;

import java.util.List;

public abstract class HoareRule {
    public abstract List<VerificationCondition> apply(Statement statement, String pre, String post);
}
