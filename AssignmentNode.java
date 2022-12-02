package assignmentTwo;

public class AssignmentNode extends StatementNode{
	
	private VariableReferenceNode target;
	private Node expression;
	
	public AssignmentNode() {
		
	}
	
	public AssignmentNode(VariableReferenceNode target, Node expression) {
		this.target = target;
		this.expression = expression;
	}
	
	public VariableReferenceNode getTarget() {
		return target;
	}
	
	public void setValue(Node updateValue) {
		expression = updateValue;
	}
	
	public Node getExpression () {
		return expression;
	}
	
	@Override
	public String toString() {
		return "ASSIGNMENT NODE NAME: " +target.name + " EXPRESSION: " +expression.toString() +"";
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
