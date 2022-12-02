package assignmentTwo;

import java.util.ArrayList;
import java.lang.Exception;
import assignmentOne.Shank;
import assignmentOne.Token;
import assignmentOne.Token.Tokens;
import assignmentTwo.VariableNode.types;

public class Parser {
	
	private ArrayList<Token> tokens; //All tokens in the program
	
	private Node head = new MathOpNode(); //Head of the ast representing a program
	
	public Parser(ArrayList<Token> tokens){
		this.tokens = tokens;
	}
	
	public FunctionNode parse() {
		FunctionNode functionNode = functionDefinition();
		return functionNode;
	}
	
	// Determines whether or not the program represented by the tokens is completed or not; if all tokens have been "matchAndRemove"-ed or not
	public boolean tokensIsEmpty() {
		return tokens.isEmpty();
	}
	
	/**
	 * Handles the definition of a function (Header, constants, variables, body)
	 * Expected tokens: DEFINE IDENTIFIER LPAREN (Maybe parameters) RPAREN EndOfLine (Maybe CONSTANTS) (Maybe PARAMETERS) BEGIN END
	 * @return Returns a FunctionAST node containing arraylists for parameters, constants, and variables
	 */
	private FunctionNode functionDefinition() throws RuntimeException{
		ArrayList <VariableNode> parameterNodes = new ArrayList <VariableNode> ();
		ArrayList <VariableNode> constantNodes = new ArrayList <VariableNode> ();
		ArrayList <VariableNode> variableNodes = new ArrayList <VariableNode> ();
		ArrayList <StatementNode> statementNodes = new ArrayList <StatementNode> ();
		
		if (matchAndRemove("DEFINE") == null) {
			System.out.println("Parser Exception: Define expected.");
			throw new RuntimeException("Parser Exception: Define expected.");
		}
		
		Token functionNameIdentifier = matchAndRemove("IDENTIFIER");
		
		if (functionNameIdentifier == null) {
			System.out.println("Parser excpetion: No function name.");
			throw new RuntimeException("Parser excpetion: No function name.");
		}
		
		String functionName = functionNameIdentifier.getIdentifier();
		
		if (matchAndRemove("LPAREN") == null) {
			System.out.println("Parser exception: Left parenthesis expected.");
			throw new RuntimeException("Parser exception: Left parentheis expected.");
		}
		
		if (tokens.get(0).getTokenType() == "IDENTIFIER") {
			parameterNodes = parameters();
		}

		if (matchAndRemove("RPAREN") == null) {
			System.out.println("Parser Exception: Right parenthesis expected.");
			throw new RuntimeException("Parser Exception: Right parenthesis expected.");
		}

		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser Exception: End of line expected.");
			throw new RuntimeException("Parser Exception: End of line expected.");
		}
		
		/*
		 * If there is a constants section
		 */
		if (matchAndRemove("CONSTANTS") != null) {
			// Immediate next character should be EndofLine
			if (matchAndRemove("EndOfLine") == null) {
				System.out.println("Parser Exception: Constants should be in a line by itself.");
				throw new RuntimeException("Parser Exception: Constants should be in a line by itself.");
			}
			constantNodes.add(constants());
			// Including the "if is not empty" prevents an out of bounds error
			while (!(tokens.isEmpty()) && tokens.get(0).getTokenType().equals("IDENTIFIER")) {
				constantNodes.add(constants());
				}
		}
		
		/*
		 * If there is a variables section
		 */
		if (matchAndRemove("VARIABLES") != null) {
			if (matchAndRemove("EndOfLine") == null) {
				System.out.println("Parser Exception: Variables should be in a line by itself.");
				throw new RuntimeException("Parser Exception: Variables should be in a line by itself.");
			}
			
			ArrayList <VariableNode> variablesOneLine = variables();
			
			for (int i = 0; i<variablesOneLine.size(); i++) {
				variableNodes.add(variablesOneLine.get(i));
			}
			
			while (tokens.get(0).getTokenType().equals("IDENTIFIER")) {
				variablesOneLine = variables();
				
				for (int i = 0; i<variablesOneLine.size(); i++) {
					variableNodes.add(variablesOneLine.get(i));
				}
			}
		}
		
		
		statementNodes = bodyFunction();
		
		FunctionNode functionNode = new FunctionNode(functionName);
		
		if (parameterNodes != null) {
			functionNode.setParameters(parameterNodes);
		}
		
		if (constantNodes != null) {
			functionNode.setConstants(constantNodes);
		}
		
		if (variableNodes != null) {
			functionNode.setVariables(variableNodes);
		}
		
		if (statementNodes != null) {
			functionNode.setStatements(statementNodes);
		}
		
		System.out.println(functionNode.toString());
		
		return functionNode;
	}
	
	/**
	 * Checks for the begin and end statement of a function body
	 * Expects BEGIN, EndOfline, END, EndOfLine
	 */
	private ArrayList <StatementNode> bodyFunction () {
		System.out.println("Body Function Reached");
		
		ArrayList <StatementNode> statements = new ArrayList <StatementNode>();
		
		if (matchAndRemove("BEGIN") == null) {
			System.out.println("Parser Exception: Function body expected.");
			throw new RuntimeException("Parser Exception: Function body expected.");
		}
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser Exception: Begin should be in a line by itself.");
			throw new RuntimeException("Parser Exception: Begin should be in a line by itself.");
		}

		statements = statements();
		
		if (matchAndRemove("END") == null) {
			System.out.println("Parser Exception: Function body end expected.");
			throw new RuntimeException("Parser Exception: Function body end expected.");
		}
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser Exception: End should be in a line by itself.");
			throw new RuntimeException("Parser Exception: End should be in a line by itself.");
		}
		
		System.out.println("Body function ended");
		
		return statements;
		
	}
	
	/**
	 * Handles all statements in a body, like a function body, while body etc.
	 * @return returns a collection of statement nodes 
	 */
	private ArrayList <StatementNode> statements () {
		System.out.println("Statements Reached");
		
		ArrayList <StatementNode> statements = new ArrayList <StatementNode> ();
		
		// Will continue until the function body ends
		while (!peek().toString().equals("END")) {
			StatementNode statementNode = statement();
			statements.add(statementNode);
		}
		
		System.out.println("Statements ended");
		
		return statements;
	}
	
	/**
	 * Handles a single statement
	 * @return returns a signle statements node,
	 */
	private StatementNode statement() {
		System.out.println("Statement Reached");
		
		System.out.println(peek().toString());
		
		StatementNode returnNode = null;
		Token identifier = new Token();
		
		switch (peek().getTokenType().toString()) {
		case "IDENTIFIER":
			identifier = matchAndRemove("IDENTIFIER");
			
			// Runs as an assignment statement
			if (peek().getTokenType().equals("ASSIGN")) {
				returnNode = assignment(identifier);
			}
			
			// Runs as a function call statement with parameters
			else if (peek().getTokenType().equals("IDENTIFIER")||peek().getTokenType().equals("VAR")||peek().getTokenType().equals("NUMBER")) {
				returnNode = functionCall(identifier);
			}
			
			// Runs as function call statement without parameters
			else if (peek().getTokenType().equals("EndOfLine")) {
				matchAndRemove("EndOfLine");
				returnNode = new FunctionCallNode(identifier.getIdentifier(), null);
			}
			break;
		case "READ":
			returnNode = readFunction();
			System.out.println(returnNode.toString());
			break;
		case "WRITE":
			returnNode = writeFunction();
			System.out.println(returnNode.toString());
			break;
		case "SQUAREROOT":
			returnNode = sqrtFunction();
			System.out.println(returnNode.toString());
			break;
		case "GETRANDOM":
			returnNode = getRandomFunction();
			System.out.println(returnNode.toString());
			break;
		case "INTEGERTOREAL":
			returnNode = integerToRealFunction();
			System.out.println(returnNode.toString());
			break;
		case "REALTOINTEGER":
			returnNode = realToIntegerFunction();
			System.out.println(returnNode.toString());
			break;
		case "WHILE":
			returnNode = whileBlock();
			break;
		case "REPEAT":
			returnNode = repeatBlock();
			break;
		case "FOR":
			returnNode = forBlock();
			break;
		case "IF":
			returnNode = ifBlock();
			break;
		default:
			System.out.println("Parser 'statement' Exception: Invalid input");
			throw new RuntimeException("Parser 'statement' Exception: Invalid input");
		}
		
		System.out.println("Statement ended");
		
		return returnNode;
	}
	
	private RealToIntegerNode realToIntegerFunction () {
		
		ArrayList <VariableReferenceNode> variables = new ArrayList <VariableReferenceNode> ();
		
		if (matchAndRemove("REALTOINTEGER") == null) {
			System.out.println("Parser 'realToIntegerFunction' Exception: realToInteger expected.");
			throw new RuntimeException("Parser 'realToIntegerFunction' Exception: realToInteger expected.");
		}
		
		Token identifier = matchAndRemove("IDENTIFIER");
		
		if (identifier == null) {
			System.out.println("Parser 'realToIntegerFunction' Exception: Identifier expected.");
			throw new RuntimeException("Parser 'realToIntegerFunction' Exception: Identifier expected.");
		}
		
		variables.add(new VariableReferenceNode(identifier.getIdentifier()));
		
		if (matchAndRemove("COMMA") == null) {
			System.out.println("Parser 'realToIntegerFunction' Exception: Comma expected.");
			throw new RuntimeException("Parser 'realToIntegerFunction' Exception: Comma expected.");
		}
		
		if (matchAndRemove("VAR") == null) {
			System.out.println("Parser 'realToIntegerFunction' Exception: Comma expected.");
			throw new RuntimeException("Parser 'realToIntegerFunction' Exception: Comma expected.");
		}
		
		Token varIdentifier = matchAndRemove("IDENTIFIER");

		if (varIdentifier == null) {
			System.out.println("Parser 'realToIntegerFunction' Exception: Identifier expected.");
			throw new RuntimeException("Parser 'realToIntegerFunction' Exception: Identifier expected.");
		}
		
		variables.add(new VariableReferenceNode(varIdentifier.getIdentifier()));
		
		while (matchAndRemove("COMMA") != null) {
			
			if (matchAndRemove("INTEGERTOREAL") == null) {
				System.out.println("Parser 'realToIntegerFunction' Exception: integerToReal expected.");
				throw new RuntimeException("Parser 'realToIntegerFunction' Exception: integerToReal expected.");
			}
			
			identifier = matchAndRemove("IDENTIFIER");
			
			if (identifier == null) {
				System.out.println("Parser 'realToIntegerFunction' Exception: Identifier expected.");
				throw new RuntimeException("Parser 'realToIntegerFunction' Exception: Identifier expected.");
			}
			
			variables.add(new VariableReferenceNode(identifier.getIdentifier()));
			
			if (matchAndRemove("COMMA") == null) {
				System.out.println("Parser 'realToIntegerFunction' Exception: Comma expected.");
				throw new RuntimeException("Parser 'realToIntegerFunction' Exception: Comma expected.");
			}
			
			if (matchAndRemove("VAR") == null) {
				System.out.println("Parser 'realToIntegerFunction' Exception: Comma expected.");
				throw new RuntimeException("Parser 'realToIntegerFunction' Exception: Comma expected.");
			}
			
			varIdentifier = matchAndRemove("IDENTIFIER");

			if (varIdentifier == null) {
				System.out.println("Parser 'realToIntegerFunction' Exception: Identifier expected.");
				throw new RuntimeException("Parser 'realToIntegerFunction' Exception: Identifier expected.");
			}
			
			variables.add(new VariableReferenceNode(varIdentifier.getIdentifier()));
		}
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'realToIntegerFunction' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'realToIntegerFunction' Exception: EndOfLine expected.");
		}
		
		return new RealToIntegerNode (variables);
	}
	
	private IntegerToRealNode integerToRealFunction () {
		
		ArrayList <VariableReferenceNode> variables = new ArrayList <VariableReferenceNode> ();
		
		if (matchAndRemove("INTEGERTOREAL") == null) {
			System.out.println("Parser 'integerToRealFunction' Exception: integerToReal expected.");
			throw new RuntimeException("Parser 'integerToRealFunction' Exception: integerToReal expected.");
		}
		
		Token identifier = matchAndRemove("IDENTIFIER");
		
		if (identifier == null) {
			System.out.println("Parser 'integerToRealFunction' Exception: Identifier expected.");
			throw new RuntimeException("Parser 'integerToRealFunction' Exception: Identifier expected.");
		}
		
		variables.add(new VariableReferenceNode(identifier.getIdentifier()));
		
		if (matchAndRemove("COMMA") == null) {
			System.out.println("Parser 'integerToRealFunction' Exception: Comma expected.");
			throw new RuntimeException("Parser 'integerToRealFunction' Exception: Comma expected.");
		}
		
		if (matchAndRemove("VAR") == null) {
			System.out.println("Parser 'integerToRealFunction' Exception: Comma expected.");
			throw new RuntimeException("Parser 'integerToRealFunction' Exception: Comma expected.");
		}
		
		Token varIdentifier = matchAndRemove("IDENTIFIER");

		if (varIdentifier == null) {
			System.out.println("Parser 'integerToRealFunction' Exception: Identifier expected.");
			throw new RuntimeException("Parser 'integerToRealFunction' Exception: Identifier expected.");
		}
		
		variables.add(new VariableReferenceNode(varIdentifier.getIdentifier()));
		
		while (matchAndRemove("COMMA") != null) {
			
			if (matchAndRemove("INTEGERTOREAL") == null) {
				System.out.println("Parser 'integerToRealFunction' Exception: integerToReal expected.");
				throw new RuntimeException("Parser 'integerToRealFunction' Exception: integerToReal expected.");
			}
			
			identifier = matchAndRemove("IDENTIFIER");
			
			if (identifier == null) {
				System.out.println("Parser 'integerToRealFunction' Exception: Identifier expected.");
				throw new RuntimeException("Parser 'integerToRealFunction' Exception: Identifier expected.");
			}
			
			variables.add(new VariableReferenceNode(identifier.getIdentifier()));
			
			if (matchAndRemove("COMMA") == null) {
				System.out.println("Parser 'integerToRealFunction' Exception: Comma expected.");
				throw new RuntimeException("Parser 'integerToRealFunction' Exception: Comma expected.");
			}
			
			if (matchAndRemove("VAR") == null) {
				System.out.println("Parser 'integerToRealFunction' Exception: Comma expected.");
				throw new RuntimeException("Parser 'integerToRealFunction' Exception: Comma expected.");
			}
			
			varIdentifier = matchAndRemove("IDENTIFIER");

			if (varIdentifier == null) {
				System.out.println("Parser 'integerToRealFunction' Exception: Identifier expected.");
				throw new RuntimeException("Parser 'integerToRealFunction' Exception: Identifier expected.");
			}
			
			variables.add(new VariableReferenceNode(varIdentifier.getIdentifier()));
		}
		
		return new IntegerToRealNode (variables);
	}
	
	private GetRandomNode getRandomFunction() {
		System.out.println("GetRandom Function Reached");
		
		ArrayList <VariableReferenceNode> variables = new ArrayList <VariableReferenceNode> ();
		
		if (matchAndRemove("GETRANDOM") == null) {
			System.out.println("Parser 'getRandomFunction' Exception: getRandom expected.");
			throw new RuntimeException("Parser 'getRandomFunction' Exception: getRandom expected.");
		}
		
		if (matchAndRemove("VAR") == null) {
			System.out.println("Parser 'getRandomFunction' Exception: Var expected.");
			throw new RuntimeException("Parser 'getRandomFunction' Exception: Var expected.");
		}
		
		Token identifier = matchAndRemove("IDENTIFIER");
		
		if (identifier == null) {
			System.out.println("Parser 'getRandomFunction' Exception: Identifier expected.");
			throw new RuntimeException("Parser 'getRandomFunction' Exception: Identifier expected.");
		}
		
		variables.add(new VariableReferenceNode(identifier.getIdentifier()));
		
		while (matchAndRemove("COMMA") != null) {
			
			if (matchAndRemove("VAR") == null) {
				System.out.println("Parser 'getRandomFunction' Exception: Var expected.");
				throw new RuntimeException("Parser 'getRandomFunction' Exception: Var expected.");
			}
			
			identifier = matchAndRemove("IDENTIFIER");
			
			if (identifier == null) {
				System.out.println("Parser 'getRandomFunction' Exception: Identifier expected.");
				throw new RuntimeException("Parser 'getRandomFunction' Exception: Identifier expected.");
			}
			
			variables.add(new VariableReferenceNode(identifier.getIdentifier()));
		}
		
		GetRandomNode getRandomNode = new GetRandomNode(variables);
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'getRandom' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'getRandom' Exception: EndOfLine expected.");
		}
		
		System.out.println("GetRandom Function Ended");
		
		return getRandomNode;
		
	}
	
	private SqrtNode sqrtFunction() {
		System.out.println("Square Root Function Reached");
		
		if (matchAndRemove("SQUAREROOT") == null) {
			System.out.println("Parser 'sqrtFunction' Exception: squareRoot expected.");
			throw new RuntimeException("Parser 'sqrtFunction' Exception: squareRoot expected.");
		}
		
		Token number = matchAndRemove("NUMBER");
		
		if (number == null) {
			System.out.println("Parser 'sqrtFunction' Exception: Number expected.");
			throw new RuntimeException("Parser 'sqrtFunction' Exception: Number expected.");
		}
		
		if (number.getValue() < 0) {
			System.out.println("Parser 'sqrtFunction' Exception: Cannot get the square root of a negative number.");
			throw new RuntimeException("Parser 'sqrtFunction' Exception: Cannot get the square root of a negative number.");
		}
		
		if (matchAndRemove("COMMA") == null) {
			System.out.println("Parser 'sqrtFunction' Exception: Comma expected.");
			throw new RuntimeException("Parser 'sqrtFunction' Exception: Comma expected.");
		}
		
		if (matchAndRemove("VAR") == null) {
			System.out.println("Parser 'sqrtFunction' Exception: Var expected.");
			throw new RuntimeException("Parser 'sqrtFunction' Exception: Var expected.");
		}
		
		Token varIdentifier = matchAndRemove("IDENTIFIER");
		
		if (varIdentifier == null) {
			System.out.println("Parser 'sqrtFunction' Exception: Identifier expected.");
			throw new RuntimeException("Parser 'sqrtFunction' Exception: Identifier expected.");
		}
		
		VariableReferenceNode referencedVariable = new VariableReferenceNode(varIdentifier.getIdentifier());
		
		SqrtNode sqrtNode = new SqrtNode((float)number.getValue(), referencedVariable);
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'sqrtFunction' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'sqrtFunction' Exception: EndOfLine expected.");
		}
		
		System.out.println("Square Root Function Ended");
		
		return sqrtNode;
		
	}
	
	private WriteNode writeFunction() {
		System.out.println("Write Function Reached");
		
		ArrayList <VariableReferenceNode> variables = new ArrayList <VariableReferenceNode> ();
		
		if (matchAndRemove("WRITE") == null) {
			System.out.println("Parser 'writeFunction' Exception: Write expected.");
			throw new RuntimeException("Parser 'writeFunction' Exception: Write expected.");
		}
		
		Token identifier = matchAndRemove("IDENTIFIER");
		
		if (identifier == null) {
			System.out.println("Parser 'writeFunction' Exception: Identifier expected.");
			throw new RuntimeException("Parser 'writeFunction' Exception: Identifier expected.");
		}
		
		variables.add(new VariableReferenceNode(identifier.getIdentifier()));
		
		while (matchAndRemove("COMMA") != null) {
			
			identifier = matchAndRemove("IDENTIFIER");
			
			if (identifier == null) {
				System.out.println("Parser 'writeFunction' Exception: Identifier expected.");
				throw new RuntimeException("Parser 'writeFunction' Exception: Identifier expected.");
			}
			
			variables.add(new VariableReferenceNode(identifier.getIdentifier()));
		}
		
		WriteNode writeNode = new WriteNode(variables);
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'writeFunction' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'writeFunction' Exception: EndOfLine expected.");
		}
		
		System.out.println("Write Function Ended");
		
		return writeNode;
		
	}
	
	private ReadNode readFunction() {
		
		System.out.println("Read Function Reached");
		
		ArrayList <VariableReferenceNode> variables = new ArrayList <VariableReferenceNode> ();
		
		if (matchAndRemove("READ") == null) {
			System.out.println("Parser 'readFunction' Exception: Read expected.");
			throw new RuntimeException("Parser 'readFunction' Exception: Read expected.");
		}
		
		if (matchAndRemove("VAR") == null) {
			System.out.println("Parser 'readFunction' Exception: Var expected.");
			throw new RuntimeException("Parser 'readFunction' Exception: Var expected.");
		}
		
		Token identifier = matchAndRemove("IDENTIFIER");
		
		if (identifier == null) {
			System.out.println("Parser 'readFunction' Exception: Identifier expected.");
			throw new RuntimeException("Parser 'readFunction' Exception: Identifier expected.");
		}
		
		variables.add(new VariableReferenceNode(identifier.getIdentifier()));
		
		while (matchAndRemove("COMMA") != null) {
			
			if (matchAndRemove("VAR") == null) {
				System.out.println("Parser 'readFunction' Exception: Var expected.");
				throw new RuntimeException("Parser 'readFunction' Exception: Var expected.");
			}
			
			identifier = matchAndRemove("IDENTIFIER");
			
			if (identifier == null) {
				System.out.println("Parser 'readFunction' Exception: Identifier expected.");
				throw new RuntimeException("Parser 'readFunction' Exception: Identifier expected.");
			}
			
			variables.add(new VariableReferenceNode(identifier.getIdentifier()));
		}
		
		ReadNode readNode = new ReadNode(variables);
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'readFunction' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'readFunction' Exception: EndOfLine expected.");
		}
		
		System.out.println("Read Function Ended");
		
		return readNode;
		
	}
	
	/**
	 * Handles an entire function call that contains parameters
	 * Expects paramerter {COMMA parameter}
	 * @param functionName The name of the function as an identifier token 
	 * @return Returns the FunctionCallNode representing this function call
	 */
	private FunctionCallNode functionCall(Token functionName) {
		System.out.println("Function Call Reached");
		
		ArrayList <Node> functionCallParameters = new ArrayList <Node> ();
		
		functionCallParameters.add(functionParameter());
		
		// Any other parameters in the list are separated by commas
		while (matchAndRemove("COMMA") != null) {
			functionCallParameters.add(functionParameter());
		}
		
		System.out.println("NEXT" +peek());
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'functionCall' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'functionCall' Exception: EndOfLine expected.");
		}
		
		FunctionCallNode functionCallNode = new FunctionCallNode (functionName.getIdentifier(), functionCallParameters);
		
		System.out.println("Function Call Ended");
		
		return functionCallNode;
	}
	
	/**
	 * Handles a single function call parameter
	 * Expects (VAR, IDEN) || (IDEN) || (INT/REAL)
	 * @return Returns a VariableReferenceNode or a IntegerNode/RealNode depending on the type of parameter
	 */
	private Node functionParameter () {
		
		System.out.println("Function Call Parameter Reached");
	
		Node parameter = new MathOpNode();
			
		if (peek().getTokenType().equals("VAR")) {
			
			if (matchAndRemove("VAR") == null) {
				System.out.println("Parser 'functionParameter' Exception: Var expected.");
				throw new RuntimeException("Parser 'functionParameter' Exception: Var expected.");
			}
			
			Token identifier = matchAndRemove("IDENTIFIER");
			
			if (identifier == null) {
				System.out.println("Parser 'functionParameter' Exception: Identifier expected.");
				throw new RuntimeException("Parser 'functionParameter' Exception: Identifier expected.");
			}
				
			parameter = new VariableReferenceNode(identifier.getIdentifier());
			
		}
		
		else if(peek().getTokenType().equals("IDENTIFIER")) {
			
			Token identifier = matchAndRemove("IDENTIFIER");
			
			if (identifier == null) {
				System.out.println("Parser 'functionParameter' Exception: Identifier expected.");
				throw new RuntimeException("Parser 'functionParameter' Exception: Identifier expected.");
			}
				
			parameter = new VariableReferenceNode(identifier.getIdentifier());
			
		}
		
		else if(peek().getTokenType().equals("NUMBER")) {
			
			Token number = matchAndRemove("NUMBER");
			
			if (number == null) {
				System.out.println("Parser 'functionParameter' Exception: Number expected.");
				throw new RuntimeException("Parser 'functionParameter' Exception: Number expected.");
			}
			
			// If the number represents an integer
			if (number.getValue() % 1 == 0) {
				parameter = new IntegerNode (number, (int) number.getValue());
			}
			
			// If the number represents a float
			else {
				parameter = new FloatNode (number, (float) number.getValue());
			}
		}
		
		// If the parameter is not valid; not Identifier, not Var Identifier, not Number
		else {
			System.out.println("Parser 'functionParameter' Exception: Parameters expected.");
			throw new RuntimeException("Parser 'functionParameter' Exception: Parameters expected.");
		}

		System.out.println("Function Call Parameter Ended");
			
		return parameter;
			
	}
	
	/**
	 * Handles an if block and any subsequent elsifs
	 * Expects IF, boolean expression, THEN, EndOfLine, body, {ELSIF, boolean expression, THEN, EndOfLine, body}
	 * @return
	 */
	private IfNode ifBlock() {
		
		IfNode mostRecentIf = new IfNode();
		
		if (matchAndRemove("IF") == null) {
			System.out.println("Parser 'ifBlock' Exception: If expected.");
			throw new RuntimeException("Parser 'ifBlock' Exception: If expected.");
		}
		
		BooleanExpressionNode booleanExpression = booleanExpression();
		
		if (booleanExpression == null) {
			System.out.println("Parser 'ifBlock' Exception: Boolean expression expected.");
			throw new RuntimeException("Parser 'ifBlock' Exception: Boolean expression expected.");
		}
		
		if (matchAndRemove("THEN") == null) {
			System.out.println("Parser 'ifBlock' Exception: Then expected.");
			throw new RuntimeException("Parser 'ifBlock' Exception: Then expected.");
		}
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'ifBlock' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'ifBlock' Exception: EndOfLine expected.");
		}
		
		ArrayList <StatementNode> ifStatements = bodyFunction();
		
		IfNode ifNode = new IfNode(ifStatements, booleanExpression);
		mostRecentIf = ifNode;
		
		while (matchAndRemove("ELSIF") != null) {
			
			System.out.println("Elsif reached");
			
			BooleanExpressionNode elsifBoolean = booleanExpression();
			
			if (elsifBoolean == null) {
				System.out.println("Parser 'ifBlock' Exception: Boolean expression expected.");
				throw new RuntimeException("Parser 'ifBlock' Exception: Boolean expression expected.");
			}
			
			if (matchAndRemove("THEN") == null) {
				System.out.println("Parser 'ifBlock' Exception: Then expected.");
				throw new RuntimeException("Parser 'ifBlock' Exception: Then expected.");
			}
			
			if (matchAndRemove("EndOfLine") == null) {
				System.out.println("Parser 'ifBlock' Exception: EndOfLine expected.");
				throw new RuntimeException("Parser 'ifBlock' Exception: EndOfLine expected.");
			}
			
			ArrayList <StatementNode> elsifStatements = bodyFunction();
			
			IfNode elsifNode = new IfNode (elsifStatements, elsifBoolean);
			
			mostRecentIf.add(elsifNode);
			mostRecentIf = elsifNode;
		}
		
		
		if (matchAndRemove("ELSE") != null) {
			
			ArrayList <StatementNode> elseStatements = new ArrayList <StatementNode> ();
			
			if (matchAndRemove("EndOfLine") == null) {
				System.out.println("Parser 'ifBlock' Exception: EndOfLine expected.");
				throw new RuntimeException("Parser 'ifBlock' Exception: EndOfLine expected.");
			}
			
			elseStatements = bodyFunction();
			
			IfNode elseNode = new IfNode (elseStatements);
			
			mostRecentIf.add(elseNode);
			mostRecentIf = elseNode;
			
		}
		
		return ifNode;
		
	}
	
	/**
	 * Handles a for block
	 * Expects FOR, IDENTIFIER, FROM, NUMBER, TO, NUMBER, EndOfLine, body
	 * @return Reutnrs the forNode with the variable being referenced, a start, and an end as well as statements within the block
	 */
	private ForNode forBlock() {
		
		System.out.println("For block reached");
		
		ArrayList <StatementNode> statements = new ArrayList<StatementNode> ();
		Node start = new MathOpNode();
		Node end = new MathOpNode();
		
		if (matchAndRemove("FOR") == null) {
			System.out.println("Parser 'forBlock' Exception: For expected.");
			throw new RuntimeException("Parser 'forBlock' Exception: For expected.");
		}
		
		Token identifier = matchAndRemove("IDENTIFIER"); //Holds on to ideentifier value
		
		if (identifier == null) {
			System.out.println("Parser 'forBlock' Exception: Identifier expected.");
			throw new RuntimeException("Parser 'forBlock' Exception: Identifier expected.");
		}
		
		if (matchAndRemove("FROM") == null) {
			System.out.println("Parser 'forBlock' Exception: From expected.");
			throw new RuntimeException("Parser 'forBlock' Exception: From expected.");
		}
		
		Token number = matchAndRemove("NUMBER"); //Holds on to number value
		
		if (number == null) {
			System.out.println("Parser 'forBlock' Exception: Number expected.");
			throw new RuntimeException("Parser 'forBlock' Exception: Number expected.");
		}
		
		// Determines whether the START is an integer or a float
		if (number.getValue() % 1 == 0) {
			start = new IntegerNode(number, (int) number.getValue());
		}
		else {
			start = new FloatNode(number, (float) number.getValue());
		}
		
		if (matchAndRemove("TO") == null) {
			System.out.println("Parser 'forBlock' Exception: To expected.");
			throw new RuntimeException("Parser 'forBlock' Exception: To expected.");
		}
	
		Token numberEnd = matchAndRemove("NUMBER"); //Holds on to number value 
		
		if (numberEnd == null) {
			System.out.println("Parser 'forBlock' Exception: Number expected.");
			throw new RuntimeException("Parser 'forBlock' Exception: Number expected.");
		}
		
		// Determines whether the END is an integer or a float
		if (numberEnd.getValue() % 1 == 0) {
			end = new IntegerNode(numberEnd, (int) numberEnd.getValue());
		}
		else {
			end = new FloatNode(numberEnd, (float) numberEnd.getValue());
		}
		
		VariableReferenceNode variable = new VariableReferenceNode(identifier.getIdentifier());
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'forBlock' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'forBlock' Exception: EndOfLine expected.");
		}
		
		statements = bodyFunction();
		
		System.out.println("For block ended")
;		
		return new ForNode(statements, variable, start, end);
		
	}
	
	/**
	 * Handles any instance of a repeat block and calls statements() for the repeat block's body
	 * Expects REPEAD, EndOfLine, body, UNTIL, booleanExpression, EndOfLine
	 * @return
	 */
	private RepeatNode repeatBlock() {
		System.out.println("Repeat block reached");
		
		ArrayList <StatementNode> statements = new ArrayList <StatementNode> ();
		
		if (matchAndRemove("REPEAT") == null) {
			System.out.println("Parser 'repeatBlock' Exception: Repeat expected.");
			throw new RuntimeException("Parser 'repeatBlock' Exception: Repeat expected.");
		}
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'repeatBlock' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'repeatBlock' Exception: EndOfLine expected.");
		}
		
		statements = bodyFunction();
		
		if (matchAndRemove("UNTIL") == null) {
			System.out.println("Parser 'repeatBlock' Exception: Until expected.");
			throw new RuntimeException("Parser 'repeatBlock' Exception: Until expected.");
		
		}
		
		BooleanExpressionNode booleanExpression = booleanExpression();
		
		if (booleanExpression == null) {
			System.out.println("Parser 'repeatBlock' Exception: Boolean expression expected.");
			throw new RuntimeException("Parser 'repeatBlock' Exception: Boolean expression expected.");
		}
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'repeatBlock' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'repeatBlock' Exception: EndOfLine expected.");
		}
		
		System.out.println("Repeat block ended");
		
		return new RepeatNode(booleanExpression, statements);
	}
	
	/**
	 * Handles any instance of a while block and calls statements() for the while block's statements
	 * Expects WHILE, BOOLEAN EXPRESSION, EndOfLine, and a valid body with BEGIN and END
	 * @return The whileNode to be added to the AST
	 */
	private WhileNode whileBlock () {
		System.out.println("While block reached");
		
		ArrayList <StatementNode> statements = new ArrayList <StatementNode> ();
		
		if (matchAndRemove("WHILE") == null) {
			System.out.println("Parser Exception: While expected.");
			throw new RuntimeException("Parser Exception: While expected.");
		}
		
		BooleanExpressionNode  booleanExpression = booleanExpression();
		
		if (booleanExpression == null) {
			System.out.println("Parser Exception: Invalid boolean expression.");
			throw new RuntimeException("Parser Exception: Invalid boolean expression.");
		}
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'whileBlock' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'whileBlock' Exception: EndOfLine expected.");
		}
		
		statements = bodyFunction();
		
		WhileNode whileNode = new WhileNode (booleanExpression, statements);
		
		System.out.println("While block ended");
		
		return whileNode;
		
	}
	
	/**
	 * Handles simple boolean expressions in format of expression-comparator-expression
	 * Expects {NUMBER||IDENTIFIER}, a comparator, {NUMBER||IDENTIFIER}
	 * @return The booleanExpressionNode made up of Comparator, leftExpression, rightExpression
	 */
	private BooleanExpressionNode booleanExpression() {		
		System.out.println("Boolean expression reached");
		
		if (!peek().getTokenType().equals("NUMBER") && !peek().getTokenType().equals("IDENTIFIER")) {
			System.out.println("Parser Exception: Number or Identifier expected.");
			throw new RuntimeException("Parser Exception: Number or Identifier expected.");
		}
		
		Node booleanLeft = expression();
		
		Token comparator = getComparator();
		
		if (comparator == null) {
			System.out.println("Parser Exception: Comparator expected.");
			throw new RuntimeException("Parser Exception: Comparator expected.");
		}
		
		if (!peek().getTokenType().equals("NUMBER") && !peek().getTokenType().equals("IDENTIFIER")) {
			System.out.println("Parser Exception: Number or Identifier expected.");
			throw new RuntimeException("Parser Exception: Number or Identifier expected.");
		}
		
		Node booleanRight = expression();
		
		BooleanExpressionNode booleanExpression = new BooleanExpressionNode(comparator, booleanLeft, booleanRight);
		
		System.out.println("Boolean expression ended");
		
		return booleanExpression;
	}
	
	/**
	 * A quick method designed specifically to determine the comparator in a boolean expression 
	 * Made mainly to avoid a long if-else statement in the actual boyd of booleanExpression()
	 * @return The type of token the comparator is, return null if the next token is not a comparator
	 */
	private Token getComparator() {
		if (matchAndRemove("EQUAL") != null) {
			return new Token(Tokens.EQUAL);
		}
		else if (matchAndRemove("NOTEQUAL") != null) {
			return new Token(Tokens.NOTEQUAL);
		}
		else if (matchAndRemove("GREATER") != null) {
			return new Token(Tokens.GREATER);
		}
		else if (matchAndRemove("LESS") != null) {
			return new Token(Tokens.LESS);
		}
		else if (matchAndRemove("GREATERTHANEQUAL") != null) {
			return new Token(Tokens.GREATERTHANEQUAL);
		}
		else if (matchAndRemove("LESSTHANEQUAL") != null) {
			return new Token(Tokens.LESSTHANEQUAL);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Handles assignment statements. Expects IDENTIFIER, ASSIGN, NUMBER, EndOfLine
	 * IDENTIFIER and ASSIGN have already been handled by this point, only NUMBER gets checked here
	 * @param identifier The identifier of the variable that's being assigned to a specific value
	 * @return Returns an AssignmentNode representing the entire assignment
	 */
	private AssignmentNode assignment(Token identifier) {
		System.out.println("Asssignment Reached");
		
		if (matchAndRemove("ASSIGN") == null) {
			System.out.println("Parser 'assignment' Exception: Assign expected.");
			throw new RuntimeException("Parser 'assignment' Exception: Assign expected.");
		}
		
		if (!peek().getTokenType().equals("NUMBER") && !peek().getTokenType().equals("IDENTIFIER")) {
			System.out.println("Parser 'assignment' Exception: Number expected.");
			throw new RuntimeException("Parser 'assignment' Exception: Number expected.");
		}
		
		//jmkjm

		Node expression = expression();
		
		System.out.println("OWO" +expression.toString());
				
		VariableReferenceNode referencedVariable = new VariableReferenceNode(identifier.getIdentifier());
		AssignmentNode assignmentNode = new AssignmentNode(referencedVariable, expression);
		
		// An endofLine is always expected at the end of a statement 
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'assignment' Exception: EndOfLine expected.");
			throw new RuntimeException("Parser 'assignment' Exception: EndOfLine expected.");
		}
		
		System.out.println("Asssignment Ended");
		
		return assignmentNode;
	}
	
	/**
	 * Responsible for getting nodes of ALL function paramters, including those of different types
	 * @return All function parameters
	 */
	private ArrayList <VariableNode> parameters() {
		ArrayList <VariableNode> allParameterNodes = new ArrayList <VariableNode> (); //Represents all parameters in function header
		ArrayList <VariableNode> parameterNodes = parametersOfAType(); //Represents all parameters in the lists seprarated by semicolons
		
		for (int i = 0; i<parameterNodes.size(); i++) {
			allParameterNodes.add(parameterNodes.get(i));
		}
		
		// Identifiers separated by semicolons  
		while (matchAndRemove("SEMICOLON") != null) {
			parameterNodes = parametersOfAType(); //Only gets a single lines
			for (int i = 0; i<parameterNodes.size(); i++) {
				allParameterNodes.add(parameterNodes.get(i)); //Adds a single line to all the lines
			}
		}
		return allParameterNodes;
	}
	
	/**
	 * Responsible for getting nodes of function paramters in lists separated by semicolons
	 * ie the collection (param1, param2, param3 : integer)
	 * Called as many times as needed to represent all the lists in the function parameters
	 * @return Parameter of a type
	 */
	private ArrayList <VariableNode> parametersOfAType(){
		ArrayList <Token> parameterTokens = new ArrayList<Token> (); //Collection of TOKEN versions of paramters
		ArrayList <VariableNode> parameterNodes = new ArrayList <VariableNode> (); //Collection of NODE parameters to be derived from TOKEN arraylist
		parameterTokens.add(matchAndRemove("IDENTIFIER"));
	
		// More than one comma can separate paramters in a list, but one doesn't need to be encountered
		while (matchAndRemove("COMMA") != null) {
			parameterTokens.add(matchAndRemove("IDENTIFIER"));
		}
		// Colon expexted
		if (matchAndRemove("COLON") == null) {
			System.out.println("Parser 'parametersOfAType' Exception: Colon expected.");
			throw new RuntimeException("Parser 'parametersOfAType' Exception: Colon expected.");
		}
		// public VariableNode (Node value, boolean isConstant, types type) {
		
		// Next expected token is either an INTEGER or REAL token, any others are invalid (as of asmt 3)
		if (matchAndRemove("INTEGER") != null) {
			
			// Initializes all parameters at a value of 0
			for (int i = 0; i < parameterTokens.size(); i++) {
				IntegerNode integerValue = new IntegerNode(new Token(Tokens.NUMBER, "0"), 0);
				VariableNode parameterNode = new VariableNode (parameterTokens.get(i).getIdentifier(), integerValue, false, types.INTEGER);
				parameterNodes.add(parameterNode);
			}
		} 
		else if (matchAndRemove("REAL") != null){
			
			// Initializes all parameters at a value of 0
			for (int i = 0; i < parameterTokens.size(); i++) {	
				FloatNode floatValue = new FloatNode(new Token(Tokens.NUMBER, "0"), 0);
				VariableNode parameterNode = new VariableNode (parameterTokens.get(i).getIdentifier(), floatValue, false, types.REAL);
				parameterNodes.add(parameterNode);
			}
		}
		
		// Invalid data type
		else {
			System.out.println("Parser 'parametersOfAType' Exception: Invalid data type.");
			throw new RuntimeException("Parser 'parametersOfAType' Exception: Invalid data type.");
		}
		return parameterNodes;
	}
	
	
	/**
	 * Runs for every expected line in the CONSTANTS section
	 * Expected tokens IDENTIFIER, EQUAL, NUMBER, EndOfLine
	 * @return Returns a VariableNode of the designated constant
	 */
	private VariableNode constants (){
		Token identifier = matchAndRemove("IDENTIFIER");
		
		if (identifier == null) {
			System.out.println("Parser 'constants' error: No Identifier");
			throw new RuntimeException("Parser 'constants' error: No Identifier");
		}
		
		if (matchAndRemove ("EQUAL") == null) {
			System.out.println("Parser ' constants' error: No equals");
			throw new RuntimeException("Parser 'constants' error: No equals");
		}
		
		Token number  = matchAndRemove("NUMBER");
		
		if (number == null) {
			System.out.println("Parser 'constants' error : No number");
			throw new RuntimeException("Parser 'constants' error: No number");
		}
		
		VariableNode constantNode = new VariableNode();
		
		// Makes an INTEGER type node
		if (number.getValue() % 1 == 0) {
			constantNode = (new VariableNode(identifier.getIdentifier(), new IntegerNode(number, (int)number.getValue()), true, types.INTEGER));
		}
		
		// Makes a REAL type node
		else {
			constantNode = (new VariableNode(identifier.getIdentifier(), new FloatNode(number, (float) number.getValue()), true, types.REAL));
		}
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser 'constants' error: No EndOfLine Token");
			throw new RuntimeException("Parser 'constants' error: No EndOfLine Token");
		}
		return constantNode;
	}
	
	/**
	 * Runs for one line in the VARIABLES section
	 */
	private ArrayList <VariableNode> variables(){
		
		ArrayList <Token> identifiers = new ArrayList <Token> (); //All identifiers in a line of variables
		ArrayList <VariableNode> variableNodes = new ArrayList <VariableNode> (); 
		
		Token identifier = matchAndRemove("IDENTIFIER");
		identifiers.add(identifier);
		
		// All identifiers separated by commas in the variables section
		while(matchAndRemove("COMMA") != null) {
			identifier = (matchAndRemove("IDENTIFIER"));
			identifiers.add(identifier);
		}
		
		if (matchAndRemove("COLON") == null) {
			System.out.println("Parser Exception: Colon expected.");
			throw new RuntimeException("Parser Exception: Colon expected.");
		}
		
		if (matchAndRemove("INTEGER") != null) {
			for (int i = 0; i<identifiers.size(); i++) {
				variableNodes.add(new VariableNode(identifiers.get(i).getIdentifier(), (new IntegerNode(new Token(Tokens.NUMBER, "0"), 0)), false, types.INTEGER));
			}
		}
		else if (matchAndRemove("REAL") != null) {
			for (int i = 0; i<identifiers.size(); i++) {
				variableNodes.add(new VariableNode(identifiers.get(i).getIdentifier(), (new FloatNode(new Token(Tokens.NUMBER, "0"), 0)), false, types.REAL));
			}
		}
		else if (matchAndRemove("STRING") != null) {
			for (int i = 0; i<identifiers.size(); i++) {
				variableNodes.add(new VariableNode(identifiers.get(i).getIdentifier(), (new StringNode("")), false, types.STRING));
			}
		}
		else if (matchAndRemove("CHAR") != null) {
			for (int i = 0; i<identifiers.size(); i++) {
				variableNodes.add(new VariableNode(""+identifiers.get(i).getCharacter(), (new CharNode(' ')), false, types.CHAR));
			}
		}
		else {
			System.out.println("Parser Exception: Invalid data type.");
			throw new RuntimeException("Parser Exception: Invalid data type.");
		}
		
		if (matchAndRemove("EndOfLine") == null) {
			System.out.println("Parser Exception: EndOfLine expected.");
			throw new RuntimeException("Parser Exception: EndOfLine expected.");
		}
		
		return variableNodes;
	}
	
	/**
	 * First layer of mathematical expression parser, handles PLUS and MINUS operations
	 * @return Returns the head of the ast node
	 */
	private Node expression() {
		System.out.println("Expression accessed");
		
		Node nodeOne = term(); //left node of upcoming MathOpNode
		Node nodeTwo = null; //right node of upcoming MathOpNode
		while (tokens.get(0).getTokenType() == "PLUS" || tokens.get(0).getTokenType() == "MINUS") {
			Token operator;
			if (matchAndRemove("PLUS") != null) {
				operator = new Token (Tokens.PLUS);
				nodeTwo = term();
				nodeOne = new MathOpNode (operator, nodeOne, nodeTwo);
			}
			else if (matchAndRemove("MINUS") != null) {
				operator = new Token (Tokens.MINUS);
				nodeTwo = term();
				nodeOne = new MathOpNode (operator, nodeOne, nodeTwo);
			}
			else if (matchAndRemove("MOD") != null) {
				operator = new Token (Tokens.MOD);
				nodeTwo = term();
				nodeOne = new MathOpNode (operator, nodeOne, nodeTwo);
			}
		}
		System.out.println("Expression ended");
		return nodeOne;
	}
	
	/**
	 * Second layer of the mathematical expression parser, handles TIMES and DIVIDE operations
	 * @return Returns the MathOpNode which leads to a 
	 */
	private Node term() {
		System.out.println("Term accessed");
		
		Node nodeOne = factor(); //left node of upcoming MathOpNode
		Node nodeTwo = null; //right node of upcoming MathOpNode
		while (tokens.get(0).getTokenType() == "TIMES" || tokens.get(0).getTokenType() == "DIVIDE" || tokens.get(0).getTokenType() == "MOD") {
			Token operator;
			if (matchAndRemove("TIMES") != null) {
				operator = new Token (Tokens.TIMES); //Saves token value before initializing the MathOp
				nodeTwo = factor();
				nodeOne = new MathOpNode (operator, nodeOne, nodeTwo);
			}
			else if (matchAndRemove("DIVIDE") != null) {
				operator = new Token (Tokens.DIVIDE); //Saves token value before initializing the MathOp
				nodeTwo = factor();
				nodeOne = new MathOpNode (operator, nodeOne, nodeTwo);
			}
			else if (matchAndRemove("MOD") != null) {
				operator = new Token (Tokens.MOD);
				nodeTwo = factor();
				nodeOne = new MathOpNode (operator, nodeOne, nodeTwo);
			}
		}
		
		System.out.println("Term ended");
		
		return nodeOne; //Returns MathOpNode with the left and right Nodes being a result of factor()
	}
	
	/**
	 * Returns a Node after evaluating what type of number is represented.
	 * If a double is represented, a FloatNode will be returned. 
	 * If an integer is represented, an IntegerNode will be represented.
	 * @return An IntegerNode or FloatNode
	 */
	private Node factor () {
		System.out.println("Factor accessed");
		
		if (tokens.get(0).getTokenType().equals("NUMBER")) {
			Token numToken = matchAndRemove("NUMBER");
			if (numToken.getValue() % 1 == 0) { //Decides if decimal or integer and returns appropriate Node
				return new IntegerNode (numToken, (int) numToken.getValue()); 
			}
			else {
				return new FloatNode (numToken, (float) numToken.getValue());
			}
		}
		else if (matchAndRemove("LPAREN") != null){
			Node returnNode = expression(); //loops back around to expression which keeps calling factor until a number is encountered
			matchAndRemove("RPAREN");
			return returnNode;
		}
		else if (peek().getTokenType().equals("IDENTIFIER")) {
			Token identifier = matchAndRemove("IDENTIFIER");
			System.out.println("Identifier" +identifier.toString());
			System.out.println("Factor ended");
			return new VariableReferenceNode(identifier.getIdentifier());
		}
		else if (peek().getTokenType().equals("STRINGCONTENTS")) {
			Token string = matchAndRemove("STRINGCONTENTS");
			System.out.println("String" +string.toString());
			System.out.println("Factor ended");
			return new StringNode(string.getIdentifier());
		}
		else if (peek().getTokenType().equals("CHARCONTENTS")) {
			Token character = matchAndRemove("CHARCONTENTS");
			System.out.println("Char" +character.toString());
			System.out.println("Factor ended");
			return new CharNode(character.getCharacter());
		}
		else { //Factor() does not run if the next char is not (, an identifier, or a number, the expected values
			return null;
		}
	}
	
	
	private Token matchAndRemove (String token) {
		if(!tokens.isEmpty()) {
			if (token.equals(tokens.get(0).getTokenType())) {
				Token matchedToken = tokens.get(0);
				tokens.remove(0);
				return matchedToken;
			}
		}
		return null;
	}
	
	private Token peek (){
		if (!tokens.isEmpty()) {
			return tokens.get(0);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Transfers the tree to a simple string. Method exists mostly for testing. 
	 * @param head Accepts the head of the tree
	 */
	public void toString(Node head) {
	    if (head == null) {
	    	return;
	    }
		toString(head.getLeft());
	    System.out.println(head.toString());
	    toString(head.getRight());
	}
	
}
