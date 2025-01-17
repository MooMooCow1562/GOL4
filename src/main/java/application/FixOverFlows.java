package application;

public class FixOverFlows {

	public FixOverFlows() {
		super();
	}

	/**
	 * @param broken    takes an integer
	 * @param boardSize takes an integer
	 * @return calls both an underflow and overflow method to completely fix an integer that is outside the range of an
	 * array.
	 */
	public int FixFlow(int broken, int boardSize) {
		return UnUnderflow(UnOverflow(broken, boardSize), boardSize);
	}

	/**
	 * @param overflow  takes an integer
	 * @param boardSize takes an integer
	 * @return fixes overflow when given an integer input and an integer dimension, useful
	 * for working with possible overlap on arrays.
	 */
	private int UnOverflow(int overflow, int boardSize) {
		return overflow % boardSize;
	}

	/**
	 * @param underflow takes an integer
	 * @param boardSize takes an integer
	 * @return fixes underflow when given an integer input and an integer dimension useful
	 * for working with possible overlap on arrays.
	 */
	private int UnUnderflow(int underflow, int boardSize) {
		if (underflow < 0) {
			while (underflow < 0) {
				underflow += boardSize;
			}
		}
		return underflow;
	}
}
