# Project 11 AES Implementation

This submission was prepared by Georgios Panormitis Latos for the course DA563A Introduction to Computer Security, taught by Qinghua Wang. The date of submission is 28/03/2026.

The project contains four Java source files and the final PDF report. `AesCryptoService.java` handles AES key generation, encryption, and decryption. `AesGuiApp.java` provides the graphical interface. `AesRoundTripCheck.java` is a small check for the required assignment text. `AesServiceSelfTest.java` contains the tests used to verify that the program also works with other kinds of input.

To compile the project, open a terminal in the project folder and run `javac src/*.java`.

To start the program, run `java -cp src AesGuiApp`.

To verify the required assignment text, run `java -cp src AesRoundTripCheck`.

To run the broader tests, run `java -cp src AesServiceSelfTest`.

The self test checks empty text, text with spaces, normal text, multiline text, punctuation and tabs, UTF 8 text, and invalid ciphertext handling.
