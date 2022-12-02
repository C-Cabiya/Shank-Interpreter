package assignmentOne;

import java.util.List;
import java.lang.Exception;
import java.util.HashMap;

import assignmentOne.Token.Tokens;

public class Lexer {
	
	String currentLine;
	
	HashMap <String, Tokens> reservedWords= new HashMap <String, Tokens> (); //All reserved words and their accompanying Tokens 
	
	Token [] tokens = new Token[100]; //Stores all tokens for that line
	int tokenIncrement = 0; //Increments how large the token array has gotten
	
	/**
	 * Constructor that accepts a list of Strings representing a specific file; this is the lexer for that file
	 * @param fileLine The current file string being iterated
	 */
	public Lexer(String fileLine) {
		currentLine = fileLine;
	}
	
	/**
	 * Converts the input string into an array of tokens (NUMBER(), PLUS, MINUS, etc.)
	 * @return Returns the currentString variable as an array of tokens representing the string's characters.
	 */
	
	public Token[] lex() throws RuntimeException{
		addReservedWords(); //Calls function which adds reserved words to reservedWords HashMap
		String prevState = "0"; //Updates with the last state encountered !!ONLY used for comment states!!
		String state = "0";
		String value = ""; //Value of accumulated number characters
		String word = ""; //Word made up of accumulated characters making up a defined word or an identifier
		String string = ""; //String made up of accumulated characters following any double quote
		String character = ""; //String made up of accumulated characters following a single quote;
		tokenIncrement = 0; //Determines size of the array
		int lParenCount = 0;
		for (int k = 0; k<currentLine.length(); k++) {
			if (state == "FAIL") {
				System.out.println("Lexer failed: Invalid input.");
				throw new RuntimeException("Invalid input");
			}
			char currentChar = currentLine.charAt(k);
			switch (state) {

			// Beginning state
			// No inputs have been recorded at all; this is the first state the machine is in
			case "0":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					value = value + currentChar; //Number added to NUMBER value
					state = "1";
				}
				else if (Character.isAlphabetic(currentChar)) {
					word = word + currentChar;
					state = "word";
				}
				else {
					switch (currentChar) {
					case '+':
						state = "FAIL";
						break;
					case '*':
						state = "FAIL";
						break;
					case '/':
						state = "FAIL";
						break;
					case '-':
						state = "3";
						break;
					case '.':
						value = value + currentChar; //Decimal added to NUMBER value
						state = "4";
						break;
					case ' ':
						state = "0";
						break;
					case '(':
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							prevState = "0";
							state = "comment";
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LPAREN);
							tokenIncrement++;
							state = "0";
						}
						break;
					case ')':
						state = "FAIL";
						break;
					case '=':
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '>':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.GREATERTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.GREATER);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.GREATER);
							tokenIncrement++;
						}
						break;
					case '<':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.LESSTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else if (currentLine.charAt(k+1) == '>') {
								tokens[tokenIncrement] = new Token(Tokens.NOTEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.LESS);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LESS);
							tokenIncrement++;
						}
						break;
					case '"':
						state = "string";
						break;
					case '\'':
						state = "char";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
			
			// Number state
			case "1":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					value = value + currentChar;
				}
				else if (Character.isAlphabetic(currentChar)) {
					state = "FAIL";
				}
				else {
					switch (currentChar) {
					case '+':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Adds all characters that have been accumulated in value to NUMBER token
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.PLUS); //Adds a plus token
						tokenIncrement++;
						value = ""; //Resets the value for NUMBER tokens
						state = "2";
						break;
					case '*':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Adds all characters that have been accumulated in value to NUMBER token
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.TIMES); //Adds a times token
						tokenIncrement++;
						value = ""; //Resets the value for NUMBER tokens
						state = "2";
						break;
					case '/':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Adds all characters that have been accumulated in value to NUMBER token
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.DIVIDE); //Adds a divide token
						tokenIncrement++;
						value = ""; //Resets the value for NUMBER tokens
						state = "2";
						break;
					case '-':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Adds all characters that have been accumulated in value to NUMBER token
						tokenIncrement++;
						value = ""; //Resets the value for NUMBER tokens
						state = "3";
						break;
					case '.':
						value = value + currentChar; //Decimal added to NUMBER value
						state = "4";
						break;
					case ' ':
						tokens[tokenIncrement] = new Token (Tokens.NUMBER, value); //Finalizes the number token
						tokenIncrement++;
						value = "";
						state = "6"; //Transfers to "after number" spacebar state
						break;
					case '(':
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							prevState = "1";
							state = "comment";
						}
						else {
							state = "FAIL";
						}
						break;
					case ')':
						lParenCount--;
						if (lParenCount < 0) {
							state = "FAIL";
						}
						else {
							tokens[tokenIncrement] = new Token (Tokens.NUMBER, value); //Finalizes the number token
							tokenIncrement++;
							tokens[tokenIncrement] = new Token (Tokens.RPAREN);
							tokenIncrement++;
							value = "";
							state = "6";
						}
						break;
					case '=':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '>':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Produces NUMBER token w accompanying value
						tokenIncrement++;
						value = "";
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.GREATERTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.GREATER);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.GREATER);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '<':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Produces NUMBER token w accompanying value
						tokenIncrement++;
						value = "";
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.LESSTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else if (currentLine.charAt(k+1) == '>') {
								tokens[tokenIncrement] = new Token(Tokens.NOTEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.LESS);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LESS);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '"':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Adds all characters that have been accumulated in value to NUMBER token
						tokenIncrement++;
						value = "";
						state = "string";
						break;
					case '\'':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Adds all characters that have been accumulated in value to NUMBER token
						tokenIncrement++;
						value = "";
						state = "char";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
			
			// Operator state
			case "2":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					value = value + currentChar;
					state = "1";
				}
				else if (Character.isAlphabetic(currentChar)) {
					word = word + currentChar;
					state = "word";
				}
				else {
					switch (currentChar) {
					case '+':
						state = "FAIL";
						break;
					case '*':
						state = "FAIL";
						break;
					case '/':
						state = "FAIL";
						break;
					case '-':
						state = "5";
						break;
					case '.':
						value = value + currentChar;
						state = "4";
						break;
					case ' ':
						state = "2"; 
						break;
					case '(':
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							prevState = "2";
							state = "comment";
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LPAREN);
							tokenIncrement ++;
							state = "2";
						}
						break;
					case ')':
						state = "FAIL";
						break;
					case '=':
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '"':
						state = "string";
						break;
					case '\'':
						state = "char";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
			
			// Single minus sign state.
			// Only one minus sign has been encountered, state exists bc minus sign is the only one that can repeat
			case "3":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					if (tokens[tokenIncrement-1].getTokenType().equals("PLUS") || tokens[tokenIncrement-1].getTokenType().equals("TIMES") || tokens[tokenIncrement-1].getTokenType().equals("DIVIDE")) {
						value = "-" + currentChar;
					}
					else {
						tokens[tokenIncrement] = new Token(Tokens.MINUS);
						tokenIncrement++;
						value = value+currentChar;
					}
					state = "1";
				}
				else if (Character.isAlphabetic(currentChar)) {
					tokens[tokenIncrement] = new Token(Tokens.MINUS);
					tokenIncrement++;
					word = word + currentChar;
					state = "word";
				}
				else {
					switch (currentChar) {
					case '+':
						state = "FAIL";
						break;
					case '*':
						state = "FAIL";
						break;
					case '/':
						state = "FAIL";
						break;
					case '-':
						tokens[tokenIncrement] = new Token(Tokens.MINUS);
						tokenIncrement++;
						state = "5";
						break;
					case '.':
						value = value + currentChar;
						state = "4";
						break;
					case ' ':
						tokens[tokenIncrement] = new Token(Tokens.MINUS);
						tokenIncrement++;
						state = "2";
						break;
					case '(':
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							prevState = "3";
							state = "comment";
						}
						else {
							if (currentLine.charAt(k+1) == '*') {
								state = "comment";
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.LPAREN);
								tokenIncrement++;
								tokens[tokenIncrement] = new Token(Tokens.LPAREN);
								tokenIncrement++;
								state = "2";
							}
						}
						break;
					case ')':
						state = "FAIL";
						break;
					case '=':
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '"':
						tokens[tokenIncrement] = new Token(Tokens.MINUS);
						tokenIncrement++;
						state = "string";
						break;
					case '\'':
						tokens[tokenIncrement] = new Token(Tokens.MINUS);
						tokenIncrement++;
						state = "char";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
				
			// Decimal state. When a number has already encountered a decimal point and will not accept another
			case "4":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					value = value + currentChar;
					state = "4";
				}
				else if (Character.isAlphabetic(currentChar)) {
					state = "FAIL";
				}
				else {
					if (value.equals(".")) {
						state = "FAIL";
						break;
					}
					switch (currentChar) {
					case '+':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Produces NUMBER token w accompanying value
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.PLUS); //Produces correct token for given operator
						tokenIncrement++;
						value = "";
						state = "2";
						break;
					case '*':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Produces NUMBER token w accompanying value
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.TIMES);  //Produces correct token for given operator
						tokenIncrement++;
						value = "";
						state = "2";
						break;
					case '/':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Produces NUMBER token w accompanying value
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.DIVIDE); //Produces correct token for given operator
						tokenIncrement++;
						value = "";
						state = "2";
						break;
					case '-':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Produces NUMBER token w accompanying value
						tokenIncrement++;
						state = "3";
						break;
					case '.':
						state = "FAIL";;
						break;
					case ' ':
						tokens[tokenIncrement] = new Token (Tokens.NUMBER, value); // Adds the value to the NUMBER Token
						tokenIncrement++;
						value = "";
						state = "6";
						break;
					case '(':
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							prevState = "4";
							state = "comment";
						}
						else {
							state = "FAIL";
						}
						break;
					case ')':
						lParenCount--;
						if (lParenCount < 0) {
							state = "FAIL";
						}
						else {
							tokens[tokenIncrement] = new Token (Tokens.NUMBER, value); //Finalizes the number token
							tokenIncrement++;
							tokens[tokenIncrement] = new Token (Tokens.RPAREN);
							tokenIncrement++;
							value = "";
							state = "6";
						}
						break;
					case '=':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '>':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Produces NUMBER token w accompanying value
						tokenIncrement++;
						value = "";
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.GREATERTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.GREATER);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.GREATER);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '<':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value); //Produces NUMBER token w accompanying value
						tokenIncrement++;
						value = "";
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.LESSTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else if (currentLine.charAt(k+1) == '>') {
								tokens[tokenIncrement] = new Token(Tokens.NOTEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.LESS);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LESS);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '"':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						state = "string";
						break;
					case '\'':
						tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
						tokenIncrement++;
						value = "";
						state = "char";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
			
				
			// Double minus sign state
			// Two minus signs have been encountered, no more will be accepted
			case "5":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					value = "-" + currentChar; //Appends a negative sign to the beginning of the number value
					state = "1";
				}
				else if (Character.isAlphabetic(currentChar)) {
					word = word + currentChar;
					state = "word";
				}
				else {
					switch (currentChar) {
					case '+':
						state = "FAIL";
						break;
					case '*':
						state = "FAIL";
						break;
					case '/':
						state = "FAIL";
						break;
					case '-':
						state = "FAIL";
						break;
					case '.':
						value = "-" + currentChar; //Appends a negative sign to the beginning of the number value
						state = "4";
						break;
					case ' ':
						state = "FAIL";
						break;
					case '(':
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							prevState = "5";
							state = "comment";
						}
						else {
							state = "FAIL";
						}
						break;
					case ')':
						state = "FAIL";
						break;
					case '=':
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
			
			// Space bar after number state
			case "6":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					state = "FAIL";
				}
				else if (Character.isAlphabetic(currentChar)) {
					word = word + currentChar;
					state = "word";
				}
				else {
					switch (currentChar) {
					case '+':
						tokens[tokenIncrement] = new Token(Tokens.PLUS); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '*':
						tokens[tokenIncrement] = new Token(Tokens.TIMES); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '/':
						tokens[tokenIncrement] = new Token(Tokens.DIVIDE); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '-':
						state = "3";
						break;
					case '.':
						state = "FAIL";
						break;
					case ' ':
						state = "6";
						break;
					case '(':
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							prevState = "6";
							state = "comment";
						}
						else {
							state = "FAIL";
						}
						break;
					case ')':
						lParenCount--;
						if (lParenCount < 0) {
							state = "FAIL";
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.RPAREN);
							tokenIncrement++;
							state = "6";
						}
						break;
					case '=':
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '>':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.GREATERTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.GREATER);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.GREATER);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '<':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.LESSTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else if (currentLine.charAt(k+1) == '>') {
								tokens[tokenIncrement] = new Token(Tokens.NOTEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.LESS);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LESS);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '"':
						state = "string";
						break;
					case '\'':
						state = "char";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
			
			// After =/:/;/, state.
			case "7":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					value = value + currentChar;
					state = "1";
				}
				else if (Character.isAlphabetic(currentChar)) {
					word = word + currentChar;
					state = "word";
				}
				
				else {
					switch (currentChar) {
					case '+':
						tokens[tokenIncrement] = new Token(Tokens.PLUS); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '*':
						tokens[tokenIncrement] = new Token(Tokens.TIMES); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '/':
						tokens[tokenIncrement] = new Token(Tokens.DIVIDE); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '-':
						state = "5";
						break;
					case '.':
						value = value + currentChar;
						state = "4";
						break;
					case ' ':
						state = "7";
						break;
					case '(':
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							prevState = "7";
							state = "comment";
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LPAREN);
							tokenIncrement++;
							state = "7";
						}
						break;
					case ')':
						lParenCount--;
						if (lParenCount < 0) {
							state = "FAIL";
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.RPAREN);
							tokenIncrement++;
							state = "6";
						}
						break;
					case '=':
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '>':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.GREATERTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.GREATER);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.GREATER);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '<':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.LESSTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else if (currentLine.charAt(k+1) == '>') {
								tokens[tokenIncrement] = new Token(Tokens.NOTEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.LESS);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LESS);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '"':
						state = "string";
						break;
					case '\'':
						state = "char";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
				
			// After comment state
			case "8":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					value = value + currentChar;
					state = "1";
				}
				else if (Character.isAlphabetic(currentChar)) {
					word = word + currentChar;
					state = "word";
				}
				else {
					switch (currentChar) {
					case '+':
						tokens[tokenIncrement] = new Token(Tokens.PLUS); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '*':
						tokens[tokenIncrement] = new Token(Tokens.TIMES); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '/':
						tokens[tokenIncrement] = new Token(Tokens.DIVIDE); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '-':
						state = "3";
						break;
					case '.':
						value = value + currentChar;
						state = "4";
						break;
					case ' ':
						state = "7";
						break;
					case '(':
						lParenCount++; 
						tokens[tokenIncrement] = new Token(Tokens.LPAREN);
						tokenIncrement++;
						state = "6";
						break;
					case ')':
						lParenCount--;
						if (lParenCount < 0) {
							state = "FAIL";
						}
						else {
							state = "7";
						}
						break;
					case '=':
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '>':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.GREATERTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.GREATER);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.GREATER);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '<':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.LESSTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else if (currentLine.charAt(k+1) == '>') {
								tokens[tokenIncrement] = new Token(Tokens.NOTEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.LESS);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LESS);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '"':
						state = "string";
						break;
					case '\'':
						state = "char";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
			
			// Colon state to determine regular colon useage from an assignment token
			case "9":
				//System.out.println(currentChar +state);
				if (currentChar == '=') {
					tokens[tokenIncrement] = new Token(Tokens.ASSIGN);
					tokenIncrement++;
					state = "7";
				}
				else if (Character.isDigit(currentChar)){
					tokens[tokenIncrement] = new Token(Tokens.COLON);
					tokenIncrement++;
					value = value + currentChar;
					state = "1";
				}
				else if (Character.isAlphabetic(currentChar)) {
					tokens[tokenIncrement] = new Token(Tokens.COLON);
					tokenIncrement++;
					word = word + currentChar;
					state = "word";
				}
				
				else {
					switch (currentChar) {
					case '+':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.PLUS); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '*':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.TIMES); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '/':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.DIVIDE); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '-':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						state = "3";
						break;
					case '.':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						value = value + currentChar;
						state = "4";
						break;
					case ' ':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						state = "7";
						break;
					case '(':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							prevState = "9";
							state = "comment";
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LPAREN);
							tokenIncrement++;
							state = "6";
						}
						break;
					case ')':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						lParenCount--;
						if (lParenCount < 0) {
							state = "FAIL";
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.RPAREN);
							tokenIncrement++;
							state = "6";
						}
						break;
					case ',':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						state = "9";
						break;
					case ';':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '>':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.GREATERTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.GREATER);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.GREATER);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '<':
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.LESSTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else if (currentLine.charAt(k+1) == '>') {
								tokens[tokenIncrement] = new Token(Tokens.NOTEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.LESS);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LESS);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '"':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						state = "string";
						break;
					case '\'':
						tokens[tokenIncrement] = new Token(Tokens.COLON);
						tokenIncrement++;
						state = "char";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
				
			// Word state
			case "word":
				//System.out.println(currentChar +state);
				if (Character.isDigit(currentChar)){
					word = word + currentChar;
					state = "word";
				}
				else if (Character.isAlphabetic(currentChar)) {
					word = word + currentChar;
					state = "word";
				}
				else {
					switch (currentChar) {
					case '+':
						addWord(word);
						word = "";
						tokens[tokenIncrement] = new Token(Tokens.PLUS); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '*':
						addWord(word);
						word = "";
						tokens[tokenIncrement] = new Token(Tokens.TIMES); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '/':
						addWord(word);
						word = "";
						tokens[tokenIncrement] = new Token(Tokens.DIVIDE); //Produces correct token for given operator
						tokenIncrement++;
						state = "2";
						break;
					case '-':
						addWord(word);
						word = "";
						state = "3";
						break;
					case '.':
						addWord(word);
						word = "";
						value = value + currentChar;
						state = "4";
						break;
					case ' ':
						addWord(word);
						word = "";
						state = "7";
						break;
					case '(':
						lParenCount++;
						if (currentLine.charAt(k+1) == '*') {
							state = "comment";
						}
						else {
							addWord(word);
							word = "";
							tokens[tokenIncrement] = new Token(Tokens.LPAREN);
							tokenIncrement++;
							state = "6";
						}
						break;
					case ')':
						addWord(word);
						word = "";
						lParenCount--;
						if (lParenCount < 0) {
							state = "FAIL";
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.RPAREN);
							tokenIncrement++;
							state = "6";
						}
						break;
					case '=':
						addWord(word);
						word = "";
						tokens[tokenIncrement] = new Token(Tokens.EQUAL);
						tokenIncrement++;
						state = "7";
						break;
					case ',':
						addWord(word);
						word = "";
						tokens[tokenIncrement] = new Token(Tokens.COMMA);
						tokenIncrement++;
						state = "7";
						break;
					case ':':
						addWord(word);
						word = "";
						state = "9";
						break;
					case ';':
						addWord(word);
						word = "";
						tokens[tokenIncrement] = new Token(Tokens.SEMICOLON);
						tokenIncrement++;
						state = "7";
						break;
					case '>':
						addWord(word);
						word = "";
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.GREATERTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.GREATER);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.GREATER);
							tokenIncrement++;
						}
						state = "7";
						break;
					case '<':
						addWord(word);
						word = "";
						if (k+1<currentLine.length()) {
							if (currentLine.charAt(k+1) == '=') {
								tokens[tokenIncrement] = new Token(Tokens.LESSTHANEQUAL);
								tokenIncrement++; 
								k++;
							}
							else if (currentLine.charAt(k+1) == '>') {
								tokens[tokenIncrement] = new Token(Tokens.NOTEQUAL);
								tokenIncrement++; 
								k++;
							}
							else {
								tokens[tokenIncrement] = new Token(Tokens.LESS);
								tokenIncrement++;
							}
						}
						else {
							tokens[tokenIncrement] = new Token(Tokens.LESS);
							tokenIncrement++;
						}
						state = "7";
						break;
					default :
						state = "FAIL";
						break;
					}
				}
				break;
				
			// String State	
			case "string":
				if (currentChar == '"') {
					tokens[tokenIncrement] = new Token(string, Tokens.STRINGCONTENTS);
					tokenIncrement++;
					string = "";
					state = "7";
				}
				else {
					string = string+currentChar;
				}
				break;
				
			// Character State
			case "char":
				if (currentChar == '\''){
					tokens[tokenIncrement] = new Token(character, Tokens.CHARCONTENTS);
					tokenIncrement++;
					character = "";
					state = "7";
				}
				else{
					System.out.println("Lexer 'lex' Exception: Chars can only be one character long.");
					throw new RuntimeException("Lexer 'lex' Exception: Chars can only be one character long.");
				}
				break;
				
			// Comment State
			case "comment":
				//System.out.println(currentChar +state);
				if (currentChar == '*'){
					if (currentLine.charAt(k+1) == ')') {
						state = "8";
					}
					else {
						state = "comment";
					}
				}
				else {
					state = "comment";
				}
				break;
				
			}
			if (state == "FAIL") {
				System.out.println("Lexer failed: Invalid input.");
				throw new RuntimeException("Invalid input");
			}
			
			if (lParenCount < 0) {
				state = "FAIL";
				throw new RuntimeException("Invalid input: Unclosed parentheses.");
			}
		}

		state = "0"; //Resets state to 0 for next go-around of the for loop
		
		// Remainder of the numbers stored in 'value' turned into a NUMBER token
		if(value != "") {
			tokens[tokenIncrement] = new Token(Tokens.NUMBER, value);
			tokenIncrement++;
		}
		
		// Remainder of the value stored in 'word' turned into an IDENTIFIER token
		if(word != "") {
			if (reservedWords.containsKey(word)) {
				tokens[tokenIncrement] = new Token(reservedWords.get(word));
				tokenIncrement++;
			}
			else {
				tokens[tokenIncrement] = new Token(word, Tokens.IDENTIFIER);
				tokenIncrement++;
			}
		}
		
		if (string != "") {
			System.out.println("Lexer 'lex' Exception: String not closed with double quotes.");
			throw new RuntimeException("Lexer 'lex' Exception: String not closed with double quotes.");
		}
		
		// If there are unresolved parentheses, dictated by lParenCount
		if(lParenCount != 0) {
			System.out.println("Invalid input: Unclosed parentheses.");
			throw new RuntimeException("Invalid input: Unclosed parentheses.");
		}
		
		String lastTokensType = "";
		
		// Next two if statements exist to thrown an error if a statement ends with an operator which is invalid 
		if (tokenIncrement > 0) {
		lastTokensType = tokens[tokenIncrement-1].getTokenType();
		}
		
		if(lastTokensType.equals("PLUS")||lastTokensType.equals("MINUS")||lastTokensType.equals("TIMES")||lastTokensType.equals("DIVIDE")||lastTokensType.equals("MOD")) {
			throw new RuntimeException("Invalid input: Cannot end an expression with an operator.");
		}
		
		// Produces an EndOfLine token now that we've reached the end of the line
		tokens [tokenIncrement] = new Token(Tokens.EndOfLine);
		tokenIncrement++;
		return tokens;
	}

	/**
	 * Adds the current word stored in the word state variable 'word'
	 * Checking against reserved words decides whether the appropriate token is an identifier or something more specific 
	 * @param word The word being added to tokens
	 */
	public void addWord(String word) {
		if (reservedWords.containsKey(word)) {
			tokens[tokenIncrement] = new Token(reservedWords.get(word));
			tokenIncrement++;
		}
		else {
			tokens[tokenIncrement] = new Token(word, Tokens.IDENTIFIER);
			tokenIncrement++;
		}
	}
	
	/**
	 * Determines which symbol currentChar is. 
	 * All of these symbols result in the same behavior, so the method was created to avoid repeating the same if statements
	 * @param currentChar Current character in the line
	 */
	public void determineSymbol(char currentChar) {
		switch (currentChar) {
		case ';':
			tokens[tokenIncrement] = new Token (reservedWords.get(";"));
			tokenIncrement++;
			break;
		case ':':
			tokens[tokenIncrement] = new Token (reservedWords.get(":"));
			tokenIncrement++;
			break;
		case '=':
			tokens[tokenIncrement] = new Token (reservedWords.get("="));
			tokenIncrement++;
			break;
		case ',':
			tokens[tokenIncrement] = new Token (reservedWords.get(","));
			tokenIncrement++;
			break;
		default:
			break;
		}
	}
	
	/**
	 * Adds all reserved words to the prexisting reservedWords HashMap 
	 */
	private void addReservedWords () {
		reservedWords.put("define", Tokens.DEFINE);
		reservedWords.put("integer", Tokens.INTEGER);
		reservedWords.put("real", Tokens.REAL);
		reservedWords.put("begin", Tokens.BEGIN);
		reservedWords.put("end", Tokens.END);
		reservedWords.put(";", Tokens.SEMICOLON);
		reservedWords.put(":", Tokens.COLON);
		reservedWords.put("=", Tokens.EQUAL);
		reservedWords.put("!=", Tokens.NOTEQUAL);
		reservedWords.put(",", Tokens.COMMA);
		reservedWords.put("variables", Tokens.VARIABLES);
		reservedWords.put("constants", Tokens.CONSTANTS);
		
		// Assignment 5 reserved words
		
		reservedWords.put("if", Tokens.IF);
		reservedWords.put("then", Tokens.THEN);
		reservedWords.put("else", Tokens.ELSE);
		reservedWords.put("elsif", Tokens.ELSIF);
		reservedWords.put("for", Tokens.FOR);
		reservedWords.put("from", Tokens.FROM);
		reservedWords.put("to", Tokens.TO);
		reservedWords.put("while", Tokens.WHILE);
		reservedWords.put("repeat", Tokens.REPEAT);
		reservedWords.put("until", Tokens.UNTIL);
		reservedWords.put("mod", Tokens.MOD);
		
		// Assignment 6 reserved words
		
		reservedWords.put("var", Tokens.VAR);
		reservedWords.put("read", Tokens.READ);
		reservedWords.put("write", Tokens.WRITE);
		reservedWords.put("squareRoot", Tokens.SQUAREROOT);
		reservedWords.put("getRandom", Tokens.GETRANDOM);
		reservedWords.put("integerToReal", Tokens.INTEGERTOREAL);
		reservedWords.put("realToInteger", Tokens.REALTOINTEGER);
		
		// Assignment 9 reserved words
		
		reservedWords.put("true", Tokens.TRUE);
		reservedWords.put("false", Tokens.FALSE);
		reservedWords.put("bool", Tokens.BOOL);
		reservedWords.put("string", Tokens.STRING);
		reservedWords.put("char", Tokens.CHAR);
	}
	
	/**
	 * Gets the size of the tokens[] array because the inputted tokens don't affect the size
	 * The size is set at 100
	 * @return The size of the tokens[] array
	 */
	public int getTokenIncrement () {
		return this.tokenIncrement;
	}
}