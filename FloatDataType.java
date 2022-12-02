package assignmentTwo;

public class FloatDataType extends InterpreterDataType{

	private float value;
	
	public FloatDataType() {
		
	}
	
	public FloatDataType(float value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "FLOAT DATA TYPE: (" +value +")";
	}

	@Override
	public void fromString(String word) {
		value = Float.parseFloat(word);
	}

	@Override
	public float getValue() {
		return value;
	}

}
