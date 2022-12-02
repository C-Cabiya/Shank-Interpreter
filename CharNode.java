package assignmentTwo;

import assignmentOne.Token;
import assignmentOne.Token.Tokens;

public class CharNode extends Node{
	
	private Token token;
	private char content;
	
	CharNode(){
		
	}
	
	CharNode(char content){
		this.content = content;
		this.token = new Token(Tokens.CHARCONTENTS);
	}

	@Override
	public String toString() {
		return "CHARACTER NODE: CONTENT: (" +content +")";
	}

	@Override
	public Token getToken() {
		return token;
	}
	
	public char getContent () {
		return content;
	}

	// UNUSED INHERITED METHODS //
	
	@Override
	public Node getRight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getValue() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
