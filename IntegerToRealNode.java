package assignmentTwo;

import java.util.ArrayList;

import assignmentOne.Token;

public class IntegerToRealNode extends BuiltInFunctionNode{
	
	ArrayList <VariableReferenceNode> variables;
	
	public IntegerToRealNode(ArrayList <VariableReferenceNode> variables) {
		this.variables = variables;
	}
	
	protected void execute(ArrayList<InterpreterDataType> items) {
		for(int i = 0; i<items.size(); i=i+2) {
			items.set(i, new FloatDataType((float)items.get(i).getValue()));
		}
	}

	@Override
	public String toString() {
		String returnString = " INTEGER TO REAL NODE: PARAMETERS: (";
		for (int i = 0; i<variables.size(); i=i+2) {
			returnString = returnString + "INTEGER: (" +variables.get(i).toString() +") TO VARIABLE: (" +variables.get(i).toString();
		}
		returnString = returnString + ")";
		return returnString;
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
