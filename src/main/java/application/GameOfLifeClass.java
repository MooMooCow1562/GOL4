package application;

import java.util.Random;

public class GameOfLifeClass {
	int[][] currentBoard;
	private final int boardSize = 75;
	private Random rnd;
	private final FixOverFlows fix;

	/**
	 * @return
	 * returns the current Java.Util.Random the game is using.
	 */
	public Random getRnd() {
		return rnd;
	}

	/**
	 * @param rnd
	 * sets the game's java.util.random
	 */
	@SuppressWarnings("unused") //may use this in the future, idk, don't wanna get rid of it now.
	public void setRnd(Random rnd) {
		this.rnd = rnd;
	}

	/**
	 * Non-parameterized constructor, initializes the game and sets the board size to the default of 75*75
	 */
	public GameOfLifeClass() {
		super();
		fix = new FixOverFlows();
		currentBoard = new int[boardSize][boardSize];
		rnd = new Random(75);/* temporary value for development purposes */
	}

	/**
	 * @return returns the size of the gameboard.
	 */
	@SuppressWarnings("unused") //may use this in the future, idk, don't wanna get rid of it now.
	public int getBoardSize() {
		return boardSize;
	}

	/**
	 *
	 * @return
	 * generates a new, random board
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
	 * returns the current game board
	 */
	public int[][] GetCurrentBoard() {
		return currentBoard;
	}

	/**
	 * @param newBoard
	 * updates the game board.
	 */
	public void SetCurrentBoard(int[][] newBoard) {
		this.currentBoard = newBoard;
	}

	/**
	 * @param showDying
	 * takes a boolean input, switches the logic of the run function based on the boolean input.
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
	 * the current state of the current cell
	 * @param col
	 * the current column position of the cell.
	 * @param row
	 * the current row position of the cell.
	 * @return
	 * returns whether the cell is Alive, Dead, or a Ghost.
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
		return cellState;
	}

	/**
	 * @param cellState
	 * the current state of the current cell
	 * @param col
	 * the current column position of the cell.
	 * @param row
	 * * the current row position of the cell.
	 * @return
	 * returns whether the cell is alive or dead.
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
			return 0;
		}
		return cellState;
	}

	/**
	 * @param row
	 * takes the current row of a cell
	 * @param col
	 * takes the current column of a cell
	 * @return
	 * returns the integer number of living cells surrounding the location specified by both parameters.
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
