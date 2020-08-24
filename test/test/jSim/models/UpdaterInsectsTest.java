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
import fr.jSlim.models.algorithm.UpdaterInsects;
import fr.jSlim.models.cell.Square;
import fr.jSlim.models.cell.SquareImpl;
import fr.jSlim.models.enums.State;

class UpdaterInsectsTest {

	@Test
	final void testDetermineSquaresToCheck() {
		Updater updater = new UpdaterInsects(9,9);
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
		squareToCheck = ((UpdaterInsects)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(false));
		
		square.setColumn(3);
		square.setRow(6);
		squareToCheck.clear();
		squareToCheck = ((UpdaterInsects)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(true));
		
		square.setColumn(5);
		square.setRow(6);
		squareToCheck.clear();
		squareToCheck = ((UpdaterInsects)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(true));
		
		square.setColumn(4);
		square.setRow(7);
		squareToCheck.clear();
		squareToCheck = ((UpdaterInsects)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(true));	
		
		square.setColumn(3);
		square.setRow(7);
		squareToCheck.clear();
		squareToCheck = ((UpdaterInsects)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(false));	
	}

	@Test
	final void testUpdateSquare() {
		Updater updater = new UpdaterInsects(9,9);
		Square square = new SquareImpl();
		
		List<Square> squaresList = new ArrayList<Square>();
		square.setState(State.INSECTS);
		squaresList.add(square);
		
		State res;
		
		square.setState(State.INSECTS);
		res = State.VOID;
		square = ((UpdaterInsects)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(res));
		
		square.setState(State.TREE);
		square = ((UpdaterInsects)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), isOneOf(State.TREE, State.INSECTS));
		
		square.setState(State.SHRUB);
		square = ((UpdaterInsects)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), isOneOf(State.SHRUB, State.INSECTS));
		
		square.setState(State.SPROUT);
		square = ((UpdaterInsects)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), isOneOf(State.SPROUT, State.INSECTS));
		
		square.setState(State.TREE);
		res = State.VOID;
		square = ((UpdaterInsects)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));	
		
		square.setState(State.SHRUB);
		res = State.VOID;
		square = ((UpdaterInsects)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));
		
		square.setState(State.SPROUT);
		res = State.VOID;
		square = ((UpdaterInsects)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));
	}

	@Test
	final void testCheckAndUpdateInsects() {
		Updater updater = new UpdaterInsects(9,9);
		Square square = new SquareImpl();
		
		square.setState(State.INSECTS);
		square = ((UpdaterInsects)updater).checkAndUpdateInsects(square);
		assertThat(square.getState(), is(State.VOID));
	}

	@Test
	final void testUpdateTree() {
		int treesInfected = 0;
		boolean checkInsects = false;
		
		for (int i = 0; i < 2500 ; i++) {
			Updater updater = new UpdaterInsects(9,9);
			Square squareFalse = new SquareImpl();
			Square square = new SquareImpl();
			
			square.setState(State.TREE);
			square = ((UpdaterInsects)updater).updateTree(square,true);
			if(square.getState() == State.INSECTS) {
				treesInfected = treesInfected +1;
			}

			squareFalse.setState(State.TREE);
			squareFalse= ((UpdaterInsects)updater).updateTree(squareFalse, false);
			if (squareFalse.getState() == State.INSECTS) {
				checkInsects = true;
			}
		}
		assertThat(treesInfected, greaterThan(500));
		assertThat(treesInfected, lessThan(750));
		
		assertThat(checkInsects, is(false));		
	}

	@Test
	final void testCheckTree() {
		Updater updater = new UpdaterInsects(9,9);
		Square square = new SquareImpl();
		List<Square> squaresList = new ArrayList<Square>();
		square.setState(State.INSECTS);
		squaresList.add(square);
		square.setState(State.TREE);
		
		square = ((UpdaterInsects)updater).checkTree(square, squaresList);
		assertThat(square.getState(), isOneOf(State.TREE, State.INSECTS));
	}

	@Test
	final void testUpdateShrub() {
		int shrubsInfected = 0;
		boolean checkInsects = false;
		
		for (int i = 0; i < 2500 ; i++) {
			Updater updater = new UpdaterInsects(9,9);
			Square squareFalse = new SquareImpl();
			Square square = new SquareImpl();
			
			square.setState(State.SHRUB);
			square = ((UpdaterInsects)updater).updateShrub(square,true);
			if(square.getState() == State.INSECTS) {
				shrubsInfected = shrubsInfected +1;
			}

			squareFalse.setState(State.SHRUB);
			squareFalse= ((UpdaterInsects)updater).updateShrub(squareFalse, false);
			if (squareFalse.getState() == State.INSECTS) {
				checkInsects = true;
			}
		}
		assertThat(shrubsInfected, greaterThan(1125));
		assertThat(shrubsInfected, lessThan(1375));
		
		assertThat(checkInsects, is(false));		
	}

	@Test
	final void testCheckShrub() {
		Updater updater = new UpdaterInsects(9,9);
		Square square = new SquareImpl();
		List<Square> squaresList = new ArrayList<Square>();
		square.setState(State.INSECTS);
		squaresList.add(square);
		square.setState(State.SHRUB);
		
		square = ((UpdaterInsects)updater).checkShrub(square, squaresList);
		assertThat(square.getState(), isOneOf(State.SHRUB, State.INSECTS));
	}

	@Test
	final void testUpdateSprout() {
		int sproutsInfected = 0;
		boolean checkInsects = false;
		
		for (int i = 0; i < 2500 ; i++) {
			Updater updater = new UpdaterInsects(9,9);
			Square squareFalse = new SquareImpl();
			Square square = new SquareImpl();
			
			square.setState(State.SPROUT);
			square = ((UpdaterInsects)updater).updateSprout(square,true);
			if(square.getState() == State.INSECTS) {
				sproutsInfected = sproutsInfected +1;
			}

			squareFalse.setState(State.SPROUT);
			squareFalse= ((UpdaterInsects)updater).updateSprout(squareFalse, false);
			if (squareFalse.getState() == State.INSECTS) {
				checkInsects = true;
			}
		}
		assertThat(sproutsInfected, greaterThan(1750));
		assertThat(sproutsInfected, lessThan(2000));
		
		assertThat(checkInsects, is(false));	
	}

	@Test
	final void testCheckSprout() {
		Updater updater = new UpdaterInsects(9,9);
		Square square = new SquareImpl();
		List<Square> squaresList = new ArrayList<Square>();
		square.setState(State.INSECTS);
		squaresList.add(square);
		square.setState(State.SPROUT);
		
		square = ((UpdaterInsects)updater).checkSprout(square, squaresList);
		assertThat(square.getState(), isOneOf(State.SPROUT, State.INSECTS));
	}

	@Test
	final void testCheckInsects() {
		Updater updater = new UpdaterInsects(9,9);
		Square square = new SquareImpl();
		List<Square> squaresList = new ArrayList<Square>();
		boolean fire;
		
		fire = ((UpdaterInsects)updater).checkInsects(squaresList);
		assertThat(fire, is(false));
		
		square.setState(State.INSECTS);
		squaresList.add(square);
		fire = ((UpdaterInsects)updater).checkInsects(squaresList);
		assertThat(fire, is(true));
		
		
		Square squareX = new SquareImpl();
		squareX.setState(State.ASH);
		squaresList.add(squareX);
		fire = ((UpdaterInsects)updater).checkInsects(squaresList);
		assertThat(fire, is(true));
		
		
		
		squaresList.remove(0);
		fire = ((UpdaterInsects)updater).checkInsects(squaresList);
		assertThat(fire, is(false));
	}

	@Test
	final void testSetRandomInsects() {
		int shrubsInfected = 0;
		boolean checkZeroInfection = false;
		boolean checkOnlyInfection = true;
		
		for (int i = 0; i < 2500 ; i++) {
			Updater updater = new UpdaterInsects(9,9);
			Square squareFalse = new SquareImpl();
			Square squareTrue = new SquareImpl();
			Square square = new SquareImpl();
			
			square.setState(State.SHRUB);
			square = ((UpdaterInsects)updater).setRandomInsects(square, 0.5);
			if(square.getState() == State.INSECTS) {
				shrubsInfected = shrubsInfected +1;
			}

			squareFalse.setState(State.SHRUB);
			squareFalse= ((UpdaterInsects)updater).setRandomInsects(squareFalse, 0.0);
			if (squareFalse.getState() == State.INSECTS) {
				checkZeroInfection = true;
			}
			
			squareTrue.setState(State.SHRUB);
			squareTrue= ((UpdaterInsects)updater).setRandomInsects(squareTrue, 1);
			if (squareTrue.getState() != State.INSECTS) {
				checkOnlyInfection = false;
			}
		}
		assertThat(shrubsInfected, greaterThan(1125));
		assertThat(shrubsInfected, lessThan(1375));
		assertThat(checkZeroInfection, is(false));		
		assertThat(checkOnlyInfection, is(true));
	}

}
