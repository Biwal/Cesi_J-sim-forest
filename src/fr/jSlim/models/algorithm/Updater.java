package fr.jSlim.models.algorithm;

import java.util.List;

import fr.jSlim.models.cell.Square;

public interface Updater {

	public List<Square> update(List<Square> listCasesInit);

	public List<Square> getSquaresToCheck(List<Square> listSquaresInit, int squareColumn, int squareRow);

	public int getColumnMin(int squareColumn);

	public int getColumnMax(int squareColumn);

	public int getRowMin(int squareRow);

	public int getRowMax(int squareRow);

	public int getColumns();

	public void setColumns(int columns) ;

	public int getRows() ;

	public void setRows(int rows) ;

}
