package assignmentOne;

public class Token {
	
	private String value; 
	private String tokenType;
	private String identifier;
	private char character;
	
	private double number;
	
	/*
	 * Enum describing the types of tokens possible (Operations and numbers for as of  8/29)
	 */
	public enum Tokens{
		NUMBER,
		PLUS,
		MINUS,
		TIMES,
		DIVIDE,
		LPAREN,
		RPAREN,
		
		// Beginning of Assignment 3 Tokens
		
		IDENTIFIER,
		DEFINE,
		INTEGER,
		REAL,
		BEGIN,
		END,
		SEMICOLON,
		COLON,
		EQUAL,
		COMMA,
		VARIABLES,
		CONSTANTS,
		
		// Assignment 4 Token
		
		ASSIGN,
		
		// Assignment 5 Tokens
		
		IF,
		THEN,
		ELSE,
		ELSIF,
		FOR,
		FROM,
		TO,
		WHILE,
		REPEAT,
		UNTIL,
		MOD,
		GREATER,
		LESS,
		GREATERTHANEQUAL,
		LESSTHANEQUAL,
		NOTEQUAL,
		
		// Assignment 6 Tokens
		
		VAR,
		READ,
		WRITE,
		SQUAREROOT,
		GETRANDOM,
		INTEGERTOREAL,
		REALTOINTEGER,
		
		// Assignment 9 Tokens
		
		TRUE,
		FALSE,
		STRING, //String DATA TYPE
		CHAR, //Char DATA TYPE
		STRINGCONTENTS, //String CONTENT (ie. "content within these quotes")
		CHARCONTENTS, //Char CONTENT (ie. 'A')
		BOOL,
		EndOfLine;
	}	
	
	public Token() {
		
	}
	
	/**
	 * Token initializer for operators, parentheses, and ends of lines
	 * @param specifier Specifies the kind of token we want.
	 */
	public Token (Tokens specifier) {
		this.value = specifier.toString();
		this.tokenType = specifier.toString();
	}
	
	/**
	 * Token initializer for NUMBER token, which require more formatting 
	 * @param specifier Specifies the token, which, for now, should only be NUMBER
	 * @param number The value we want attatched to the NUMBER token
	 */
	public Token (Tokens specifier, String number) {
		this.value = specifier + "(" +number +")";
		this.number = Double.parseDouble(number);
		this.tokenType = specifier.toString();
	}
	
	/**
	 * Constuctor for IDENTIFIER, STRING, and CHAR Tokens
	 * @param word The identifier, string, or char itself
	 * @param specifier Specifies the Token, assumed to be an IDENTIFIER, STRING, or CHAR
	 */
	public Token (String word, Tokens specifier) {
		this.value = specifier +"(" +word +")";
		this.identifier = word;
		this.tokenType = specifier.toString();
	}
	
	/**
	 * Converts a token into a value
	 */
	public String toString () {
		return value;
	}
	
	public String getTokenType() {
		return tokenType;
	}
	
	/*
	 * Compares this token type to another
	 * DOES NOT compare values in Tokens with specifiers like that (ie. IDENTIFIER and NUMBER)
	 * eg. would return true for NUMBER(3) and NUMBER(10)
	 */
	public boolean compareTo(Token input) {
		if (input.tokenType.equals(this.tokenType)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public char getCharacter() {
		return character;
	}
	
	public double getValue() {
		return number;
	}
	
	public String getIdentifier() {
		 return identifier;
	}
}
