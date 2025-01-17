package application;

import java.util.Random;

public class GameOfLifeClass {
	int[][] currentBoard;
	private int boardSize;
	private Random rnd;
	private FixOverFlows fix;

	/**
	 * @return
	 */
	public Random getRnd() {
		return rnd;
	}

	/**
	 * @param rnd
	 */
	public void setRnd(Random rnd) {
		this.rnd = rnd;
	}

	/**
	 * Nonparaterized constructor, sets the board size to the default of 75*75
	 */
	public GameOfLifeClass() {
		super();
		fix = new FixOverFlows();
		boardSize = 75;
		currentBoard = new int[boardSize][boardSize];
		rnd = new Random(75);/* temporary value for development purposes */
	}

	/**
	 * @return
	 */
	public int getBoardSize() {
		return boardSize;
	}

	/**
	 * generates a new, random board
	 *
	 * @return
	 */
	public int[][] GenerateBoard() {
		int[][] temp = new int[boardSize][boardSize];
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				int next = rnd.nextInt(2);
				if (next == 1)
					next++;
				temp[row][col] = next;
			}
		}
		currentBoard = temp;
		return temp;
	}

	/**
	 * @return
	 */
	public int[][] GetCurrentBoard() {
		return currentBoard;
	}

	/**
	 * @param newBoard
	 */
	public void SetCurrentBoard(int[][] newBoard) {
		this.currentBoard = newBoard;
	}

	/**
	 * @param showDying
	 */
	public void RunOneGen(boolean showDying) {
		int[][] nextBoard = new int[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (showDying) {
					nextBoard[j][i] = FindNextDying(currentBoard[j][i], j, i);
				} else {
					nextBoard[j][i] = FindNextState(currentBoard[j][i], j, i);
				}
			}
		}
		currentBoard = nextBoard;
	}

	/**
	 * @param cellState
	 * @param col
	 * @param row
	 * @return
	 */
	private int FindNextDying(int cellState, int col, int row) {
		int numOfAdjacents = CheckNearCells(col, row);
		if ((numOfAdjacents < 2 || numOfAdjacents > 3) && cellState != 0) {
			return cellState - 1;
		}
		if (numOfAdjacents == 3) {
			return 2;
		}
		if (numOfAdjacents == 2 && cellState == 1) {
			return 0;
		}
		if (cellState == 1) {
			return cellState - 1;
		}
		return cellState;
	}

	/**
	 * @param cellState
	 * @param col
	 * @param row
	 * @return
	 */
	private int FindNextState(int cellState, int col, int row) {
		int numOfAdjacents = CheckNearCells(col, row);
		if (numOfAdjacents < 2 || numOfAdjacents > 3) {
			return 0;
		}
		if (numOfAdjacents == 3) {
			return 2;
		}
		if (cellState == 1) {
			return cellState - 1;
		}
		return cellState;
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 */
	private int CheckNearCells(int row, int col) {
		int count = 0;
		if (currentBoard[fix.FixFlow(row - 1, boardSize)][fix.FixFlow(col - 1, boardSize)] == 2)
			count++;
		if (currentBoard[fix.FixFlow(row - 1, boardSize)][fix.FixFlow(col, boardSize)] == 2)
			count++;
		if (currentBoard[fix.FixFlow(row - 1, boardSize)][fix.FixFlow(col + 1, boardSize)] == 2)
			count++;
		if (currentBoard[fix.FixFlow(row, boardSize)][fix.FixFlow(col - 1, boardSize)] == 2)
			count++;
		if (currentBoard[fix.FixFlow(row, boardSize)][fix.FixFlow(col + 1, boardSize)] == 2)
			count++;
		if (currentBoard[fix.FixFlow(row + 1, boardSize)][fix.FixFlow(col - 1, boardSize)] == 2)
			count++;
		if (currentBoard[fix.FixFlow(row + 1, boardSize)][fix.FixFlow(col + 1, boardSize)] == 2)
			count++;
		if (currentBoard[fix.FixFlow(row + 1, boardSize)][fix.FixFlow(col, boardSize)] == 2)
			count++;
		return count;
	}

}
