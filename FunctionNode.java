package assignmentTwo;

import java.util.ArrayList;

import assignmentOne.Token;

public class FunctionNode extends CallableNode{
	
	// Has ArrayList <VariableNode> parameters and String name from Callable
	
	private ArrayList <VariableNode> constants;
	private ArrayList <VariableNode> variables;
	private ArrayList <StatementNode> statements;
	
	/*
	 * Constructor with just a name
	 */
	public FunctionNode(String name) {
		this.name = name;
	}
	
	// Mutators to prevent a series of constructors
	public void setParameters(ArrayList <VariableNode> parameters) {
		this.parameters = parameters;
	}
	
	public void setConstants(ArrayList <VariableNode> constants) {
		this.constants = constants;
	}
	
	public void setVariables(ArrayList <VariableNode> variables) {
		this.variables = variables;
	}
	
	public void setStatements(ArrayList <StatementNode> statements) {
		this.statements = statements;
	}
	
	// Acessors 
	public ArrayList <VariableNode> getParameters(){
		return parameters;
	}
	
	public ArrayList <VariableNode> getConstants(){
		return constants;
	}
	
	public ArrayList <VariableNode> getVariables(){
		return variables;
	}
	
	public ArrayList <StatementNode> getStatements(){
		return statements;
	}
	
	public String getName() {
		return name; 
	}

	@Override
	public String toString() {
		String toString = "";
		toString = toString +"Function Name: " +name;
		for (int i = 0; i<parameters.size(); i++) {
			toString = toString + "\n Parameters: (" +parameters.get(i).toString() +")";
		}
		for (int i = 0; i<constants.size(); i++) {
			toString = toString + "\n Constants: (" +constants.get(i).toString() +")";
		}
		for (int i = 0; i<variables.size(); i++) {
			toString = toString + "\n Variables: (" +variables.get(i).toString() +")";
		}
		for (int i = 0; i<statements.size(); i++) {
			if (statements.get(i) != null) {
				toString = toString + "\n Function statements: (" +statements.get(i).toString() +")";
			}
		}
		return toString;
	}
	
	// Inherited methods that don't apply here

	@Override
	public Token getToken() {
		return null;
	}

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
