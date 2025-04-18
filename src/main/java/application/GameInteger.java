package application;

/**
 * Barebones container class that holds the integer value for cells, preventing issues that occur when using java.lang.Integer for comparison.
 */
public class GameInteger {
	private final int VALUE;

	public GameInteger(int value) {
		this.VALUE = value;
	}

	public static int parseInt(String text){
		return java.lang.Integer.parseInt(text);
	}
	public int getVALUE() {
		return VALUE;
	}
}
