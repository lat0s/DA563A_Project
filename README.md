# AES Project 11

This project is a small Java AES encryption and decryption assignment for the course `DA563A Introduction to Computer Security`.

## Files

- `src/AesCryptoService.java`: AES key generation, encryption, and decryption logic
- `src/AesGuiApp.java`: Swing GUI with Encrypt and Decrypt buttons
- `src/AesRoundTripCheck.java`: quick command-line check for the required assignment text
- `src/AesServiceSelfTest.java`: broader self-test for different kinds of text
- `report/project11_report.tex`: LaTeX source for the final report
- `report/project11_report.pdf`: compiled report PDF

## How to Compile

From the project root:

```bash
javac src/*.java
```

## How to Run the GUI

```bash
java -cp src AesGuiApp
```

The window starts with the required text `Introduction to Computer Security` already loaded.

## Quick Check

This verifies the exact assignment message:

```bash
java -cp src AesRoundTripCheck
```

## Run the Self-Tests

This checks that the project works with different types of text:

- empty text
- text with spaces
- normal ASCII text
- multiline text
- punctuation and tabs
- UTF-8 text
- invalid ciphertext handling

Run it with:

```bash
java -cp src AesServiceSelfTest
```

## Rebuild the Report PDF

If `tectonic` is installed:

```bash
cd report
tectonic project11_report.tex
```

## Regenerate the Report Screenshots

From the project root:

```bash
java -Djava.awt.headless=true -cp src AesGuiApp --capture report/figures
```
