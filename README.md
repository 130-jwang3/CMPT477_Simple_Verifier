## ReadMe to write project report ##

### Current Progress ### 
*Danny*: \
Basic project structure set up with Maven and JavaFX\
ANTLR4 grammar implementation for parsing\
JavaFX UI skeleton for program input and verification results

After cloning, go to the root folder and enter in terminal to bring up the GUI: \
mvn clean compile\
mvn javafx:run

Current Structure :\
src/\
├── main/\
│   ├── java/com/verifier/\
│   │   ├── Main.java\
│   │   ├── ui/ (*contain MainUI*) \
│   │   ├── parser/*(contain Parser library)*\
│   │   ├── hoarelogic/\
│   │   ├── syntax/\
│   │   └── vcg/\
│   └── resources/\
│        ______└── grammar/ (contain the ANTLR4 library)\
└── test/\
└── java/com/verifier/