package application;

import java.util.Random;

import static application.FEnum.GAME_SIZE;
import static application.FEnum.LIVING_CELL;

public class GameOfLifeClass {
	private final FixOverFlows fix;
	int[][] currentBoard;
	private Random rnd;

	/**
	 * Non-parameterized constructor, initializes the game and sets the board size to the size
	 * specified in the FEnum file.
	 */
	public GameOfLifeClass() {
		super();
		fix = new FixOverFlows();
		currentBoard = new int[GAME_SIZE.getInt()][GAME_SIZE.getInt()];
		rnd = new Random(75);/* temporary value for development purposes */
	}

	/**
	 * @return returns the current Java.Util.Random the game is using.
	 */
	public Random getRnd() {
		return rnd;
	}

	/**
	 * @param rnd sets the game's java.util.random
	 */
	@SuppressWarnings("unused") //may use this in the future, IDK, don't want to get rid of it now.
	public void setRnd(Random rnd) {
		this.rnd = rnd;
	}

	/**
	 * @return returns the size of the game-board.
	 */
	@SuppressWarnings("unused") //may use this in the future, IDK, don't want to get rid of it now.
	public int getBoardSize() {
		return GAME_SIZE.getInt();
	}

	/**
	 * @return generates a new, random board
	 */
	public int[][] GenerateBoard() {
		int[][] temp = new int[GAME_SIZE.getInt()][GAME_SIZE.getInt()];
		for (int row = 0; row < GAME_SIZE.getInt(); row++) {
			for (int col = 0; col < GAME_SIZE.getInt(); col++) {
				int next = rnd.nextInt(LIVING_CELL.getInt());
				if (next == 1) next++;
				temp[row][col] = next;
			}
		}
		currentBoard = temp;
		return temp;
	}

	/**
	 * @return returns the current game board
	 */
	public int[][] GetCurrentBoard() {
		return currentBoard;
	}

	/**
	 * @param newBoard updates the game board.
	 */
	public void SetCurrentBoard(int[][] newBoard) {
		this.currentBoard = newBoard;
	}

	/**
	 * @param showDying takes a boolean input, switches the logic of the run function based on the
	 *                    boolean input.
	 */
	public void RunOneGen(boolean showDying) {
		int[][] nextBoard = new int[GAME_SIZE.getInt()][GAME_SIZE.getInt()];
		for (int i = 0; i < GAME_SIZE.getInt(); i++) {
			for (int j = 0; j < GAME_SIZE.getInt(); j++) {
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
	 * @param cellState the current state of the current cell
	 * @param x         the current x position of the cell.
	 * @param y         the current y position of the cell.
	 * @return returns next cell state based on my ghost inclusive version of conway's game of life
	 * rules.
	 */
	private int FindNextDying(int cellState, int x, int y) {
		int numOfAdjacent = CheckNearCells(x, y);
		if ((numOfAdjacent < 2 || numOfAdjacent > 3) && cellState != 0) {
			return cellState - 1;
		}
		if (numOfAdjacent == 3) {
			return LIVING_CELL.getInt();
		}
		if (numOfAdjacent == 2 && cellState == 1) {
			return 0;
		}
		return cellState;
	}

	/**
	 * @param cellState the current state of the current cell
	 * @param x         the x position of the current cell.
	 * @param y         * the y position of the current cell.
	 * @return returns next cell state based on vanilla game of life rules.
	 */
	private int FindNextState(int cellState, int x, int y) {
		int numOfAdjacent = CheckNearCells(x, y);
		if (numOfAdjacent < 2 || numOfAdjacent > 3) {
			return 0;
		}
		if (numOfAdjacent == 3) {
			return LIVING_CELL.getInt();
		}
		if (cellState == 1) {
			return 0;
		}
		return cellState;
	}

	/**
	 * @param x takes the current x position of a cell
	 * @param y takes the current y position of a cell
	 * @return returns the number of living cells surrounding the cell's grid location.
	 */
	private int CheckNearCells(int x, int y) {
		int count = 0;
		if (currentBoard[fix.FixFlow(x - 1, GAME_SIZE.getInt())][fix.FixFlow(y - 1,
				GAME_SIZE.getInt())] == LIVING_CELL.getInt())
		{
			count++;
		}
		if (currentBoard[fix.FixFlow(x - 1, GAME_SIZE.getInt())][fix.FixFlow(y, GAME_SIZE.getInt())] ==
				LIVING_CELL.getInt())
		{
			count++;
		}
		if (currentBoard[fix.FixFlow(x - 1, GAME_SIZE.getInt())][fix.FixFlow(y + 1,
				GAME_SIZE.getInt())] == LIVING_CELL.getInt())
		{
			count++;
		}
		if (currentBoard[fix.FixFlow(x, GAME_SIZE.getInt())][fix.FixFlow(y - 1, GAME_SIZE.getInt())] ==
				LIVING_CELL.getInt())
		{
			count++;
		}
		if (currentBoard[fix.FixFlow(x, GAME_SIZE.getInt())][fix.FixFlow(y + 1, GAME_SIZE.getInt())] ==
				LIVING_CELL.getInt())
		{
			count++;
		}
		if (currentBoard[fix.FixFlow(x + 1, GAME_SIZE.getInt())][fix.FixFlow(y - 1,
				GAME_SIZE.getInt())] == LIVING_CELL.getInt())
		{
			count++;
		}
		if (currentBoard[fix.FixFlow(x + 1, GAME_SIZE.getInt())][fix.FixFlow(y + 1,
				GAME_SIZE.getInt())] == LIVING_CELL.getInt())
		{
			count++;
		}
		if (currentBoard[fix.FixFlow(x + 1, GAME_SIZE.getInt())][fix.FixFlow(y, GAME_SIZE.getInt())] ==
				LIVING_CELL.getInt())
		{
			count++;
		}
		return count;
	}

}
