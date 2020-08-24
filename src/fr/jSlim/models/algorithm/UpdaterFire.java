package fr.jSlim.models.algorithm;

import java.util.List;

import fr.jSlim.models.cell.Square;
import fr.jSlim.models.enums.State;

public class UpdaterFire extends UpdaterImpl {

	public UpdaterFire(int columns, int rows) {
	super.setColumns(columns);
	super.setRows(rows);
	
}
	@Override
	public List<Square> determineSquaresToCheck(List<Square> squaresToCheck, Square square, int columnMin,
			int columnMax, int rowMin, int rowMax, int squareColumn, int squareRow) {
		int caseTestRow = square.getRow();
		int caseTestColumn = square.getColumn();
		if (caseTestRow <= rowMax && caseTestRow >= rowMin && caseTestColumn >= columnMin && caseTestColumn <= columnMax
				&& !(caseTestRow == squareRow && caseTestColumn == squareColumn)) {
			squaresToCheck.add(square);
		}
		return squaresToCheck;
	}

	@Override
	public Square updateSquare(Square squareToUpdate, List<Square> squareToCheck) {
		squareToUpdate = checkAndUpdateAsh(squareToUpdate);
		squareToUpdate = checkAndUpdateFire(squareToUpdate);
		squareToUpdate = checkTree(squareToUpdate, squareToCheck);
		squareToUpdate = checkShrub(squareToUpdate, squareToCheck);
		squareToUpdate = checkSprout(squareToUpdate, squareToCheck);
		return squareToUpdate;
	}

	public Square checkAndUpdateFire(Square squareToUpdate) {
		if (squareToUpdate.getState() == State.FIRE) {
			squareToUpdate.setState(State.ASH);
		}
		return squareToUpdate;
	}

	public Square checkAndUpdateAsh(Square squareToUpdate) {
		if (squareToUpdate.getState() == State.ASH) {
			squareToUpdate.setState(State.VOID);
		}
		return squareToUpdate;
	}

	public Square updateTree(Square squareToUpdate, boolean checkFire) {
		if (checkFire) {
			squareToUpdate = setRandomFire(squareToUpdate, 0.75);
		}
		return squareToUpdate;
	}

	public Square checkTree(Square squareToUpdate, List<Square> squareToCheck) {
		if (squareToUpdate.getState() == State.TREE) {
			boolean checkFire = checkFire(squareToCheck);
			squareToUpdate = updateTree(squareToUpdate, checkFire);
		}
		return squareToUpdate;
	}

	public Square updateShrub(Square squareToUpdate, boolean checkFire) {
		if (checkFire) {
			squareToUpdate = setRandomFire(squareToUpdate, 0.5);
		}
		return squareToUpdate;
	}

	public Square checkShrub(Square squareToUpdate, List<Square> squareToCheck) {
		if (squareToUpdate.getState() == State.SHRUB) {
			boolean checkFire = checkFire(squareToCheck);
			squareToUpdate = updateShrub(squareToUpdate, checkFire);
		}
		return squareToUpdate;
	}

	public Square updateSprout(Square squareToUpdate, boolean checkFire) {
		if (checkFire) {
			squareToUpdate = setRandomFire(squareToUpdate, 0.25);
		}
		return squareToUpdate;
	}

	public Square checkSprout(Square squareToUpdate, List<Square> squareToCheck) {
		if (squareToUpdate.getState() == State.SPROUT) {
			boolean checkFire = checkFire(squareToCheck);
			squareToUpdate = updateSprout(squareToUpdate, checkFire);
		}
		return squareToUpdate;
	}

	public boolean checkFire(List<Square> squareToCheck) {
		boolean fire = false;
		for (int j = 0; j < squareToCheck.size(); j++) {
			if (squareToCheck.get(j).getState() == State.FIRE) {
				fire = true;
			}
		}
		return fire;
	}

	public Square setRandomFire(Square squareToUpdate, double percent) {
		double randomFire = Math.random();
		if (randomFire <= percent) {
			squareToUpdate.setState(State.FIRE);
		}
		return squareToUpdate;
	}

}
