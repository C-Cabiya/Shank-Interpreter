package assignmentTwo;

import java.util.ArrayList;
import java.util.HashMap;

import assignmentOne.Token;
import assignmentOne.Token.Tokens;

public class Interpreter {

	HashMap <String, InterpreterDataType> variables = new HashMap <String, InterpreterDataType> ();
	
	/*
	 * Empty constructor just to instantiate the object
	 */
	public Interpreter (){
		
	}
	
	public void interpretFunction(CallableNode function, HashMap <String, FunctionNode> functions, ArrayList <InterpreterDataType> parameters) {
		System.out.println("interpretFunction acessed");
		
		for (int i =0; i<parameters.size(); i++) {
			if (function.parameters.size() != parameters.size()) {
				System.out.println("Interpreter 'interpretFunction' Exception: Parameters passed");
				throw new RuntimeException("Interpreter 'interpretFunction' Exception: Unsupported data type.");
			}
			variables.put(function.parameters.get(i).getName(), parameters.get(i));
		}
		
		if (function instanceof FunctionNode) {
			System.out.println("User-defined function code acessed");
			
			FunctionNode calledFunction = (FunctionNode) function; 
			
			for (int i = 0; i<calledFunction.getVariables().size(); i++) {
				VariableNode currentVariable = calledFunction.getVariables().get(i);
				
				if (currentVariable.getNodeValue() instanceof IntegerNode) {
					InterpreterDataType type = new IntegerDataType ((int)currentVariable.getValue());
					variables.put(currentVariable.getName(), type);
					System.out.println("Integer Variable: " +variables.get(currentVariable.getName()));
				}
			
				// If represented constant is a float
				else if (currentVariable.getNodeValue() instanceof FloatNode) {
					InterpreterDataType type = new FloatDataType (currentVariable.getValue());
					variables.put(currentVariable.getName(), type);
					System.out.println("Float Variable: " +variables.get(currentVariable.getName()));
				}
			
				// If represented constant is an invalid data type 
				else {
					System.out.println("Interpreter 'interpretFunction' Exception: Unsupported data type.");
					throw new RuntimeException("Interpreter 'interpretFunction' Exception: Unsupported data type.");
				}
			}
			for (int i = 0; i<calledFunction.getConstants().size(); i++) {
				VariableNode currentConstant = calledFunction.getConstants().get(i);
			
				System.out.println("Constant:" +currentConstant.toString());
				
				// If represented constant is an integer
				if (currentConstant.getNodeValue() instanceof IntegerNode) {
					InterpreterDataType type = new IntegerDataType ((int)currentConstant.getValue());
					variables.put(currentConstant.getName(), type);
				}
			
				// If represented constant is a float
				else if (currentConstant.getNodeValue() instanceof FloatNode) {
					InterpreterDataType type = new FloatDataType (currentConstant.getValue());
					variables.put(currentConstant.getName(), type);
				}
			
				// If represented constant is an invalid data type 
				else {
					System.out.println("Interpreter 'interpretFunction' Exception: Unsupported data type.");
					throw new RuntimeException("Interpreter 'interpretFunction' Exception: Unsupported data type.");
				}
			}
			
			interpretBlock(variables, functions, calledFunction.getStatements());
		}
	}
	
	public void interpretBlock(HashMap <String, InterpreterDataType> variables, HashMap<String, FunctionNode> functions, ArrayList <StatementNode> statements) {
		System.out.println("interpretBlock acessed.");
		 
		// Increments through all the statements in the given function.
		for (int i = 0; i<statements.size(); i++) {
			
			if (statements.get(i) instanceof FunctionCallNode) {
				FunctionCallNode functionCall = (FunctionCallNode) statements.get(i);
				
				if (functions.containsKey(functionCall.getName())) {
					System.out.println("Function is in program");
					
					ArrayList <InterpreterDataType> parameters = new ArrayList <InterpreterDataType>();
					FunctionNode function = functions.get(functionCall.getName());
					
					for (int k = 0; k<function.getParameters().size(); k++) {
						
						// If the parameter is an integer
						if (function.getParameters().get(k).getNodeValue() instanceof IntegerNode) {
							IntegerDataType intData = new IntegerDataType((int) function.getParameters().get(k).getNodeValue().getValue());
							parameters.add(intData);
						}
						
						// If the parameter is a float
						else if (function.getParameters().get(k).getNodeValue() instanceof FloatNode) {
							FloatDataType floatData = new FloatDataType(function.getParameters().get(k).getNodeValue().getValue());
							parameters.add(floatData);
						}
						
						// If the parameter is an invalid data type 
						else {
							System.out.println("Shank Exception: Invalid parameter data type.");
							throw new RuntimeException("Shank Exception: Invalid parameter data type.");
						}
					}
					
					interpretFunction(functions.get(functionCall.getName()), functions, parameters);
				}
				else {
					System.out.println("Interpreter 'interpretBlock' Exception: Function not in program.");
					throw new RuntimeException("Interpreter 'interpretBlock' Exception: Function not in program.");
				}
			}
			
			// If the current statement is an Assignment Statement
			else if (statements.get(i) instanceof AssignmentNode) {
				System.out.println("Assignment Statement Acessed");
				
				AssignmentNode assignment = (AssignmentNode) statements.get(i);
				
				// If the target of the assignment has been initialized
				if (variables.containsKey(assignment.getTarget().name)) {
					InterpreterDataType variableValue = variables.get(assignment.getTarget().name); //Holds value of the variable that corresponds to the target name in the assignment
					System.out.println("Assignment Expression: " +assignment.getExpression()); //Holds the expression half of the assignment
					float updateValue = resolve(assignment.getExpression()); //Resolves the value of the expression
					
					// If the variable was represented by a Integer in the hashmap
					if (variableValue instanceof IntegerDataType) {
						
						// Updates the variable's value in the hashmap with an IntegerDataType
						variables.put(assignment.getTarget().name, new IntegerDataType((int)updateValue));
					}
					
					// If the variable was represented by a FloatDataType in the hashmap
					else if (variableValue instanceof FloatDataType) {
						
						// Updates the variable's value in the hashmap with a FloatDataType
						variables.put(assignment.getTarget().name, new FloatDataType(updateValue));
					}
					
					// If the variable was not supported by a FloatDataType or IntegerDataType - an unsupported type.
					else {
						System.out.println("Interpreter 'interpretBlock' Exception: Unsupported data type.");
						throw new RuntimeException("Interpreter 'interpretBlock' Exception: Unsupported data type.");
					}
					
				}
				else {
					System.out.println("Interpreter 'interpretBlock' Exception: Variable not initialized.");
					throw new RuntimeException("Interpreter 'interpretBlock' Exception: Variable not initialized.");
				}
				
				System.out.println("Updated " +assignment.getTarget().name +" value: " +variables.get(assignment.getTarget().name).getValue());
				
			}
			
			// LOOPS/CONDITIONALS //
			
			// If the current statement is a While Statement
			else if (statements.get(i) instanceof WhileNode) {
				System.out.println("While Statement Acessed");
				
				WhileNode whileStatement = (WhileNode) statements.get(i);
				BooleanExpressionNode booleanExpression = whileStatement.getCondition();
				String booleanComparator = booleanExpression.getComparator().toString();
				
				if (booleanComparator.equals("GREATER")) {
					while (resolve(booleanExpression.getLeft())>resolve(booleanExpression.getRight())) {
						interpretBlock(variables, functions, whileStatement.getStatements());
					}
				}
				
				else if(booleanComparator.equals("LESS")) {
					while (resolve(booleanExpression.getLeftExpression())<resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, whileStatement.getStatements());
					}
				}
				
				else if(booleanComparator.equals("GREATERTHANEQUAL")) {
					while (resolve(booleanExpression.getLeftExpression())>=resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, whileStatement.getStatements());
					}
				}
				
				else if(booleanComparator.equals("LESSTHANEQUAL")) {
					while (resolve(booleanExpression.getLeftExpression())<=resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, whileStatement.getStatements());
					}
				}
				
				else if(booleanComparator.equals("NOTEQUAL")) {
					while (resolve(booleanExpression.getLeftExpression())!=resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, whileStatement.getStatements());
					}
				}
				
				System.out.println("While Resolve" +resolve(booleanExpression.getLeftExpression()));
				
			}
			
			
			// If the current Statement is a Repeat Statement
			else if (statements.get(i) instanceof RepeatNode) {
				System.out.println("Repeat Statement Accessed");
				
				RepeatNode repeatStatement = (RepeatNode) statements.get(i);
				BooleanExpressionNode booleanExpression = repeatStatement.getCondition();
				String booleanComparator = booleanExpression.getComparator().toString();
				
				interpretBlock(variables, functions, repeatStatement.getStatements());
				
				switch (booleanComparator) {
				case "GREATER":
					while(resolve(booleanExpression.getLeftExpression()) <= resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, repeatStatement.getStatements());
					}
					break;
				case "LESS":
					while(resolve(booleanExpression.getLeftExpression()) > resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, repeatStatement.getStatements());
					}
					break;
				case "GREATERTHANEQUAL":
					while(resolve(booleanExpression.getLeftExpression()) < resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, repeatStatement.getStatements());
					}
					break;
				case "LESSTHANEQUAL":
					while(resolve(booleanExpression.getLeftExpression()) > resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, repeatStatement.getStatements());
					}
					break;
				case "NOTEQUAL":
					while(resolve(booleanExpression.getLeftExpression()) == resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, repeatStatement.getStatements());
					}
					break;
				case "EQUAL":
					while(resolve(booleanExpression.getLeftExpression()) != resolve(booleanExpression.getRightExpression())) {
						interpretBlock(variables, functions, repeatStatement.getStatements());
					}
					break;
				default:
					System.out.println("Interpreter 'interpretBlock' Exception: Invalid comparator.");
					throw new RuntimeException("Interpreter 'interpretBlock' Exception: Invalid comparator.");
				}
			}
			
			// If the current statement is an if statement 
			else if (statements.get(i) instanceof IfNode) {
				System.out.println("If Statement Accessed");
				
				IfNode ifStatement = (IfNode) statements.get(i);
				BooleanExpressionNode booleanExpression = ifStatement.getCondition();
				String booleanComparator = booleanExpression.getComparator().toString();
				
				System.out.println("RESOLVEBOOLEAN: " +resolveBoolean(booleanExpression));
			}
	
			// BUILT IN FUNCTIONS (READ, WRITE, ETC //
			
			// If the current statement is a for statement 
			else if (statements.get(i) instanceof ReadNode) {
				System.out.println("Read Statement Accessed");
				
				ReadNode readStatement = (ReadNode) statements.get(i);
				ArrayList <InterpreterDataType> readValues = new ArrayList <InterpreterDataType>();
				
				for (int k = 0; k<readStatement.variables.size(); k++) {
					Node readValue = readStatement.variables.get(k);
					
					if (readValue instanceof VariableReferenceNode) {
						VariableReferenceNode readVariable = (VariableReferenceNode) readValue;
						String referencedVariable = readVariable.getName();
						
						if(variables.containsKey(referencedVariable)) {
							readValues.add(variables.get(referencedVariable));
							System.out.println("Key " +referencedVariable +" has Value " +variables.get(referencedVariable));
							variables.put(referencedVariable, readStatement.execute());
						}
						
						else {
							System.out.println("Interpreter 'interpretBlock' Exception: Variable not initialized.");
							throw new RuntimeException("Interpreter 'interpretBlock' Exception: Variable not initialized.");
						}
					}
				}
				
				System.out.println("Read Statement Completed");
			}
			
			// If the current statement is a write statement
			else if (statements.get(i) instanceof WriteNode) {
				System.out.println("Write Statement Accessed");
				
				WriteNode writeStatement = (WriteNode) statements.get(i);
				ArrayList <InterpreterDataType> writeValues = new ArrayList<InterpreterDataType> ();
				
				for (int k = 0; k<writeStatement.getVariables().size(); k++) {
					Node writeNode = writeStatement.getVariables().get(k); //Current variable being written
					
					// If the written value references a variable
					if (writeNode instanceof VariableReferenceNode) {
						VariableReferenceNode writeVariable = (VariableReferenceNode) writeNode;
						
						System.out.println("Variable Name: " +writeVariable.name +" Variable Value: " +variables.get(writeVariable.name).toString());
						
						// Makes sure the variable referenced in the write function has been initialized
						if (variables.containsKey(writeVariable.name)) {
							writeValues.add(variables.get(writeVariable.name));
						}
						else {
							System.out.println("Interpreter 'interpretBlock' Exception: Variable not initialized.");
							throw new RuntimeException("Interpreter 'interpretBlock' Exception: Variable not initialized.");
						}
					}
					
					// If the written value is just an integer or float
					else if(writeNode instanceof IntegerNode || writeNode instanceof FloatNode) {
						//!! I get this is empty rn but important to keep
					}
					
					// If the value to be written is not a Variable Reference or a Nmber
					else {
						System.out.println("Interpreter 'interpretBlock' Exception: Invalid Data Type.");
						throw new RuntimeException("Interpreter 'interpretBlock' Exception: Invalid Data Type.");
					}
				}
				System.out.println("WRITE");
				writeStatement.execute(writeValues);
			}
			
			else if (statements.get(i) instanceof SqrtNode) {
				System.out.println("Square Root Statement Accessed");
				
				SqrtNode sqrtStatement = (SqrtNode) statements.get(i);
				String referencedVariable = sqrtStatement.getVariable().getName();
				
				if (variables.containsKey(referencedVariable)) {
					InterpreterDataType root = sqrtStatement.execute();
					System.out.println("Variable: " +referencedVariable +" Root: " +root);
					variables.put(referencedVariable, root);
				}
				else {
					System.out.println("Interpreter 'interpretBlock' Exception: Variable not initialized.");
					throw new RuntimeException("Interpreter 'interpretBlock' Exception: Variable not initialized.");
				}
			}
			
		}
	}
	
	public boolean resolveBoolean(BooleanExpressionNode booleanExpression) {
		String comparator = booleanExpression.getComparator().toString();
		boolean returnBoolean = false;
		
		switch (comparator) {
		case "GREATER":
			if (resolve(booleanExpression.getLeftExpression()) > resolve(booleanExpression.getRightExpression())) {
				returnBoolean = true;
			}
			break;
		case "LESS":
			if (resolve(booleanExpression.getLeftExpression()) < resolve(booleanExpression.getRightExpression())) {
				returnBoolean = true;
			}
			break;
		case "GREATERTHANEQUAL":
			if (resolve(booleanExpression.getLeftExpression()) >= resolve(booleanExpression.getRightExpression())) {
				returnBoolean = true;
			}
			break;
		case "LESSTHANEQUAL":
			if (resolve(booleanExpression.getLeftExpression()) <= resolve(booleanExpression.getRightExpression())) {
				returnBoolean = true;
			}
			break;
		case "NOTEQUAL":
			if (resolve(booleanExpression.getLeftExpression()) != resolve(booleanExpression.getRightExpression())) {
				returnBoolean = true;
			}
			break;
		case "EQUAL":
			if (resolve(booleanExpression.getLeftExpression()) == resolve(booleanExpression.getRightExpression())) {
				returnBoolean = true;
			}
			break;
		default:
			System.out.println("Interpreter 'resolveBoolean' Exception: Invalid comparator.");
			throw new RuntimeException("Interpreter 'resolveBoolean' Exception: Invalid comparator.");	
		}
		
		return returnBoolean;
	}
	
	/**
	 * Interprets the math expression
	 * @param head The head of the AST output by the parser
	 * @return Returns the interpreted result of the mathematical expression as a float
	 */
	public float resolve(Node head) {
		System.out.println("Resolve acessed");
		System.out.println("HEAD " +head.toString());
		
		// Calls itself recursively until a NUMBER token is encountered
		if (head instanceof IntegerNode || head instanceof FloatNode) {
			return head.getValue();
		}
		
		// If the current node is an identifier 
		else if (head instanceof VariableReferenceNode) {
			VariableReferenceNode variable = (VariableReferenceNode) head; //Referenced variable
			
			// If the referenced variable has been initialized in the HashMap
			if (variables.containsKey(variable.getName())) {
				InterpreterDataType value = variables.get(variable.getName()); //Value represented by variable in the table
				
				// If the variable was initialized as an integer 
				if (value instanceof IntegerDataType) {
					return (int) value.getValue();
				}
				
				// If the variable was initialized as a float
				else if(value instanceof FloatDataType) {
					return value.getValue();
				}
				
				// If the variable was somehow not initialized as a float or an integer
				else {
					System.out.println("Interpreter 'resolve' Exception: Invalid data type");
					throw new RuntimeException("Interpreter 'resolve' Exception: Invalid data type");
				}
			}
			
			// If the referenced variable has NOT been initialized in the HashMap
			else {
				System.out.println("Interpreter 'resolve' Exception: Variable not initialized.");
				throw new RuntimeException("Interpreter 'resolve' Exception: Variable not initialized.");
			}
		}
		
		
		else {
			float operandOne = resolve(head.getLeft()); 
			float operandTwo = resolve(head.getRight());
			float result = 0;
			String operatorType = head.getToken().getTokenType();
			switch (operatorType) {
			case "PLUS":
				result = operandOne + operandTwo;
				break;
			case "MINUS":
				result = operandOne - operandTwo;
				break;
			case "TIMES":
				result = operandOne * operandTwo;
				break;
			case "DIVIDE":
				result = operandOne / operandTwo;
				break;
			default:
				System.out.println("Interpreter 'resolve' Exception: Unexpected token.");
				throw new RuntimeException("Interpreter 'resolve' Exception: Unexpected token.");
			}
			return result;
		}
	}
	
}
