package assignmentTwo;

import assignmentOne.Token;

public class VariableNode extends Node{
	
	private String name;
	private boolean isConstant;
	private String type;
	private Node value;
	
	public enum types{
		INTEGER,
		REAL,
		STRING,
		CHAR,
		BOOL;
	}
	
	// Empty constructor
	public VariableNode() {
		
	}
	
	// Constructor where String is variable name and Node is the value of variable
	public VariableNode	(String variableName, Node value, boolean isConstant, types type) {
		this.value = value; 
		this.isConstant = isConstant;
		this.type = type.toString();
		this.name = variableName;
	}
	
	public String getName () {
		return name;
	}
	
	public Node getNodeValue () {
		return value;
	}
	
	public boolean getConstant() {
		return isConstant;
	}
	
	@Override
	public String toString() {
		return ("NAME: " + name + " NODE: " +value.toString() +" TYPE: " +type.toString() +" IS CONSTANT: " +isConstant);
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
		return value.getValue();
	}

}
