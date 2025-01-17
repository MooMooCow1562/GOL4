package application;

public class FixOverFlows {

	public FixOverFlows() {
		super();
	}

	/**
	 * @param broken
	 * @param boardSize
	 * @return
	 */
	public int FixFlow(int broken, int boardSize) {
		return UnUnderflow(UnOverflow(broken, boardSize), boardSize);
	}

	/**
	 * fixes overflow when given an integer input and an integer dimension, useful
	 * for working with possible overlap on arrays.
	 *
	 * @param overflow
	 * @param boardSize
	 * @return
	 */
	private int UnOverflow(int overflow, int boardSize) {
		return overflow % boardSize;
	}

	/**
	 * fixes underflow when given an integer input and an integer dimension useful
	 * for working with possible overlap on arrays.
	 *
	 * @param underflow
	 * @param boardSize
	 * @return
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
