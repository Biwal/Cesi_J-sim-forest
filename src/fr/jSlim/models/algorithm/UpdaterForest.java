package fr.jSlim.models.algorithm;

import java.util.ArrayList;
import java.util.List;

import fr.jSlim.models.cell.Square;
import fr.jSlim.models.enums.State;

public class UpdaterForest extends UpdaterImpl {

	public UpdaterForest(int columns, int rows) {
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
		// L'ordre des fonctions est important
		squareToUpdate = checkShrub(squareToUpdate);
		squareToUpdate = checkSprout(squareToUpdate, squareToCheck);
		squareToUpdate = checkVoid(squareToUpdate, squareToCheck);
		return squareToUpdate;
	}

	public Square updateShrub(Square squareToUpdate) {
		if (squareToUpdate.isGrowthShrub()) {
			squareToUpdate.setState(State.TREE);
			squareToUpdate.setGrowthShrub(false);
		} else {
			squareToUpdate.setGrowthShrub(true);
		}
		return squareToUpdate;
	}

	public Square checkShrub(Square squareToUpdate) {
		if (squareToUpdate.getState() == State.SHRUB) {
			updateShrub(squareToUpdate);
		}
		return squareToUpdate;
	}

	public Square updateSprout(Square squareToUpdate, int tree, int shrub) {
		if ((shrub + tree) <= 3) {
			squareToUpdate.setState(State.SHRUB);
		}
		return squareToUpdate;
	}

	public Square checkSprout(Square squareToUpdate, List<Square> squareToCheck) {
		if (squareToUpdate.getState() == State.SPROUT) {
			List<Integer> numberShrubAndTree = countShrubAndTree(squareToCheck);
			squareToUpdate = updateSprout(squareToUpdate, numberShrubAndTree.get(0), numberShrubAndTree.get(1));
		}
		return squareToUpdate;
	}

	public List<Integer> countShrubAndTree(List<Square> squareToCheck) {
		List<Integer> numberShrubAndTree = new ArrayList<Integer>();
		int shrub = 0;
		int tree = 0;
		for (int j = 0; j < squareToCheck.size(); j++) {
			if (squareToCheck.get(j).getState() == State.SHRUB) {
				shrub = shrub + 1;
			} else if (squareToCheck.get(j).getState() == State.TREE) {
				tree = tree + 1;
			}
		}
		numberShrubAndTree.add(tree);
		numberShrubAndTree.add(shrub);
		return numberShrubAndTree;
	}

	public Square updateVoid(Square squareToUpdate, int tree, int shrub) {
		if (shrub >= 3) {
			squareToUpdate.setState(State.SPROUT);
		} else if (tree >= 2) {
			squareToUpdate.setState(State.SPROUT);
		} else if (tree == 1 && shrub == 2) {
			squareToUpdate.setState(State.SPROUT);
		}
		return squareToUpdate;
	}

	public Square checkVoid(Square squareToUpdate, List<Square> squareToCheck) {
		if (squareToUpdate.getState() == State.VOID) {
			List<Integer> numberShrubAndTree = countShrubAndTree(squareToCheck);
			squareToUpdate = updateVoid(squareToUpdate, numberShrubAndTree.get(0), numberShrubAndTree.get(1));
		}
		return squareToUpdate;
	}
}
