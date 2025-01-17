package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Scanner;

public class Main extends Application {
	GameOfLifeClass game;
	Pane leftPane, rightPane, rootPane;
	Scene scene;
	TextField seedHolder, customGenRuns, brushSize;
	boolean showPriorLiving;
	Button eStop;
	FixOverFlows fix;
	Timeline scheduled = new Timeline(new KeyFrame(Duration.seconds(0.1), ev -> {
		game.RunOneGen(showPriorLiving);
		makeBoard(game.GetCurrentBoard());
	}));

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// creating the bulk of our objects
			showPriorLiving = false;
			rightPane = new FlowPane();
			leftPane = new GridPane();
			rootPane = new BorderPane(null, null, rightPane, null, leftPane);
			scene = new Scene(rootPane, 695, 480);
			game = new GameOfLifeClass();

			// setting sizes for the two grid elements
			rightPane.setMinSize(500, 480);
			rightPane.setMaxSize(500, 480);
			((FlowPane) rightPane).setOrientation(Orientation.VERTICAL);
			leftPane.setMinSize(500, 480);
			leftPane.setMaxSize(500, 480);

			// rightside gui
			// seed setter
			Button setSeed = new Button("Set new Seed");
			seedHolder = new TextField();
			setSeed.setOnAction(this::changeSeed);
			seedHolder.setPromptText("Please input integers only.");
			FlowPane seedPane = new FlowPane(seedHolder, setSeed);
			seedHolder.setPrefWidth(175);
			setSeed.setPrefWidth(175);
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
			customGenRuns.setPrefWidth(175);
			runCustomGens.setPrefWidth(175);
			// toggle ghosts
			ToggleButton PriorLivingToggle = new ToggleButton("Activate Ghosts");
			PriorLivingToggle.setSelected(true);
			PriorLivingToggle.setOnAction(this::togglePriorLiving);
			// Estop button
			eStop = new Button("Emergency Stop");
			eStop.setBackground(Background.fill(Color.BLACK));
			// board reset button
			Button resetBoard = new Button("reset the board");
			resetBoard.setOnAction(this::resetBoard);
			// changeBrushSize
			brushSize = new TextField("1");
			brushSize.setTooltip(new Tooltip("The size of the brush \nMax of 75, Min of 1"));
			Label brushText = new Label("Change the brush size");

			// setting things into the pane
			rightPane.getChildren().addAll(new Label("Change the Seed"), seedPane, genRand, run1Gen, run100Gens,
					new Label("Set Custom Generation #"), customGenPane, eStop, resetBoard, PriorLivingToggle, brushText,
					brushSize, new Label("Please input integers only."));

			((Label) rightPane.getChildren().getLast()).setTextFill(Color.GRAY);

			// standardizing the widths of the UI elements.
			for (int i = 0; i < rightPane.getChildren().size(); i++) {
				((Region) rightPane.getChildren().get(i)).setPrefWidth(175);
			}

			// creating the grid.
			int[][] initialGrid = new int[75][75];
			makeBoard(initialGrid);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Game Of Life GUI version 3.0 (UI update)");
			// prevent people from fucking with the window
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param resetButtonPressed
	 */
	private void resetBoard(ActionEvent resetButtonPressed) {
		eStopProcessor(null);
		game.SetCurrentBoard(new int[75][75]);
		int[][] temp = game.GetCurrentBoard();
		makeBoard(temp);
	}

	/**
	 * @param buttonEvent
	 */
	private void generateRandomBoard(ActionEvent buttonEvent) {
		int[][] temp = game.GenerateBoard();
		makeBoard(temp);
	}

	/**
	 * @param toggleEvent
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
	 * @param buttonEvent
	 */
	private void changeSeed(ActionEvent buttonEvent) {
		game.getRnd().setSeed(Long.parseLong(seedHolder.getText()));
	}

	/**
	 * @param buttonEvent
	 */
	private void runOneGenPlease(ActionEvent buttonEvent) {
		game.RunOneGen(showPriorLiving);
		makeBoard(game.GetCurrentBoard());
	}

	/**
	 * @param buttonEvent
	 */
	private void runOneHundredGenPlease(ActionEvent buttonEvent) {
		scheduled.setCycleCount(100);
		scheduled.play();
		eStop.setOnAction(this::eStopProcessor);
		eStop.setBackground(Background.fill(Color.CRIMSON));
		eStop.setCursor(Cursor.OPEN_HAND);
		scheduled.setOnFinished(this::eStopProcessor);
	}

	/**
	 * @param buttonEvent
	 */
	private void runCustomGenPlease(ActionEvent buttonEvent) {
		scheduled.setCycleCount(Integer.parseInt(customGenRuns.getText()));
		scheduled.play();
		eStop.setOnAction(this::eStopProcessor);
		eStop.setBackground(Background.fill(Color.CRIMSON));
		eStop.setCursor(Cursor.OPEN_HAND);
		scheduled.setOnFinished(this::eStopProcessor);
	}

	/**
	 * @param estopEvent
	 */
	private void eStopProcessor(ActionEvent estopEvent) {
		eStop.setCursor(Cursor.DEFAULT);
		scheduled.stop();
		eStop.setOnAction(null);
		eStop.setBackground(Background.fill(Color.BLACK));
	}

	/**
	 * @param click
	 */
	private void changeColor(MouseEvent click) {
		Pane lifeCell = (Pane) click.getSource();
		String cellLocation;
		fix = new FixOverFlows();
		int[][] currentVisible = game.GetCurrentBoard();
		cellLocation = lifeCell.getId();
		Scanner scnr = new Scanner(cellLocation);
		if (Integer.parseInt(brushSize.getText()) > 75) {
			brushSize.setText("" + 75);
		}
		if (Integer.parseInt(brushSize.getText()) < 1) {
			brushSize.setText("" + 1);
		}
		int x = scnr.nextInt();
		int y = scnr.nextInt();
		switch (currentVisible[x][y]) {
			case 0:
				for (int col = Integer.parseInt(brushSize.getText()); col > 0; col--) {
					for (int row = Integer.parseInt(brushSize.getText()); row > 0; row--) {
						scnr.close();
						scnr = new Scanner(cellLocation);
						currentVisible[fix.FixFlow(x + (row - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)), 75)][fix.FixFlow(y + (col - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)), 75)] = 2;
					}

					game.SetCurrentBoard(currentVisible);
					makeBoard(game.GetCurrentBoard());
				}
				break;
			case 1:
				for (int col = Integer.parseInt(brushSize.getText()); col > 0; col--) {
					for (int row = Integer.parseInt(brushSize.getText()); row > 0; row--) {
						scnr.close();
						scnr = new Scanner(cellLocation);
						currentVisible[fix.FixFlow(x + (row - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)), 75)][fix.FixFlow(y + (col - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)), 75)] = 0;
					}

					game.SetCurrentBoard(currentVisible);
					makeBoard(game.GetCurrentBoard());
				}
				break;
			case 2:
				for (int col = Integer.parseInt(brushSize.getText()); col > 0; col--) {
					for (int row = Integer.parseInt(brushSize.getText()); row > 0; row--) {
						scnr.close();
						scnr = new Scanner(cellLocation);
						currentVisible[fix.FixFlow(x + (row - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)), 75)][fix.FixFlow(y + (col - (int) Math.ceil((Integer.parseInt(brushSize.getText()) + .1) / 2.0)), 75)] = 1;
					}

					game.SetCurrentBoard(currentVisible);
					makeBoard(game.GetCurrentBoard());
				}
				break;
		}
		scnr.close();
	}

	/**
	 * @param temporaryBoard
	 */
	private void makeBoard(int[][] temporaryBoard) {
		leftPane = new GridPane();
		leftPane.setMinSize(500, 480);
		leftPane.setMaxSize(500, 480);
		((BorderPane) rootPane).setLeft(leftPane);

		for (int j = 0; j < 75; j++) {
			for (int i = 0; i < 75; i++) {
				// create and add panes
				Pane lifeCell = new Pane();
				lifeCell.setMinHeight(9 * (2.0 / 3));
				lifeCell.setMinWidth(9 * (2.0 / 3));
				lifeCell.setId(i + " " + j);
				if (temporaryBoard[i][j] == 0) {
					lifeCell.setBackground(Background.fill(Color.BLACK));
				}
				if (temporaryBoard[i][j] == 1) {
					lifeCell.setBackground(Background.fill(Color.GREY));
				}
				if (temporaryBoard[i][j] == 2) {
					lifeCell.setBackground(Background.fill(Color.WHITE));
				}
				lifeCell.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null,
						new BorderWidths(0.25))));
				lifeCell.setOnMouseClicked(this::changeColor);
				GridPane.setConstraints(lifeCell, j, i);
				leftPane.getChildren().add(lifeCell);
				// iterate
			}
		}
	}
}