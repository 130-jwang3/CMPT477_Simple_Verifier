// Generated from java-escape by ANTLR 4.11.1

package com.verifier.parser.generated;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GrammarParser}.
 */
public interface GrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GrammarParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(GrammarParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(GrammarParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#precondition}.
	 * @param ctx the parse tree
	 */
	void enterPrecondition(GrammarParser.PreconditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#precondition}.
	 * @param ctx the parse tree
	 */
	void exitPrecondition(GrammarParser.PreconditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#postcondition}.
	 * @param ctx the parse tree
	 */
	void enterPostcondition(GrammarParser.PostconditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#postcondition}.
	 * @param ctx the parse tree
	 */
	void exitPostcondition(GrammarParser.PostconditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#conditionList}.
	 * @param ctx the parse tree
	 */
	void enterConditionList(GrammarParser.ConditionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#conditionList}.
	 * @param ctx the parse tree
	 */
	void exitConditionList(GrammarParser.ConditionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(GrammarParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(GrammarParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(GrammarParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(GrammarParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(GrammarParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(GrammarParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#conditionalStatement}.
	 * @param ctx the parse tree
	 */
	void enterConditionalStatement(GrammarParser.ConditionalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#conditionalStatement}.
	 * @param ctx the parse tree
	 */
	void exitConditionalStatement(GrammarParser.ConditionalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(GrammarParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(GrammarParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(GrammarParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(GrammarParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(GrammarParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(GrammarParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(GrammarParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(GrammarParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#relOp}.
	 * @param ctx the parse tree
	 */
	void enterRelOp(GrammarParser.RelOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#relOp}.
	 * @param ctx the parse tree
	 */
	void exitRelOp(GrammarParser.RelOpContext ctx);
}