# Implementing a Theorem Prover
This project implements a theorem prover based on Hoare Logic, designed to verify simplified imperative programs. It ensures that programs meet their specified preconditions and postconditions, providing a framework for software correctness verification.
- Assignment: `x := expr`
- Conditional Statements: `if condition then ... else ...` (Nested conditionals are not supported)
- Sequences: `{S1; S2}` (Only assignments are allowed within sequences)
- Basic Data Types: Integers and booleans
- Logical and Arithmetic Expressions: +, -, *, /, &&, ||, <, <=, ==, !=
## Workflow:
1. Parse the input program into a syntax tree using ANTLR.
2. Convert the syntax tree into an Abstract Syntax Tree (AST).
3. Generate verification conditions (VCs) based on Hoare Logic rules.
4. Simplify VCs and format them for Z3.
5. Validate VCs using the Z3 SMT solver.
6. Return the validation results.
- Parsing: Converts source code into an AST (Key file: ProgramParser.java)
- Syntax Representation: Represents structures like Assignment, Sequence, and Condition
- Verification Condition Generation: Generates VCs (Key file: VerificationConditionGenerator.java)
- Proof Checking: Validates conditions with Z3 (Key file: ProofChecker.java)
- Utilities: Helper methods for logical conditions (Key file: ConditionUtils.java)
## Evaluation:
We tested the verifier with both successful and unsuccessful programs.

**Example 1: Successful Verification**
- Input:
- Result: All verification conditions were satisfied.
![Figure 1: Successful Verification](path_to_image_1)

**Example 2: Failed Verification**
- Input:
- Result: One or more verification conditions failed.
![Figure 2: Failed Verification](path_to_image_2)

## Instructions:
1. **System Requirements:**
   - Operating System: Windows
   - IDE: IntelliJ IDEA
   - Z3 Version: 4.12.6
   - JavaFX Library Dependency: Follow steps in reference [5] for library issues.

2. **Open the Project:**
   - Open the project in IntelliJ IDEA.

3. **Add Z3 Library:**
   - Add `com.microsoft.z3.jar` from `src/main/resources/z3-4.12.6-x64-win/bin/`.

4. **Build the Project:**
   - Run the following Maven commands:
     ```
     mvn clean
     mvn install
     mvn compile
     ```

5. **Launch the Application:**
   - Execute the command: `mvn javafx:run`.
   - Enter your program in the "Program Input" field, and click "Verify Program."

## Division of Work:
- Danny Nguyen: Designed the user interface (UI) and implemented the corresponding UI functions.
- Xinyu Liu: Implemented the core logic of Hoare Logic and developed the main class for managing the verifier.
- Letian Wang: Developed the verification condition generator (VCG) and proof-checking modules.
- Zihang Yuan: Conducted testing for the verifier and prepared the project report.

## References:
1. [Z3 SMT Solver](https://stackoverflow.com/questions/76885418/how-to-link-z3-when-running-project-compiled-with-maven-where-should-the-file)
2. [ANTLR Maven Dependency](https://mvnrepository.com/artifact/org.antlr/antlr4/4.13.2)
3. [Open-Source VCG Project](https://github.com/florianschanda/PyVCG)
4. [Hoare Logic Prover Design](https://daltron.de/posts/hoare-prover/)
5. [JavaFX Dependency Issues](https://www.reddit.com/r/JavaFX/comments/suxkun/javafx_issue_with_library_dependency/)

![Figure 1: Successful Verification](images/figure1.png)
![Figure 2: Failed Verification](images/figure2.png)



