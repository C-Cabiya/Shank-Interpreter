package assignmentTwo;

import java.util.ArrayList;

public class SqrtNode extends BuiltInFunctionNode{
	
	float value;
	VariableReferenceNode variable;
	
	public SqrtNode (float value, VariableReferenceNode variable) {
		this.value = value;
		this.variable = variable;
	}
	
	protected InterpreterDataType execute() {
		InterpreterDataType returnData = new IntegerDataType();
		
		float root = (float) Math.sqrt(this.value);
		
		if (root % 1 == 0) {
			returnData = new IntegerDataType((int)root);
		}
		else {
			returnData = new FloatDataType(root);
		}
		
		return returnData;
	}
	
	public VariableReferenceNode getVariable() {
		return variable;
	}
	
	@Override
	public float getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "SQRT NODE: FLOAT: (" +value +") REFERENCED VARIABLE: (" +variable;
	}

	// UNUSED BUT EXTENDED METHODS //
	
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
	protected void execute(ArrayList<InterpreterDataType> items) {
		// TODO Auto-generated method stub
		
	}
}
