package assignmentTwo;

import java.util.ArrayList;

public class RepeatNode extends StatementNode{

	private ArrayList <StatementNode> statements;
	private BooleanExpressionNode condition;
	
	public RepeatNode () {
		
	}
	
	public RepeatNode (BooleanExpressionNode condition, ArrayList <StatementNode> statements) {
		this.statements = statements;
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		String returnString = "(REPEATNODE: CONDITION: (" +condition.toString() +") \n STATEMENTS:";
		for (int i = 0; i<statements.size(); i++) {
			returnString = returnString +"\n "+statements.get(i).toString();
		}
		returnString = returnString + ")";
		return returnString;
	}
	
	public BooleanExpressionNode getCondition() {
		return condition;
	}
	
	public ArrayList<StatementNode> getStatements() {
		return statements;
	}

	// UNUSED FUNCTIONS INHERETED FROM NODE CLASS //
	
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
