package com.verifier.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.verifier.parser.generated.GrammarLexer;
import com.verifier.parser.generated.GrammarParser;

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
}