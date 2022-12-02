package assignmentTwo;

import assignmentOne.Token;

public abstract class StatementNode extends Node{

	public abstract String toString();

	public Token getToken() {
		return null;
	}

	@Override
	public abstract Node getRight();

	@Override
	public abstract Node getLeft();

	@Override
	public abstract float getValue();
	
}
