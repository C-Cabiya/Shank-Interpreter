package assignmentTwo;

public class IntegerDataType extends InterpreterDataType{

	private int value; 
	
	public IntegerDataType() {
		
	}
	
	public IntegerDataType(int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "INTEGER DATA TYPE: (" +value +")";
	}

	@Override
	public void fromString(String word) {
		value = Integer.parseInt(word);
	}

	@Override
	public float getValue() {
		return value;
	}

}
