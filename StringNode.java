package assignmentTwo;

import assignmentOne.Token;
import assignmentOne.Token.Tokens;

public class StringNode extends Node{
	
	private Token token;
	private String content;
	
	StringNode(){
		
	}
	
	StringNode(String content){
		this.content = content;
		this.token = new Token(Tokens.STRINGCONTENTS);
	}
	
	StringNode(Token token, String content){
		this.content = content;
		this.token = token;
	}

	@Override
	public String toString() {
		return "STRING NODE: CONTENT: (" +content +")";
	}

	@Override
	public Token getToken() {
		return token;
	}
	
	public String getContent () {
		return content;
	}
	
	// UNUSED INHERITED METHODS //

	@Override
	public Node getRight() {
		return null;
	}

	@Override
	public Node getLeft() {
		return null;
	}

	@Override
	public float getValue() {
		return 0;
	}
	
}
