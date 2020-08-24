package fr.jSlim.models.grid;

import java.util.List;

import fr.jSlim.models.cell.Square;

public interface Grid {

	public List<Square> buildGrid();

	//public String getStartingNumbers(int numGrid) throws IOException;

	public List<Square> getSquareGrid();

	public void setSquareGrid(List<Square> listSquares);
	
	public Square getSquare(int row,int col);
//
//	public Solver getSolverSudoku();
//
//	public void setSolverSudoku(Solver solverSudoku);
//
//	public String getStartingNumber();
//
//	public void setStartingNumber(String startingNumber);

	@Override
	public String toString();

}
