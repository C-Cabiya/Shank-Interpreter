package assignmentTwo;

import java.util.ArrayList;
import java.util.Scanner;

public class WriteNode extends BuiltInFunctionNode{
	
	ArrayList <VariableReferenceNode> variables;
	
	public WriteNode(ArrayList <VariableReferenceNode> variables) {
		this.variables = variables;
	}
	
	public String toString() {
		String returnString = "WRITE NODE: PARAMETERS: (";
		for (int i = 0; i<variables.size(); i++) {
			returnString = returnString + " " +variables.get(i).toString();
		}
		returnString = returnString +")";
		return returnString;
	}

	protected void execute(ArrayList<InterpreterDataType> items) {
		for(int i = 0; i<items.size(); i++) {
			System.out.print(items.get(i).getValue() +" ");
		}
		System.out.println();
	}
	
	public ArrayList<VariableReferenceNode> getVariables(){
		return variables;
	}
}
