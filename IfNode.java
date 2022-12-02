package assignmentTwo;

import java.util.ArrayList;

public class IfNode extends StatementNode{

	private ArrayList<StatementNode> statements;
	private BooleanExpressionNode condition;
	private IfNode next; 
	
	IfNode(){
		
	}
	
	// Has a condition and next if, an 'if' or 'else if' that is not the last in the chain
	IfNode(ArrayList<StatementNode> statements, BooleanExpressionNode condition, IfNode next){
		this.statements = statements; 
		this.condition = condition;
		this.next = next;
	}
	
	// No condition or next if, for 'else' statements 
	IfNode(ArrayList<StatementNode> statements){
		this.statements = statements;
		this.condition = null;
		this.next = null;
	}
	
	// No next Node, the last 'else if' of a series
	IfNode(ArrayList<StatementNode> statements, BooleanExpressionNode condition){
		this.statements = statements;
		this.condition = condition;
		this.next = null;
	}
	
	public void add(IfNode next) {
		this.next = next;
	}
	
	public BooleanExpressionNode getCondition() {
		return condition;
	}
	
	public ArrayList<StatementNode> getStatements() {
		return statements;
	}
	
	public IfNode getNext() {
		return next;
	}
	
	@Override
	public String toString() {
		String returnString;
		
		if (this.condition == null) {
			returnString = "\n IFNODE ELSE \n STATEMENTS: ";
		}
		else {
			returnString = "\n IFNODE CONDITION: (" +condition.toString() +") \n STATEMENTS: ";
		}
	
		for (int i = 0; i<statements.size(); i++) {
			returnString = returnString +"\n " +statements.get(i).toString();
		}
		if (this.next == null) {
			return returnString;
		}
		else {
			return returnString + next.toString();
		}
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
