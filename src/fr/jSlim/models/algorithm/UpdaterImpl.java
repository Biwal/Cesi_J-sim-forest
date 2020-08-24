package fr.jSlim.models.algorithm;

import java.util.ArrayList;
import java.util.List;

import fr.jSlim.models.cell.Square;
import fr.jSlim.models.cell.SquareImpl;

public abstract class UpdaterImpl implements Updater {
	
 	private int columns;
	private int rows;
	
	public abstract List<Square> determineSquaresToCheck(List<Square> squaresToCheck, Square square, int columnMin,
			int columnMax, int rowMin, int rowMax, int squareColumn, int squareRow);
	
	public abstract Square updateSquare(Square squareToUpdate, List<Square> squareToCheck);

	public List<Square> update(List<Square> listSquaresInit) {
		List<Square> listSquareUpdated = new ArrayList<Square>();
		for (int i = 0; i < listSquaresInit.size(); i++) {

			Square squareToUpdate = new SquareImpl();
			squareToUpdate.setColumn(listSquaresInit.get(i).getColumn());
			squareToUpdate.setRow(listSquaresInit.get(i).getRow());
			squareToUpdate.setGrowthShrub(listSquaresInit.get(i).isGrowthShrub());
			squareToUpdate.setState(listSquaresInit.get(i).getState());
			squareToUpdate.setPosition(listSquaresInit.get(i).getPosition());
			squareToUpdate.setConfiguration(listSquaresInit.get(i).getConfiguration());
			squareToUpdate.setGrowthShrub(!(listSquaresInit.get(i).isGrowthShrub()));

			List<Square> squareToCheck = getSquaresToCheck(listSquaresInit, squareToUpdate.getColumn(),
					squareToUpdate.getRow());
			Square squareUpdated = updateSquare(squareToUpdate, squareToCheck);
			listSquareUpdated.add(squareUpdated);
		}
		return listSquareUpdated;
	}

	public List<Square> getSquaresToCheck(List<Square> listSquaresInit, int squareColumn, int squareRow) {
		List<Square> squaresToCheck = new ArrayList<Square>();
		int columnMin = getColumnMin(squareColumn);
		int columnMax = getColumnMax(squareColumn);
		int rowMin = getRowMin(squareRow);
		int rowMax = getRowMax(squareRow);
		for (int j = 0; j < listSquaresInit.size(); j++) {
			squaresToCheck = determineSquaresToCheck(squaresToCheck, listSquaresInit.get(j), columnMin, columnMax,
					rowMin, rowMax, squareColumn, squareRow);
		}
		return squaresToCheck;
	}

	public int getColumnMin(int squareColumn) {
		if ((squareColumn) - 1 < 1) {
			return squareColumn;
		} else {
			return (squareColumn - 1);
		}
	}

	public int getColumnMax(int squareColumn) {
		if ((squareColumn) + 1 > this.columns) {
			return squareColumn;
		} else {
			return (squareColumn + 1);
		}
	}

	public int getRowMin(int squareRow) {
		if ((squareRow) - 1 < 1) {
			return squareRow;

		} else {
			return (squareRow - 1);
		}
	}

	public int getRowMax(int squareRow) {
		if ((squareRow) + 1 > this.rows) {
			return squareRow;
		} else {
			return (squareRow + 1);
		}
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

}
