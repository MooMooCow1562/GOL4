package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Scanner;

import static application.FEnum.*;

public class Main extends Application {
	GameOfLifeClass game;
	Pane leftPaneGrid, rightPane, rightPaneLeft, rightPaneRight, rootPane;
	Scene scene;
	Accordion colorSelect;
	TextField seedHolder, customGenRuns, brushSize;
	ComboBox<String> livingColor, dyingColor, deadColor, highlightColor;
	boolean showPriorLiving;
	Button eStop;
	FixOverFlows fix;
	Timeline scheduled = new Timeline(new KeyFrame(Duration.seconds(Math.max((
			(Math.pow(((double) GAME_SIZE.getInt() / 75), Math.E)) / 10), .05)), _ -> {
		game.RunOneGen(showPriorLiving);
		makeBoard(game.GetCurrentBoard());
	}));

	public static void main(String[] args) {
		launch(args);
	}

	@Override public void start(Stage primaryStage) {
		try {
			// creating the bulk of our objects
			showPriorLiving = false;
			rightPane = new FlowPane();
			leftPaneGrid = new GridPane();
			rightPaneRight = new FlowPane();
			rightPaneLeft = new FlowPane();
			rootPane = new BorderPane(null, null, rightPane, null, leftPaneGrid);
			scene = new Scene(rootPane, SCENE_WIDTH.getInt(), SCENE_HEIGHT.getInt());
			game = new GameOfLifeClass();
			// setting sizes for the two grid elements
			rightPane.setMinSize(PANE_WIDTH.getInt(), PANE_HEIGHT.getInt());
			rightPane.setMaxSize(PANE_WIDTH.getInt(), PANE_HEIGHT.getInt());
			rightPaneRight.setMinSize(PANE_WIDTH.getInt() / 2.0 - 1, PANE_HEIGHT.getInt());
			rightPaneLeft.setMinSize(PANE_WIDTH.getInt() / 2.0 - 1, PANE_HEIGHT.getInt());
			((FlowPane) rightPaneRight).setOrientation(Orientation.VERTICAL);
			((FlowPane) rightPaneLeft).setOrientation(Orientation.VERTICAL);
			((FlowPane) rightPane).setOrientation(Orientation.HORIZONTAL);
			leftPaneGrid.setMinSize(PANE_WIDTH.getInt(), PANE_HEIGHT.getInt());
			leftPaneGrid.setMaxSize(PANE_WIDTH.getInt(), PANE_HEIGHT.getInt());

			// right-side gui
			// seed setter
			Button setSeed = new Button("Set new Seed");
			seedHolder = new TextField();
			setSeed.setOnAction(this::changeSeed);
			seedHolder.setPromptText("Please input integers only.");
			FlowPane seedPane = new FlowPane(seedHolder, setSeed);
			seedHolder.setPrefWidth(BUTTON_WIDTH.getInt());
			setSeed.setPrefWidth(BUTTON_WIDTH.getInt());
			// board generator
			Button genRand = new Button("Generate Random Board");
			genRand.setOnAction(this::generateRandomBoard);
			// run 1 generation
			Button run1Gen = new Button("Run 1 Generation");
			run1Gen.setOnAction(this::runOneGenPlease);
			// run 100 generations
			Button run100Gens = new Button("Run 100 Generations");
			run100Gens.setOnAction(this::runOneHundredGenPlease);
			// run custom # of generations
			customGenRuns = new TextField();
			customGenRuns.setPromptText("Please input integers only.");
			Button runCustomGens = new Button("Run custom # of Generations");
			runCustomGens.setOnAction(this::runCustomGenPlease);
			FlowPane customGenPane = new FlowPane(customGenRuns, runCustomGens);
			customGenRuns.setPrefWidth(BUTTON_WIDTH.getInt());
			runCustomGens.setPrefWidth(BUTTON_WIDTH.getInt());
			// toggle ghosts
			ToggleButton PriorLivingToggle = new ToggleButton("Activate Ghosts");
			PriorLivingToggle.setSelected(true);
			PriorLivingToggle.setOnAction(this::togglePriorLiving);
			// Emergency stop button
			eStop = new Button("Emergency Stop");
			eStop.setBackground(Background.fill(Color.BLACK));
			// board reset button
			Button resetBoard = new Button("reset the board");
			resetBoard.setOnAction(this::resetBoard);
			// changeBrushSize
			brushSize = new TextField("1");
			brushSize.setTooltip(new Tooltip(
					"The size of the brush \nMax of " + GAME_SIZE.getInt() + ", Min of 1"));
			Label brushText = new Label("Change the brush size");

			// setting things into the rightPaneRight
			rightPaneRight.getChildren()
					.addAll(new Label("Change the Seed"),
							seedPane,
							genRand,
							run1Gen,
							run100Gens,
							new Label("Set Custom Generation #"),
							customGenPane,
							eStop,
							resetBoard,
							PriorLivingToggle,
							brushText,
							brushSize,
							new Label("Please input integers only."));
			((Label) rightPaneRight.getChildren().getLast()).setTextFill(Color.GRAY);
			//bastardizing the justification of the children.
			((FlowPane) rightPaneRight).setAlignment(Pos.CENTER);

			// standardizing the widths of the UI elements.
			for (int i = 0; i < rightPaneRight.getChildren().size(); i++) {
				((Region) rightPaneRight.getChildren().get(i)).setPrefWidth(BUTTON_WIDTH.getInt());
			}

			//setting up rightPaneRight
			ObservableList<String> colorPick = FXCollections.observableArrayList("Black",
					"Gray",
					"White",
					"Red",
					"Orange",
					"Yellow",
					"Green",
					"Blue",
					"Purple");

			deadColor = new ComboBox<>(FXCollections.observableArrayList(new ArrayList<>(9)));
			deadColor.setOnAction(this::changePalette);
			deadColor.setId("Dead Cells");
			for (int i = 0; i < 9; i++) {
				deadColor.getItems().add(colorPick.get(i));
			}
			deadColor.getSelectionModel().select(0);

			dyingColor = new ComboBox<>(FXCollections.observableArrayList(new ArrayList<>(9)));
			dyingColor.setOnAction(this::changePalette);
			dyingColor.setId("Dying Cells");
			for (int i = 0; i < 9; i++) {
				dyingColor.getItems().add(colorPick.get(i));
			}
			dyingColor.getSelectionModel().select(1);

			livingColor = new ComboBox<>(FXCollections.observableArrayList(new ArrayList<>(9)));
			livingColor.setOnAction(this::changePalette);
			livingColor.setId("Living Cells");
			for (int i = 0; i < 9; i++) {
				livingColor.getItems().add(colorPick.get(i));
			}
			livingColor.getSelectionModel().select(2);

			highlightColor = new ComboBox<>(FXCollections.observableArrayList(new ArrayList<>(9)));
			highlightColor.setOnAction(this::changePalette);
			highlightColor.setId("Cell Highlighter");
			for (int i = 0; i < 9; i++) {
				highlightColor.getItems().add(colorPick.get(i));
			}
			highlightColor.getSelectionModel().select(7);

			colorSelect = new Accordion(new TitledPane("Living Cells", livingColor),
					new TitledPane("Dying Cells", dyingColor),
					new TitledPane("Dead Cells", deadColor),
					new TitledPane("Cell Highlighter", highlightColor));
			rightPaneLeft.getChildren().addAll(new Label("Change Simulation Colors!"), colorSelect);
			((FlowPane) rightPaneLeft).setAlignment(Pos.CENTER);

			rightPane.getChildren().addAll(rightPaneRight, rightPaneLeft);
			((Region) rightPaneLeft.getChildren().getFirst()).setPrefWidth(BUTTON_WIDTH.getInt());
			// creating the grid.
			int[][] initialGrid = new int[GAME_SIZE.getInt()][GAME_SIZE.getInt()];
			makeBoard(initialGrid);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Game Of Life GUI version 4.0 (Color update!)");
			// prevent people from messing with the window
			primaryStage.setResizable(false);
		} catch (Exception e) {
			//noinspection CallToPrintStackTrace
			e.printStackTrace();
		}
	}

	private void changePalette(ActionEvent actionEvent) {
		String colorToChange;
		colorToChange = ((ComboBox<String>) actionEvent.getSource()).getValue();
		if (((ComboBox<?>) actionEvent.getSource()).getId().equals("Living Cells")) {
			LIVING_CELL.setColor(Color.valueOf(colorToChange.toUpperCase()));
		} else if (((ComboBox<?>) actionEvent.getSource()).getId().equals("Dying Cells")) {
			DYING_CELL.setColor(Color.valueOf(colorToChange.toUpperCase()));
		} else if (((ComboBox<?>) actionEvent.getSource()).getId().equals("Dead Cells")) {
			DEAD_CELL.setColor(Color.valueOf(colorToChange.toUpperCase()));
		} else {
			HIGHLIGHT_COLOR.setColor(Color.valueOf(colorToChange.toUpperCase()));
		}
		makeBoard(game.GetCurrentBoard());
	}

	/**
	 * @param resetButtonPressed resets the board.
	 */
	private void resetBoard(ActionEvent resetButtonPressed) {
		eStopProcessor(null);
		game.SetCurrentBoard(new int[GAME_SIZE.getInt()][GAME_SIZE.getInt()]);
		int[][] temp = game.GetCurrentBoard();
		makeBoard(temp);
	}

	/**
	 * @param randomBoardButton Generates a random board.
	 */
	private void generateRandomBoard(ActionEvent randomBoardButton) {
		int[][] temp = game.GenerateBoard();
		makeBoard(temp);
	}

	/**
	 * @param toggleEvent toggles the ghost cells.
	 */
	private void togglePriorLiving(ActionEvent toggleEvent) {
		if (showPriorLiving) {
			((ToggleButton) toggleEvent.getSource()).setText("Activate Ghosts");
		}
		if (!showPriorLiving) {
			((ToggleButton) toggleEvent.getSource()).setText("Deactivate Ghosts");
		}
		showPriorLiving = !showPriorLiving;
	}

	/**
	 * @param changeSeedButtonEvent changes the seed.
	 */
	private void changeSeed(ActionEvent changeSeedButtonEvent) {
		game.getRnd().setSeed(Long.parseLong(seedHolder.getText()));
	}

	/**
	 * @param runButtonPressed proceeds one game state.
	 */
	private void runOneGenPlease(ActionEvent runButtonPressed) {
		game.RunOneGen(showPriorLiving);
		makeBoard(game.GetCurrentBoard());
	}

	/**
	 * @param runButtonPressed proceeds 100 game states
	 */
	private void runOneHundredGenPlease(ActionEvent runButtonPressed) {
		scheduled.setCycleCount(100);
		scheduled.play();
		eStop.setOnAction(this::eStopProcessor);
		eStop.setBackground(Background.fill(Color.CRIMSON));
		eStop.setCursor(Cursor.OPEN_HAND);
		scheduled.setOnFinished(this::eStopProcessor);
	}

	/**
	 * @param runButtonPressed runs as many game states as directed to.
	 */
	private void runCustomGenPlease(ActionEvent runButtonPressed) {
		scheduled.setCycleCount(Integer.parseInt(customGenRuns.getText()));
		scheduled.play();
		eStop.setOnAction(this::eStopProcessor);
		eStop.setBackground(Background.fill(Color.CRIMSON));
		eStop.setCursor(Cursor.OPEN_HAND);
		scheduled.setOnFinished(this::eStopProcessor);
	}

	/**
	 * @param eStopEvent stops the game from running.
	 */
	private void eStopProcessor(ActionEvent eStopEvent) {
		eStop.setCursor(Cursor.DEFAULT);
		scheduled.stop();
		eStop.setOnAction(null);
		eStop.setBackground(Background.fill(Color.BLACK));
	}

	/**
	 * @param click changes the color of a lifecell.
	 */
	private void changeColor(MouseEvent click) {
		Pane lifeCell = (Pane) click.getSource();
		String cellLocation;
		fix = new FixOverFlows();
		int[][] currentVisible = game.GetCurrentBoard();
		cellLocation = lifeCell.getId();
		Scanner scnr = new Scanner(cellLocation);
		if (Integer.parseInt(brushSize.getText()) > GAME_SIZE.getInt()) {
			brushSize.setText("" + GAME_SIZE.getInt());
		} else if (Integer.parseInt(brushSize.getText()) < 1) {
			brushSize.setText("" + 1);
		}
		int x = scnr.nextInt();
		int y = scnr.nextInt();
		switch (currentVisible[x][y]) {
			case 0://dead
				for (int col = Integer.parseInt(brushSize.getText()); col > 0; col--) {
					for (int row = Integer.parseInt(brushSize.getText()); row > 0; row--) {
						scnr.close();
						scnr = new Scanner(cellLocation);
						currentVisible[fix.FixFlow(x +
										(row - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)),
								GAME_SIZE.getInt())][fix.FixFlow(y +
										(col - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)),
								GAME_SIZE.getInt())] = LIVING_CELL.getInt();
					}

					game.SetCurrentBoard(currentVisible);
					makeBoard(game.GetCurrentBoard());
				}
				break;
			case 1://dying
				for (int col = Integer.parseInt(brushSize.getText()); col > 0; col--) {
					for (int row = Integer.parseInt(brushSize.getText()); row > 0; row--) {
						scnr.close();
						scnr = new Scanner(cellLocation);
						currentVisible[fix.FixFlow(x +
										(row - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)),
								GAME_SIZE.getInt())][fix.FixFlow(y +
										(col - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)),
								GAME_SIZE.getInt())] = DEAD_CELL.getInt();
					}

					game.SetCurrentBoard(currentVisible);
					makeBoard(game.GetCurrentBoard());
				}
				break;
			case 2: //living
				for (int col = Integer.parseInt(brushSize.getText()); col > 0; col--) {
					for (int row = Integer.parseInt(brushSize.getText()); row > 0; row--) {
						scnr.close();
						scnr = new Scanner(cellLocation);
						currentVisible[fix.FixFlow(x +
										(row - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)),
								GAME_SIZE.getInt())][fix.FixFlow(y +
										(col - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)),
								GAME_SIZE.getInt())] = DYING_CELL.getInt();
					}

					game.SetCurrentBoard(currentVisible);
					makeBoard(game.GetCurrentBoard());
				}
				break;
		}
		scnr.close();
	}

	/**
	 * @param temporaryBoard creates the next board.
	 */
	private void makeBoard(int[][] temporaryBoard) {
		leftPaneGrid = new GridPane();
		leftPaneGrid.setBackground(Background.fill(DEAD_CELL.getColor()));
		leftPaneGrid.setMinSize(PANE_WIDTH.getInt(), PANE_HEIGHT.getInt());
		leftPaneGrid.setMaxSize(PANE_WIDTH.getInt(), PANE_HEIGHT.getInt());
		((BorderPane) rootPane).setLeft(leftPaneGrid);
		leftPaneGrid.setSnapToPixel(false);

		for (int j = 0; j < GAME_SIZE.getInt(); j++) {
			for (int i = 0; i < GAME_SIZE.getInt(); i++) {
				// create and add panes
				Pane lifeCell = new Pane();
				//lifeCell.setSnapToPixel(false);
				lifeCell.setMinHeight(((double) PANE_HEIGHT.getInt()) / GAME_SIZE.getInt());
				lifeCell.setMinWidth(((double) PANE_HEIGHT.getInt()) / GAME_SIZE.getInt());
				lifeCell.setId(i + " " + j);
				if (temporaryBoard[i][j] == DEAD_CELL.getInt()) {
					lifeCell.setBackground(Background.fill(DEAD_CELL.getColor()));
				}
				if (temporaryBoard[i][j] == DYING_CELL.getInt()) {
					lifeCell.setBackground(Background.fill(DYING_CELL.getColor()));
				}
				if (temporaryBoard[i][j] == LIVING_CELL.getInt()) {
					lifeCell.setBackground(Background.fill(LIVING_CELL.getColor()));
				}
				lifeCell.setBorder(new Border(new BorderStroke(Color.GRAY,
						BorderStrokeStyle.SOLID,
						null,
						new BorderWidths(0))));
				lifeCell.setOnMouseClicked(this::changeColor);
				lifeCell.setOnMouseEntered(this::highlight);
				lifeCell.setOnMouseExited(this::deHighlight);
				GridPane.setConstraints(lifeCell, j, i);
				leftPaneGrid.getChildren().add(lifeCell);
				// iterate
			}
		}
	}

	private void highlight(MouseEvent mouseEvent) {
		Pane cell = (Pane) mouseEvent.getSource();
		cell.setBorder(new Border(new BorderStroke(HIGHLIGHT_COLOR.getColor(),
				BorderStrokeStyle.SOLID,
				null,
				new BorderWidths(1))));
	}

	private void deHighlight(MouseEvent mouseEvent) {
		Pane cell = (Pane) mouseEvent.getSource();
		cell.setBorder(new Border(new BorderStroke(HIGHLIGHT_COLOR.getColor(),
				BorderStrokeStyle.SOLID,
				null,
				new BorderWidths(0))));
	}
}