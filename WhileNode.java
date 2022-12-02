package assignmentTwo;

import java.util.ArrayList;

public class WhileNode extends StatementNode{

	private BooleanExpressionNode condition;
	
	private ArrayList<StatementNode> statements = new ArrayList <StatementNode> ();
	
	public WhileNode() {
		
	}
	
	public WhileNode(BooleanExpressionNode condition, ArrayList<StatementNode> statements) {
		this.condition = condition;
		this.statements = statements;
	}
	
	@Override
	public String toString() {
		String returnString = "(WHILE CONDITION: (" +condition +") \n STATEMENTS: ";
		for (int i = 0; i < statements.size(); i++) {
			returnString = returnString + "\n  " +statements.get(i).toString();
		}
		returnString = returnString +")";
		return returnString;
	}
	
	public BooleanExpressionNode getCondition () {
		return condition;
	}
	
	public ArrayList<StatementNode> getStatements(){
		return statements;
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
