package assignmentTwo;

import assignmentOne.Token;

public class VariableReferenceNode extends Node {
	
	String name;
	VariableNode referencedVariable;
	
	public VariableReferenceNode () {
		
	}
	
	public VariableReferenceNode (String name) {
		this.name = name;
	}
	
	public VariableReferenceNode (String name, VariableNode referencedVariable) {
		this.name = name;
		this.referencedVariable = referencedVariable;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "VARIABLE REFERENCE: " +name;
	}
	@Override
	public Token getToken() {
		return null;
	}
	@Override
	public Node getLeft() {
		return null;
	}
	@Override
	public Node getRight() {
		return null;
	}
	@Override
	public float getValue() {
		return 0;
	}
	
}
