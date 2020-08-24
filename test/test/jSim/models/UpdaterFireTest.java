package test.jSim.models;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsIn.isOneOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.jSlim.models.algorithm.Updater;
import fr.jSlim.models.algorithm.UpdaterFire;
import fr.jSlim.models.cell.Square;
import fr.jSlim.models.cell.SquareImpl;
import fr.jSlim.models.enums.State;

class UpdaterFireTest {

	@Test
	final void testDetermineSquaresToCheck() {
		Updater updater = new UpdaterFire(9,9);
		int columnMin = 2;
		int columnMax = 4;
		int rowMin = 5;
		int rowMax = 7;
		int squareColumn = 3;
		int squareRow = 6;
		
		List<Square> squareToCheck= new ArrayList<Square>();
		Square square = new SquareImpl();
		
		
		square.setColumn(2);
		square.setRow(6);
		squareToCheck = ((UpdaterFire)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(false));
		
		square.setColumn(3);
		square.setRow(6);
		squareToCheck.clear();
		squareToCheck = ((UpdaterFire)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(true));
		
		square.setColumn(5);
		square.setRow(6);
		squareToCheck.clear();
		squareToCheck = ((UpdaterFire)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(true));
		
		square.setColumn(4);
		square.setRow(7);
		squareToCheck.clear();
		squareToCheck = ((UpdaterFire)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(false));		
	}

	@Test
	final void testUpdateSquare() {
		Updater updater = new UpdaterFire(9,9);
		Square square = new SquareImpl();
		
		List<Square> squaresList = new ArrayList<Square>();
		square.setState(State.FIRE);
		squaresList.add(square);

		
		State res;
		
		square.setState(State.ASH);
		res = State.VOID;
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(res));
		
		square.setState(State.FIRE);
		res = State.ASH;
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(res));
		
		square.setState(State.TREE);
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), isOneOf(State.TREE, State.FIRE));
		
		square.setState(State.SHRUB);
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), isOneOf(State.SHRUB, State.FIRE));
		
		square.setState(State.SPROUT);
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), isOneOf(State.SPROUT, State.FIRE));
		
		square.setState(State.FIRE);
		res = State.VOID;
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));
		
		square.setState(State.TREE);
		res = State.ASH;
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));	
		
		square.setState(State.TREE);
		res = State.VOID;
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));	
		
		square.setState(State.SHRUB);
		res = State.ASH;
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));	
		
		square.setState(State.SHRUB);
		res = State.VOID;
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));
		
		square.setState(State.SPROUT);
		res = State.ASH;
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));	
		
		square.setState(State.SPROUT);
		res = State.VOID;
		square = ((UpdaterFire)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));
	}

	@Test
	final void testCheckAndUpdateFire() {
		Updater updater = new UpdaterFire(9,9);
		Square square = new SquareImpl();
		
		square.setState(State.FIRE);
		square = ((UpdaterFire)updater).checkAndUpdateFire(square);
		assertThat(square.getState(), is(State.ASH));
	}

	@Test
	final void testCheckAndUpdateAsh() {
		Updater updater = new UpdaterFire(9,9);
		Square square = new SquareImpl();
		
		square.setState(State.ASH);
		square = ((UpdaterFire)updater).checkAndUpdateAsh(square);
		assertThat(square.getState(), is(State.VOID));
	}

	@Test
	final void testUpdateTree() {
		int treesOnFire = 0;
		boolean checkFire = false;
		
		for (int i = 0; i < 2500 ; i++) {
			Updater updater = new UpdaterFire(9,9);
			Square squareFalse = new SquareImpl();
			Square square = new SquareImpl();
			
			square.setState(State.TREE);
			square = ((UpdaterFire)updater).updateTree(square,true);
			if(square.getState() == State.FIRE) {
				treesOnFire = treesOnFire +1;
			}

			squareFalse.setState(State.TREE);
			squareFalse= ((UpdaterFire)updater).updateTree(squareFalse, false);
			if (squareFalse.getState() == State.FIRE) {
				checkFire = true;
			}
		}
		assertThat(treesOnFire, greaterThan(1750));
		assertThat(treesOnFire, lessThan(2000));
		
		assertThat(checkFire, is(false));		
	}

	@Test
	final void testCheckTree() {
		Updater updater = new UpdaterFire(9,9);
		Square square = new SquareImpl();
		List<Square> squaresList = new ArrayList<Square>();
		square.setState(State.FIRE);
		squaresList.add(square);
		square.setState(State.TREE);
		
		square = ((UpdaterFire)updater).checkTree(square, squaresList);
		assertThat(square.getState(), isOneOf(State.TREE, State.FIRE));
	}

	@Test
	final void testUpdateShrub() {
		int shrubsOnFire = 0;
		boolean checkFire = false;
		
		for (int i = 0; i < 2500 ; i++) {
			Updater updater = new UpdaterFire(9,9);
			Square squareFalse = new SquareImpl();
			Square square = new SquareImpl();
			
			square.setState(State.SHRUB);
			square = ((UpdaterFire)updater).updateShrub(square,true);
			if(square.getState() == State.FIRE) {
				shrubsOnFire = shrubsOnFire +1;
			}

			squareFalse.setState(State.SHRUB);
			squareFalse= ((UpdaterFire)updater).updateShrub(squareFalse, false);
			if (squareFalse.getState() == State.FIRE) {
				checkFire = true;
			}
		}
		assertThat(shrubsOnFire, greaterThan(1125));
		assertThat(shrubsOnFire, lessThan(1375));
		assertThat(checkFire, is(false));		
	}

	@Test
	final void testCheckShrub() {
		Updater updater = new UpdaterFire(9,9);
		Square square = new SquareImpl();
		List<Square> squaresList = new ArrayList<Square>();
		square.setState(State.FIRE);
		squaresList.add(square);
		square.setState(State.SHRUB);
		
		square = ((UpdaterFire)updater).checkShrub(square, squaresList);
		assertThat(square.getState(), isOneOf(State.SHRUB, State.FIRE));
	}

	@Test
	final void testUpdateSprout() {
		int sproutsOnFire = 0;
		boolean checkFire = false;
		
		for (int i = 0; i < 2500 ; i++) {
			Updater updater = new UpdaterFire(9,9);
			Square squareFalse = new SquareImpl();
			Square square = new SquareImpl();
			
			square.setState(State.SPROUT);
			square = ((UpdaterFire)updater).updateSprout(square,true);
			if(square.getState() == State.FIRE) {
				sproutsOnFire = sproutsOnFire +1;
			}

			squareFalse.setState(State.SPROUT);
			squareFalse= ((UpdaterFire)updater).updateSprout(squareFalse, false);
			if (squareFalse.getState() == State.FIRE) {
				checkFire = true;
			}
		}
		assertThat(sproutsOnFire, greaterThan(500));
		assertThat(sproutsOnFire, lessThan(750));
		assertThat(checkFire, is(false));		
	}

	@Test
	final void testCheckSprout() {
		Updater updater = new UpdaterFire(9,9);
		Square square = new SquareImpl();
		List<Square> squaresList = new ArrayList<Square>();
		square.setState(State.FIRE);
		squaresList.add(square);
		square.setState(State.SPROUT);
		
		square = ((UpdaterFire)updater).checkSprout(square, squaresList);
		assertThat(square.getState(), isOneOf(State.SPROUT, State.FIRE));
	}

	@Test
	final void testCheckFire() {
		Updater updater = new UpdaterFire(9,9);
		Square square = new SquareImpl();
		List<Square> squaresList = new ArrayList<Square>();
		boolean fire;
		
		fire = ((UpdaterFire)updater).checkFire(squaresList);
		assertThat(fire, is(false));
		
		square.setState(State.FIRE);
		squaresList.add(square);
		fire = ((UpdaterFire)updater).checkFire(squaresList);
		assertThat(fire, is(true));
		
		Square squareB = new SquareImpl();
		squareB.setState(State.ASH);
		squaresList.add(squareB);
		fire = ((UpdaterFire)updater).checkFire(squaresList);
		assertThat(fire, is(true));
		
		squaresList.remove(0);
		fire = ((UpdaterFire)updater).checkFire(squaresList);
		assertThat(fire, is(false));
		
		
	}

	@Test
	final void testSetRandomFire() {
		int shrubsOnFire = 0;
		boolean checkZeroFire = false;
		boolean checkOnlyFire = true;
		
		for (int i = 0; i < 2500 ; i++) {
			Updater updater = new UpdaterFire(9,9);
			Square squareFalse = new SquareImpl();
			Square squareTrue = new SquareImpl();
			Square square = new SquareImpl();
			
			square.setState(State.SHRUB);
			square = ((UpdaterFire)updater).setRandomFire(square, 0.5);
			if(square.getState() == State.FIRE) {
				shrubsOnFire = shrubsOnFire +1;
			}

			squareFalse.setState(State.SHRUB);
			squareFalse= ((UpdaterFire)updater).setRandomFire(squareFalse, 0.0);
			if (squareFalse.getState() == State.FIRE) {
				checkZeroFire = true;
			}
			
			squareTrue.setState(State.SHRUB);
			squareTrue= ((UpdaterFire)updater).setRandomFire(squareTrue, 1);
			if (squareTrue.getState() != State.FIRE) {
				checkOnlyFire = false;
			}
		}
		assertThat(shrubsOnFire, greaterThan(1125));
		assertThat(shrubsOnFire, lessThan(1375));
		assertThat(checkZeroFire, is(false));		
		assertThat(checkOnlyFire, is(true));
	}

}
