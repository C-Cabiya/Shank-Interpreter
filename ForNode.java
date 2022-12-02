package assignmentTwo;

import java.util.ArrayList;

public class ForNode extends StatementNode{

	private ArrayList<StatementNode> statements;
	private VariableReferenceNode variable; 
	private Node start;
	private Node end;
	
	public ForNode() {
		
	}
	
	public ForNode(ArrayList <StatementNode> statements, VariableReferenceNode variable, Node start, Node end) {
		this.statements = statements;
		this.variable = variable;
		this.start = start;
		this.end = end; 
	}
	
	@Override
	public String toString() {
		String returnString = "FORNODE IDENTIFIER: (" +variable.toString() +") START: (" +start.toString() +") END: (" +end.toString() +") \nSTATEMENTS";
		for (int i = 0; i<statements.size(); i++) {
			returnString = returnString + "\n" +statements.get(i).toString();
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
