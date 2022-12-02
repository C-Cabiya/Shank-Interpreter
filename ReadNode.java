package assignmentTwo;

import java.util.ArrayList;
import java.util.Scanner;

public class ReadNode extends BuiltInFunctionNode{

	ArrayList <VariableReferenceNode> variables;
	
	public ReadNode (ArrayList <VariableReferenceNode> variables) {
		this.variables = variables;
	}
	
	public String toString() {
		String returnString = "READ NODE: PARAMETERS: (";
		for (int i = 0; i<variables.size(); i++) {
			returnString = returnString + " " +variables.get(i).toString();
		}
		returnString = returnString +")";
		return returnString;
	}
	
	protected InterpreterDataType execute() {
		System.out.println("READ EXECUTE ACESSED");
		
		Scanner sc = new Scanner(System.in);
		float number = sc.nextFloat();
		
		if (number % 1 == 0) {
			return new IntegerDataType((int)number);
		}
		else {
			return new FloatDataType(number);
		}
	}
	
	public ArrayList<VariableReferenceNode> getVariables(){
		return variables;
	}

	@Override
	protected void execute(ArrayList<InterpreterDataType> items) {
		// TODO Auto-generated method stub
		
	}

}
