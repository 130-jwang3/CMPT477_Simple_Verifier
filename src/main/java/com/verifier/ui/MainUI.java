package com.verifier.ui;

import com.verifier.ProofChecker.ProofChecker;
import com.verifier.ProofChecker.Simplifier;
import com.verifier.Syntax.Parser;
import com.verifier.VCG.VerificationCondition;
import com.verifier.VCG.VerificationConditionGenerator;
import com.verifier.Verifier;
import com.verifier.parser.ProgramParser;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class MainUI extends Application {
    private TextArea programInput;
    private TextArea outputArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Program Verifier");

        // Create the main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));


        Label inputLabel = new Label("Program Input:");
        programInput = new TextArea();
        programInput.setPromptText("Enter your program here...\nExample:\nx := 5\nif x > 0 then y := 1 else y := 0");
        programInput.setPrefRowCount(10);


        Button verifyButton = new Button("Verify Program");
        verifyButton.setOnAction(e -> verifyProgram());


        Label outputLabel = new Label("Verification Results:");
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefRowCount(10);
        outputArea.setWrapText(true);

        // Add components to layout
        mainLayout.getChildren().addAll(
                inputLabel,
                programInput,
                verifyButton,
                outputLabel,
                outputArea
        );


        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void verifyProgram() {
        String program = programInput.getText().trim();
        if (program.isEmpty()) {
            showError("Please enter a program to verify.");
            return;
        }

        Verifier verifier = new Verifier();
        String results = verifier.verify(program);
        outputArea.setText(results);
    }




    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        outputArea.setText("Error: " + message);
    }

    private void clearInterface() {
        programInput.clear();
        outputArea.clear();
    }
}