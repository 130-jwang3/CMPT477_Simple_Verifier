## ReadMe to write project report ##

### Current Progress ### 
*Danny*: \
Update project structure set up with Maven and JavaFX\
Implemented ANTLR4 grammar and the program parser\
Create a simple JavaFX UI for program input and verification results

After cloning, go to the root folder and enter these cmd in terminal to bring up the GUI: \
mvn clean compile\
mvn javafx:run\


Current Structure :\
src/\
├── main/\
│   ├── java/com/verifier/\
│   │   ├── Main.java\
│   │   ├── ui/ (*contain the JavaFX MainUI*) \
│   │   ├── parser/*(contain the program parser)*\
│   │   ├── hoarelogic/\
│   │   ├── syntax/\
│   │   └── vcg/\
│   └── resources/\
│        ______└── grammar/ (contain the ANTLR4 library)\
└── test/\
└── java/com/verifier/