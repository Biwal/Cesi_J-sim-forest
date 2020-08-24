package fr.jSlim.models.grid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.jSlim.controller.HomepageController;
import fr.jSlim.models.cell.CellNode;
import fr.jSlim.models.cell.Square;
import fr.jSlim.models.cell.SquareImpl;
import fr.jSlim.models.enums.State;
import javafx.scene.layout.GridPane;


public class GridImpl extends GridPane implements Grid {

	private List<State> startingNumber ;
	private List<Square> squareGrid;
	private List<CellNode> nodeGrid ;
	private int columns;
	private int rows ;

	public GridImpl(int columns, int rows) {

		this.columns = columns;
		this.rows = rows;
		this.squareGrid = buildGrid();
	}


    public GridImpl(int row, int col, double parentW, double parentH, HomepageController controller) {
    	
    	setHgap(0);
		setVgap(0);
		setGridLinesVisible(false);

//		controller.configuration = new Configuration(controller.getCycleNumber(), row, col);
		columns = col;
		rows = row;
		squareGrid = new ArrayList<>();
		nodeGrid = new ArrayList<>();

		double width = parentW / row;
		if (width < 5) {
			width = 5;
		}
		double height = parentH / col;
		if (height < 5) {
			height = 5;
		}

		for (int x = 0; x < row; x++) {
			for (int y = 0; y < col; y++) {
				CellNode cell = new CellNode(x,y,width,height,controller);
				
				squareGrid.add(cell.getSquare());
				nodeGrid.add(cell);
				add(cell, y, x);
			}
		}
	}

	//
//	public GridImpl(State currentType)  {
//		this.startingNumber = new ArrayList<State>();
//		this.squareGrid = null;
//
//	}
//	public GridImpl(int columns, int rows, State currentType) {
////		this.startingNumber = testState();
//		this.columns = columns;
//		this.rows = rows;
//		this.squareGrid = buildGrid();
//	}
//
	public List<Square> buildGrid() {
		List<Square> gridFormation = new LinkedList<>();
		int idCase = 0;
		for (int row = 1; row <= this.rows; row++) {
			for (int column = 1; column <= this.columns; column++) {
				gridFormation.add(new SquareImpl((idCase), State.VOID, column, row));
				idCase += 1;
			}
		}
		return gridFormation;
	}

//
//		List<Square> gridSquare = new LinkedList<>();
//		int idCase = 0;
//		for (int row = 1; row <= this.rows; row++) {
//			for (int column = 1; column <= this.columns; column++) {
//				gridSquare.add(new SquareImpl((idCase), State.VOID, column, row));
//				idCase += 1;
//			}
//		}
//		return gridSquare;
//	}

	public List<Square> getSquareGrid() {
		return squareGrid;
	}

	public void setSquareGrid(List<Square> listCases) {
		this.squareGrid = listCases;
	}

	public List<State> getStartingNumber() {
		return startingNumber;
	}

	public void setStartingNumber(List<State> startingNumber) {
		this.startingNumber = startingNumber;
	}

	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

	public Iterable<? extends CellNode> getCellNode() {
		return nodeGrid;
	}


	@Override
	public Square getSquare(int row, int col) {
		for (Square square : squareGrid) {
			if(row == square.getRow() && col == square.getColumn()) {
				return square;
			}
		}
		throw new RuntimeException("ya un bug");
	}
}
