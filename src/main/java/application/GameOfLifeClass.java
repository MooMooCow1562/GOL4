package application;

import java.util.Random;

import static application.FEnum.*;

public class GameOfLifeClass {
	private final FixOverFlows fix;
	GameInteger[][] currentBoard;
	private Random rnd;

	/**
	 * Non-parameterized constructor, initializes the game and sets the board size to the size
	 * specified in the FEnum file.
	 */
	public GameOfLifeClass() {
		super();
		fix = new FixOverFlows();
		currentBoard = new GameInteger[GAME_SIZE.getInt()][GAME_SIZE.getInt()];
		for (int row = 0; row < GAME_SIZE.getInt(); row++){
			for (int col = 0; col < GAME_SIZE.getInt(); col++){
				currentBoard[row][col] = new GameInteger(DEAD_CELL.getInt());
			}
		}
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
	public GameInteger[][] GenerateBoard() {
		GameInteger[][] temp = new GameInteger[GAME_SIZE.getInt()][GAME_SIZE.getInt()];

		for (int row = 0; row < GAME_SIZE.getInt(); row++) {
			for (int col = 0; col < GAME_SIZE.getInt(); col++) {
				int next = rnd.nextInt(LIVING_CELL.getInt());
				if (next == 1) next++;
				temp[row][col] = new GameInteger(next);
			}
		}
		currentBoard = temp;
		return temp;
	}

	/**
	 * @return returns the current game board
	 */
	public GameInteger[][] GetCurrentBoard() {
		return currentBoard;
	}

	/**
	 * @param newBoard updates the game board.
	 */
	public void SetCurrentBoard(GameInteger[][] newBoard) {
		this.currentBoard = newBoard;
	}

	/**
	 * @param showDying takes a boolean input, switches the logic of the run function based on the
	 *                    boolean input.
	 */
	public void RunOneGen(boolean showDying) {
		GameInteger[][] nextBoard = new GameInteger[GAME_SIZE.getInt()][GAME_SIZE.getInt()];
		for (int i = 0; i < GAME_SIZE.getInt(); i++) {
			for (int j = 0; j < GAME_SIZE.getInt(); j++) {
				nextBoard[j][i] = FindNext(currentBoard[j][i], j, i, showDying);
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
	private GameInteger FindNext(GameInteger cellState, int x, int y, boolean showDying) {
		GameInteger numOfAdjacent = CheckNearCells(x, y);
		boolean shouldLive, shouldComeAlive;
		shouldLive = false;
		shouldComeAlive = false;

		for (int rule : SURVIVAL_RULES.getIntArray()){
			if (numOfAdjacent.getVALUE() == rule) {
				shouldLive = true;
				break;
			}
		}

		for (int rule : APPEARANCE_RULES.getIntArray()){
			if (numOfAdjacent.getVALUE() == rule){
				shouldComeAlive = true;
				break;
			}
		}

		if ((!shouldLive)) {
			if (showDying && cellState.getVALUE() != 0)
				return new GameInteger(cellState.getVALUE()-1);
			return new GameInteger(DEAD_CELL.getInt());
		}

		if (shouldComeAlive) {
			return new GameInteger(LIVING_CELL.getInt());
		}

		if (cellState.getVALUE() == DYING_CELL.getInt()) {
			return new GameInteger(DEAD_CELL.getInt());
		}

		return cellState;
	}

	/**
	 * @param x takes the current x position of a cell
	 * @param y takes the current y position of a cell
	 * @return returns the number of living cells surrounding the cell's grid location.
	 */
	private GameInteger CheckNearCells(int x, int y) {
		int halfRange = SEARCH_RANGE.getInt()/2;
		int count = 0;
		for (int row = -halfRange; row < SEARCH_RANGE.getInt() - halfRange; row++) {
			for (int col = -halfRange; col < SEARCH_RANGE.getInt() - halfRange; col++) {
				boolean isAlive = (currentBoard[fix.FixFlow(x+row, GAME_SIZE.getInt())][fix.FixFlow(y+col,
						GAME_SIZE.getInt())].getVALUE() == 2);
				boolean isNotSelf = (!currentBoard[fix.FixFlow(x+row, GAME_SIZE.getInt())][fix.FixFlow(y+col,
						GAME_SIZE.getInt())].equals(currentBoard[fix.FixFlow(x, GAME_SIZE.getInt())][fix.FixFlow(y,
						GAME_SIZE.getInt())]));
				if (isAlive && isNotSelf){
					count++;
				}
			}
		}
		return new GameInteger(count);
	}
}
