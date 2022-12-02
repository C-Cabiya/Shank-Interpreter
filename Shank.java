package assignmentOne;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import assignmentTwo.FloatDataType;
import assignmentTwo.FloatNode;
import assignmentTwo.FunctionNode;
import assignmentTwo.IntegerDataType;
import assignmentTwo.IntegerNode;
import assignmentTwo.Interpreter;
import assignmentTwo.InterpreterDataType;
import assignmentTwo.Node;
import assignmentTwo.Parser;

public class Shank {

	public static void main(String[] args) throws IOException{
		
		// Ensures there is only one argument, otherwise the process quits
		if (args.length > 1 || args.length == 0) { 
			System.out.println("ERROR: There must be one, and only one, argument.");
			System.exit(1);
		}
		
		Path path = Paths.get(args[0]); //Gets the filepath from args [0]
		List <String> fileLines = Files.readAllLines(path); //Returns a list of all lines in the file
		ArrayList <Token> allLines = new ArrayList<Token> (); //Collection of tokens in all lines
		
		// For each element in the list
		for(int i = 0; i<fileLines.size(); i++) {
			Lexer lexer = new Lexer(fileLines.get(i));
			Token[] tokens = lexer.lex(); //Collection of tokens in a given line
			// For each element in the Tokens arrayList
			for (int j = 0; j<lexer.tokenIncrement; j++) {
				System.out.print(tokens[j].toString() +" ");
				allLines.add(tokens[j]);
			}
			
			System.out.println(""); //Prints a new line. Just for formatting.
		}
		
		System.out.println();
		
		HashMap <String, FunctionNode> functions = new HashMap<String, FunctionNode>();
		
		Parser parser = new Parser (allLines);
		Interpreter interpreter = new Interpreter();
		
		// Only parses and interprets is the line is not empty
		if (!allLines.isEmpty()) {
			// Parsing first function
			FunctionNode head = parser.parse();
			functions.put(head.getName(), head);
			
			// Parsing any subsequent functions 
			while (!parser.tokensIsEmpty()) {
				FunctionNode nextFunction = parser.parse();
				functions.put(nextFunction.getName(), nextFunction);
			}
			
			ArrayList <InterpreterDataType> parameters = new ArrayList <InterpreterDataType>();
			
			// Puts together a list of values being passed to a function
			for (int i = 0; i<head.getParameters().size(); i++) {
				
				// If the parameter is an integer
				if (head.getParameters().get(i).getNodeValue() instanceof IntegerNode) {
					IntegerDataType intData = new IntegerDataType((int) head.getParameters().get(i).getNodeValue().getValue());
					parameters.add(intData);
				}
				
				// If the parameter is a float
				else if (head.getParameters().get(i).getNodeValue() instanceof FloatNode) {
					FloatDataType floatData = new FloatDataType(head.getParameters().get(i).getNodeValue().getValue());
					parameters.add(floatData);
				}
				
				// If the parameter is an invalid data type 
				else {
					System.out.println("Shank Exception: Invalid parameter data type.");
					throw new RuntimeException("Shank Exception: Invalid parameter data type.");
				}
			}
			
			// Interprets the first function
			FunctionNode startFunction = functions.get("start");
			if (!(startFunction == null)) {
				interpreter.interpretFunction(startFunction, functions, parameters);
			}
		}
		
		// If there is no contact in the file
		else {
			System.out.println("The file is empty.");
		}
	}

}
