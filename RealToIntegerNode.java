package assignmentTwo;

import java.util.ArrayList;

public class RealToIntegerNode extends BuiltInFunctionNode{
ArrayList <VariableReferenceNode> variables;
	
	public RealToIntegerNode(ArrayList <VariableReferenceNode> variables) {
		this.variables = variables;
	}
	
	protected void execute(ArrayList<InterpreterDataType> items) {
		for(int i = 0; i<items.size(); i++) {
			items.set(i, new FloatDataType((int)items.get(i).getValue()));
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
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
