package application;

import javafx.scene.paint.Color;

public enum FEnum {
	SCENE_WIDTH(900),
	SCENE_HEIGHT(450),
	//this is a separate number from PANE_HEIGHT to account for the size of the window's top bar.
	PANE_WIDTH(450),
	PANE_HEIGHT(450),
	BUTTON_WIDTH(175),
	GAME_SIZE(75),
	LIVING_CELL(2, Color.WHITE),
	DYING_CELL(1, Color.GRAY),
	DEAD_CELL(0, Color.BLACK),
	HIGHLIGHT_COLOR(Color.BLUE);

	private int value;
	private Color color;

	FEnum(int value) {
		this.value = value;
	}
	FEnum(Color color) {
		this.color = color;
	}

	FEnum(int value, Color color) {
		this.value = value;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getInt() {
		return value;
	}
}
