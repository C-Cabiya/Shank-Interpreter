package assignmentTwo;

import assignmentOne.Token;

public class MathOpNode extends Node{
	
	private Token token;
	
	private Node left;
	private Node right;
	
	// Empty constructor to reate empty Nodes
	MathOpNode(){
		
	}
	
	// More frequently used, parameterized constructor
	MathOpNode(Token token, Node left, Node right){
		this.token = token;
		this.left = left;
		this.right = right;
	}
	
	
	public enum operations{
		PLUS,
		MINUS,
		TIMES,
		DIVIDE,
		
		// Assignment 5 update
		
		MOD;
	}
	
	public Node getLeft() {
		return left;
	}
	
	public Node getRight() {
		return right;
	}
	
	/*
	 * Formatted toString to showcase behavior of a tree, showcasing its left and right relationships
	 */
	@Override
	public String toString() {
		return "(MATHOPNODE OPERATION: (" +token.toString() +") Left: (" +this.getLeft().toString() +") Right: (" +this.getRight().toString()+")";
	}

	@Override
	public Token getToken() {
		return token;
	}

	@Override
	public float getValue() {
		return 0;
	}
	
}
