package assignmentTwo;

import assignmentOne.Token;

public abstract class Node {
	
	public abstract String toString();
	
	public abstract Token getToken();
	
	public abstract Node getRight();
	public abstract Node getLeft();
	public abstract float getValue();
	
}
