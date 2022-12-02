package assignmentTwo;

import java.util.ArrayList;

import assignmentOne.Token;

public abstract class CallableNode extends StatementNode{
	
	protected String name;
	protected ArrayList <VariableNode> parameters;
	
	public  abstract String toString();
}
