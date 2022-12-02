package assignmentTwo;

import assignmentOne.Token;

public class BooleanExpressionNode extends Node{
		
		private Node leftExpression;
		private Node rightExpression;
		
		private Token comparator;
		
		// Empty constructor
		public BooleanExpressionNode () {
			
		}
		
		public BooleanExpressionNode(Token comparator, Node leftExpression, Node rightExpression) {
			this.comparator = comparator;
			this.leftExpression = leftExpression;
			this.rightExpression = rightExpression;
		}
		
		public Node getLeftExpression() {
			return leftExpression;
		}
		
		public Node getRightExpression() {
			return rightExpression;
		}
		
		public Token getComparator() {
			return comparator;
		}
		
		public String toString() {
			return "(BOOLEAN EXPRESSION: " +leftExpression.toString() +" " +comparator.toString() +" " +rightExpression.toString() +")";
		}
		
		@Override
		public Token getToken() {
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
