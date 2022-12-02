package assignmentTwo;

import java.util.ArrayList;

import assignmentOne.Token;

public  abstract class BuiltInFunctionNode extends CallableNode {

	// Has ArrayList <VariableNode> parameters and String name from Callable
	
	boolean isVariadic;
	
	protected abstract void execute (ArrayList<InterpreterDataType> items);
	
	public BuiltInFunctionNode() {
		
	}
	
	public BuiltInFunctionNode(String name) {
		this.name = name;
		this.parameters = null;
	}
	
	public BuiltInFunctionNode(String name, ArrayList<VariableNode> parameters) {
		this.name = name;
		this.parameters = parameters;
	}
	
	@Override
	public String toString() {
		return null;
	}

	@Override
	public Token getToken() {
		return null;
	}

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
