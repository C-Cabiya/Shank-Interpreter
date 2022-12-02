package assignmentTwo;

import java.util.ArrayList;

public class GetRandomNode extends BuiltInFunctionNode{
	ArrayList <VariableReferenceNode> variables;
	
	public GetRandomNode(ArrayList <VariableReferenceNode> variables) {
		this.variables = variables;
	}
	
	protected void execute(ArrayList<InterpreterDataType> items) {
		for(int i = 0; i<items.size(); i++) {
			items.set(i, new FloatDataType((float)Math.random()));
		}
	}

	@Override
	public String toString() {
		String returnString = "GET RANDOM NODE: PARAMETERS: (";
		for (int i = 0; i<variables.size(); i++) {
			returnString = returnString + " " +variables.get(i).toString();
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
