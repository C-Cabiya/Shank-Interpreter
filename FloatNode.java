package assignmentTwo;

import assignmentOne.Token;

public class FloatNode extends Node{
	
	private Token token;
	private float value;
	
	FloatNode(Token input, float value) {
		this.token = input;
		this.value = value;
	}
	
	public float getValue() {
		return value;
	}
	
	public String toString() {
		return "" + token;
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
