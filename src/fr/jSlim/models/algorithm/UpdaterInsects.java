package fr.jSlim.models.algorithm;

import java.util.List;

import fr.jSlim.models.cell.Square;
import fr.jSlim.models.enums.State;

public class UpdaterInsects extends UpdaterImpl {

		public UpdaterInsects(int columns, int rows) {
		super.setColumns(columns);
		super.setRows(rows);
	}

	@Override
	public List<Square> determineSquaresToCheck(List<Square> squaresToCheck, Square square, int columnMin,
			int columnMax, int rowMin, int rowMax, int squareColumn, int squareRow) {
		int caseTestRow = square.getRow();
		int caseTestColumn = square.getColumn();
		if (((caseTestRow == rowMax && caseTestColumn == squareColumn)
				|| (caseTestRow == rowMin && caseTestColumn == squareColumn)
				|| (caseTestRow == squareRow && caseTestColumn == columnMin)
				|| (caseTestRow == squareRow && caseTestColumn == columnMax))
				&& !(caseTestRow == squareRow && caseTestColumn == squareColumn)) {
			squaresToCheck.add(square);
		}
		return squaresToCheck;

	}

	@Override
	public Square updateSquare(Square squareToUpdate, List<Square> squareToCheck) {
		squareToUpdate = checkAndUpdateInsects(squareToUpdate);
		squareToUpdate = checkTree(squareToUpdate, squareToCheck);
		squareToUpdate = checkShrub(squareToUpdate, squareToCheck);
		squareToUpdate = checkSprout(squareToUpdate, squareToCheck);
		return squareToUpdate;
	}
	
	public Square checkAndUpdateInsects(Square squareToUpdate) {
		if (squareToUpdate.getState() == State.INSECTS) {
			squareToUpdate.setState(State.VOID);
		}
		return squareToUpdate;
	}

	public Square updateTree(Square squareToUpdate, boolean checkInsects) {
		if (checkInsects) {
			squareToUpdate = setRandomInsects(squareToUpdate, 0.25);
		}
		return squareToUpdate;
	}

	public Square checkTree(Square squareToUpdate, List<Square> squareToCheck) {
		if (squareToUpdate.getState() == State.TREE) {
			boolean checkFire = checkInsects(squareToCheck);
			squareToUpdate = updateSprout(squareToUpdate, checkFire);
		}
		return squareToUpdate;
	}

	public Square updateShrub(Square squareToUpdate, boolean checkInsects) {
		if (checkInsects) {
			squareToUpdate = setRandomInsects(squareToUpdate, 0.5);
		}
		return squareToUpdate;
	}

	public Square checkShrub(Square squareToUpdate, List<Square> squareToCheck) {
		if (squareToUpdate.getState() == State.SHRUB) {
			boolean checkFire = checkInsects(squareToCheck);
			squareToUpdate = updateSprout(squareToUpdate, checkFire);
		}
		return squareToUpdate;
	}

	public Square updateSprout(Square squareToUpdate, boolean checkInsects) {
		if (checkInsects) {
			squareToUpdate = setRandomInsects(squareToUpdate, 0.75);
		}
		return squareToUpdate;
	}

	public Square checkSprout(Square squareToUpdate, List<Square> squareToCheck) {
		if (squareToUpdate.getState() == State.SPROUT) {
			boolean checkFire = checkInsects(squareToCheck);
			squareToUpdate = updateSprout(squareToUpdate, checkFire);
		}
		return squareToUpdate;
	}

	public boolean checkInsects(List<Square> squareToCheck) {
		boolean fire = false;
		for (int j = 0; j < squareToCheck.size(); j++) {
			if (squareToCheck.get(j).getState() == State.INSECTS) {
				fire = true;
			}
		}
		return fire;
	}

	public Square setRandomInsects(Square squareToUpdate, double percent) {
		double randomInsect = Math.random();
		if (randomInsect <= percent) {
			squareToUpdate.setState(State.INSECTS);
		}
		return squareToUpdate;
	}
	

}
