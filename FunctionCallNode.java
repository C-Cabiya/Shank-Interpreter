package assignmentTwo;

import java.util.ArrayList;

public class FunctionCallNode extends CallableNode{
	
	private String functionName;
	private ArrayList <Node> parameters;
	
	public FunctionCallNode() {
		
	}
	
	public FunctionCallNode(String functionName) {
		this.functionName = functionName;
		parameters = null;
	}
	
	public FunctionCallNode(String functionName, ArrayList <Node> parameters) {
		this.functionName = functionName;
		this.parameters = parameters;
	}
	
	public String getName() {
		return functionName;
	}

	@Override
	public String toString() {
		String returnString = "FUNCTION CALL FUNCTION:" +this.functionName;
		if (parameters != null) {
			for (int i = 0; i<parameters.size(); i++){
				returnString = returnString + "\n  " + parameters.get(i).toString();
			}	
		}
		return returnString;
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
