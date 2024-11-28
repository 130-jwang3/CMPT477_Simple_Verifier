package com.verifier.parser;

import com.verifier.Syntax.*;
import com.verifier.Syntax.Parser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.verifier.parser.generated.GrammarLexer;
import com.verifier.parser.generated.GrammarParser;

import java.util.ArrayList;
import java.util.List;

public class ProgramParser {
    public static ParseTree parse(String input) throws Exception {
        CharStream charStream = CharStreams.fromString(input);
        GrammarLexer lexer = new GrammarLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line,
                                    int charPositionInLine,
                                    String msg,
                                    RecognitionException e) {
                throw new RuntimeException("Error at line " + line +
                        ":" + charPositionInLine + " " + msg);
            }
        });

        return parser.program();
    }

    public static String getParseTreeText(ParseTree tree) {
        if (tree == null) return "";
        StringBuilder sb = new StringBuilder();
        getParseTreeText(tree, sb, 0);
        return sb.toString();
    }

    private static void getParseTreeText(ParseTree tree, StringBuilder sb, int indent) {
        sb.append("  ".repeat(indent));
        if (tree instanceof TerminalNode) {
            sb.append(tree.getText());
        } else {
            sb.append(tree.getClass().getSimpleName().replace("Context", ""));
        }
        sb.append('\n');
        for (int i = 0; i < tree.getChildCount(); i++) {
            getParseTreeText(tree.getChild(i), sb, indent + 1);
        }
    }

    public static Parser toAST(ParseTree tree) {
        // Step 1: Extract preconditions
        List<Condition> preconditions = extractPreconditions(tree.getChild(0));
        System.out.println("Extracted Preconditions: " + preconditions);

        // Step 2: Extract statements (body of the program)
        Statement body = extractStatements(tree.getChild(1));
        System.out.println("Extracted Body: " + body);

        // Step 3: Extract postconditions
        List<Condition> postconditions = extractPostconditions(tree.getChild(2));
        System.out.println("Extracted Postconditions: " + postconditions);

        // Step 4: Create and return the Parser instance
        return new Parser(preconditions, body, postconditions);
    }

    private static List<Condition> extractPreconditions(ParseTree preconditionNode) {
        List<Condition> preconditions = new ArrayList<>();

        // Ensure "pre:" exists in the parse tree
        if (preconditionNode.getChildCount() > 1) {
            // Get the ConditionList node (second child)
            ParseTree conditionListNode = preconditionNode.getChild(2);

            // Traverse the children of the ConditionList node
            for (int i = 0; i < conditionListNode.getChildCount(); i++) {
                ParseTree conditionTree = conditionListNode.getChild(i);

                // Skip commas
                if (!conditionTree.getText().equals(",")) {
                    preconditions.add(toCondition(conditionTree));
                }
            }
        }

        return preconditions;
    }
    private static List<Condition> extractPostconditions(ParseTree postconditionNode) {
        List<Condition> postconditions = new ArrayList<>();

        // Ensure "post:" exists in the parse tree
        if (postconditionNode.getChildCount() > 1) {
            // Get the ConditionList node (second child)
            ParseTree conditionListNode = postconditionNode.getChild(2);

            // Traverse the children of the ConditionList node
            for (int i = 0; i < conditionListNode.getChildCount(); i++) {
                ParseTree conditionTree = conditionListNode.getChild(i);

                // Skip commas
                if (!conditionTree.getText().equals(",")) {
                    postconditions.add(toCondition(conditionTree));
                }
            }
        }

        return postconditions;
    }


    private static Statement extractStatements(ParseTree statementListNode) {
        List<Statement> statements = new ArrayList<>();

        for (int i = 0; i < statementListNode.getChildCount(); i++) {
            ParseTree child = statementListNode.getChild(i);

            // Skip semicolons
            if (child.getText().equals(";")) {
                continue;
            }

            // Extract the actual statement from StatementContext
            if (child instanceof GrammarParser.StatementContext) {
                ParseTree actualStatement = child.getChild(0); // Get the specific child (Assignment or Conditional)
                statements.add(toStatement(actualStatement));
            } else {
                throw new IllegalArgumentException("Unsupported node in statement list: " + child.getClass().getSimpleName());
            }
        }

        // Wrap multiple statements in a Sequence node
        if (statements.size() == 1) {
            return statements.get(0); // Single statement, return directly
        } else {
            return new Sequence(statements); // Multiple statements, return as a sequence
        }
    }

    private static Condition toCondition(ParseTree conditionTree) {
        String left = conditionTree.getChild(0).getText();
        String operator = conditionTree.getChild(1).getText();
        String right = conditionTree.getChild(2).getText();

        // Debugging
        System.out.println("Processing condition: " + left + " " + operator + " " + right);

        return new Condition(left, operator, right);
    }

    private static Statement toStatement(ParseTree statementNode) {
        System.out.println("Processing statement node: " + statementNode.getText());

        // Handle StatementContext: Extract its child node
        if (statementNode instanceof GrammarParser.StatementContext) {
            System.out.println("Detected StatementContext, extracting child...");
            return toStatement(statementNode.getChild(0)); // Process the first child (e.g., AssignmentContext or ConditionalStatementContext)
        }

        // Handle Assignment
        if (statementNode instanceof GrammarParser.AssignmentContext) {
            System.out.println("Detected assignment: " + statementNode.getText());
            String variable = statementNode.getChild(0).getText();
            Expression expression = toExpression(statementNode.getChild(2));
            return new Assignment(variable, expression);

            // Handle Conditional Statement
        } else if (statementNode instanceof GrammarParser.ConditionalStatementContext) {
            System.out.println("Detected conditional: " + statementNode.getText());
            Condition condition = toCondition(statementNode.getChild(1));
            Statement thenBranch = toStatement(statementNode.getChild(3)); // Process "then" branch
            Statement elseBranch = statementNode.getChildCount() > 4
                    ? toStatement(statementNode.getChild(5)) // Process "else" branch if it exists
                    : null;
            return new Conditional(condition, thenBranch, elseBranch);
        }

        // Throw an error for unsupported statement types
        throw new IllegalArgumentException("Unsupported statement type: " + statementNode.getClass().getSimpleName());
    }


    private static Expression toExpression(ParseTree expressionTree) {
        if (expressionTree.getChildCount() == 1) {
            // Single term
            System.out.println("Processing single term: " + expressionTree.getText());
            return new Expression(expressionTree.getText(), null, null);
        } else {
            String left = expressionTree.getChild(0).getText();
            String operator = expressionTree.getChild(1).getText();
            String right = expressionTree.getChild(2).getText();
            System.out.println("Processing expression: " + left + " " + operator + " " + right);
            return new Expression(left, operator, right);
        }
    }
}