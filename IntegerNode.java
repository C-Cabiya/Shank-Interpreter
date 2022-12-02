package assignmentTwo;

import assignmentOne.Token;

public class IntegerNode extends Node{
	
	private Token token;
	private float value;
	
	
	IntegerNode(Token input, int value) {
		this.token = input;
		this.value = value;
	}
	
	public float getValue() {
		return value;
	}
	
	public String toString() {
		return "INTEGER NODE: " +token.toString();
	}
	
	public Token getToken() {
		return token;
	}
	
	public Node getLeft() {
		return null;
	}

	public Node getRight() {
		return null;
	}
}
