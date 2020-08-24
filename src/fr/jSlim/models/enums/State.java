package fr.jSlim.models.enums;

import javafx.scene.paint.Color;

public enum State {
	
	INVALID(' ',Color.BLACK),
	VOID('.',Color.BURLYWOOD),
	SPROUT('1',Color.LIGHTGREEN),
	SHRUB('2',Color.GREEN),
	TREE('3',Color.DARKGREEN),
	FIRE('f',Color.RED),
	ASH('c',Color.GRAY),
	INSECTS('i',Color.DEEPPINK);

	private char symbol;
	private Color color;

	State(char symbol, Color color){

		this.setSymbol(symbol);
		this.setColor(color);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public char getSymbol() {
		return symbol;
	}
	
	public static State getStateBySymbol(char test) {
		
		for (State tmp : State.values()) {
			if (test ==tmp.getSymbol()){
				return tmp;
			}
		}
		
		return null;
		
		
	}

	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
	
}
