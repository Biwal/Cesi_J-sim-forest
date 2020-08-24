package test.jSim.models;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.jSlim.models.algorithm.Updater;
import fr.jSlim.models.algorithm.UpdaterForest;
import fr.jSlim.models.cell.Square;
import fr.jSlim.models.cell.SquareImpl;
import fr.jSlim.models.enums.State;

class UpdaterForestTest {

	@Test
	final void testDetermineSquaresToCheck() {
		Updater updater = new UpdaterForest(9,9);
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
		squareToCheck = ((UpdaterForest)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(false));
		
		square.setColumn(3);
		square.setRow(6);
		squareToCheck.clear();
		squareToCheck = ((UpdaterForest)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(true));
		
		square.setColumn(5);
		square.setRow(6);
		squareToCheck.clear();
		squareToCheck = ((UpdaterForest)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(true));
		
		square.setColumn(4);
		square.setRow(7);
		squareToCheck.clear();
		squareToCheck = ((UpdaterForest)updater).determineSquaresToCheck(squareToCheck, square, columnMin, columnMax, rowMin, rowMax, squareColumn, squareRow);
		assertThat(squareToCheck.isEmpty(), is(false));		
	}

	@Test
	final void testUpdateSquare() {
		Updater updater = new UpdaterForest(9,9);
		Square squareb = new SquareImpl();
		
		List<Square> squaresList = new ArrayList<Square>();
		squareb.setState(State.TREE);
		for (int i = 0; i < 2; i++) {
			squaresList.add(squareb);
		}	
		
		State res;
		Square square = new SquareImpl();
		square.setState(State.VOID);
		res = State.SPROUT;
		square = ((UpdaterForest)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(res));
		
		square.setState(State.SPROUT);
		res = State.SHRUB;
		square = ((UpdaterForest)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(res));
		
		square.setState(State.VOID);
		res = State.SHRUB;
		square = ((UpdaterForest)updater).updateSquare(square, squaresList);
		assertThat(square.getState(), is(not(res)));
		
		square.setState(State.SPROUT);
		square = ((UpdaterForest)updater).updateSquare(square, squaresList);
		assertThat(square.isGrowthShrub(), is(not(true)));		
	}

	@Test
	final void testUpdateShrub() {
		Updater updater = new UpdaterForest(9,9);
		Square square = new SquareImpl();
		Boolean resBool;
		State resState;
		
		square.setGrowthShrub(false);
		resBool = true;
		assertTrue("set true",  ((UpdaterForest) updater).updateShrub(square).isGrowthShrub() == resBool);
		
		square.setGrowthShrub(false);
		resBool= false;
		assertFalse("must be true", ((UpdaterForest) updater).updateShrub(square).isGrowthShrub() == resBool);
		
		square.setGrowthShrub(true);
		resBool = false;
		assertTrue("setfalse", ((UpdaterForest) updater).updateShrub(square).isGrowthShrub() == resBool);
		
		square.setGrowthShrub(true);
		resBool = true;
		assertFalse("must be false", ((UpdaterForest) updater).updateShrub(square).isGrowthShrub() == resBool);
		
		square.setGrowthShrub(true);
		resState = State.TREE;
		assertTrue("must be 3", ((UpdaterForest) updater).updateShrub(square).getState() == resState);
	}

	@Test
	final void testCheckShrub() {
		Updater updater = new UpdaterForest(9,9);
		Square square = new SquareImpl();
		
		square.setState(State.SHRUB);
		square.setGrowthShrub(false);
		square = ((UpdaterForest) updater).checkShrub(square);
		
		assertThat(square.isGrowthShrub(), is(true));
		
		square.setState(State.SPROUT);
		square.setGrowthShrub(false);
		square = ((UpdaterForest) updater).checkShrub(square);
		assertThat(square.isGrowthShrub(), is(false));
	}

	@Test
	final void testUpdateSprout() {
		Updater updater = new UpdaterForest(9,9);
		Square square = new SquareImpl();
		int tree, shrub;
		
		State res = State.SHRUB;
		
		tree = 1;
		shrub = 2;
		square.setState(State.VOID);
		square = ((UpdaterForest) updater).updateSprout(square, tree, shrub);
		assertThat(square.getState(), is(res));
		
		tree = 1;
		shrub = 1;
		square.setState(State.VOID);
		square = ((UpdaterForest) updater).updateSprout(square, tree, shrub);
		assertThat(square.getState(), is(res));
		
		tree = 2;
		shrub = 2;
		square.setState(State.VOID);
		square = ((UpdaterForest) updater).updateSprout(square, tree, shrub);
		assertThat(square.getState(), is(not(res)));	
	}

	@Test
	final void testCheckSprout() {
		Updater updater = new UpdaterForest(9,9);
		Square square = new SquareImpl();
		
		Square squareTmp = new SquareImpl();
		List<Square> squaresList = new ArrayList<Square>();
		squareTmp.setState(State.SHRUB);
		squaresList.add(squareTmp);
		
		State res = State.SHRUB;
		
		square.setState(State.SPROUT);
		square= ((UpdaterForest)updater).checkSprout(square, squaresList);
		assertThat(square.getState(), is(res));
		
		square.setState(State.VOID);
		square= ((UpdaterForest)updater).checkSprout(square, squaresList);
		assertThat(square.getState(), is(not(res)));
		
	}

	@Test
	final void testCountShrubAndTree() {
		Updater updater = new UpdaterForest(9,9);
		List<Square> squaresList = new ArrayList<Square>();
		Square square = new SquareImpl();
		
		for (int i = 0; i < 5; i++) {
			squaresList.add(square);
		}
		Square squareB = new SquareImpl();
		squareB.setState(State.SHRUB);
		for (int i = 0; i < 4; i++) {
			squaresList.add(squareB);
		}
		Square squareC = new SquareImpl();
		squareC.setState(State.TREE);
		for (int i = 0; i < 3; i++) {
			squaresList.add(squareC);
		}
		
		List<Integer> listTest = new ArrayList<Integer>();		
		int resTree = 3;
		int resShrub = 4;
		
		listTest = ((UpdaterForest)updater).countShrubAndTree(squaresList);
		
		assertThat(listTest.get(1), is(resShrub));
		assertThat(listTest.get(0), is(resTree));
	
	}

	@Test
	final void testUpdateVoid() {
		Updater updater = new UpdaterForest(9,9);
		Square square = new SquareImpl();
		int tree, shrub;
		
		State res = State.SPROUT;
		
		tree = 3;
		shrub = 1;
		square = ((UpdaterForest)updater).updateVoid(square, tree, shrub);
		assertThat(square.getState(), is(res));

		tree = 1;
		shrub = 2;
		square.setState(State.INVALID);
		square = ((UpdaterForest)updater).updateVoid(square, tree, shrub);
		assertThat(square.getState(), is(res));

		tree = 0;
		shrub = 3;
		square.setState(State.INVALID);
		square = ((UpdaterForest)updater).updateVoid(square, tree, shrub);
		assertThat(square.getState(), is(res));
		
		tree = 1;
		shrub = 1;
		square.setState(State.INVALID);
		square = ((UpdaterForest)updater).updateVoid(square, tree, shrub);
		assertThat(square.getState(), is(not(res)));
		
		tree = 0;
		shrub = 2;
		square.setState(State.INVALID);
		square = ((UpdaterForest)updater).updateVoid(square, tree, shrub);
		assertThat(square.getState(), is(not(res)));
		
	}

	@Test
	final void testCheckVoid() {
		Updater updater = new UpdaterForest(9,9);
		Square square = new SquareImpl();
		
	
		List<Square> squaresList = new ArrayList<Square>();
		square.setState(State.SHRUB);
		for (int i = 0; i < 4; i++) {
			squaresList.add(square);
		}
		
		State res = State.SPROUT;
		
		Square squareB = new SquareImpl();
		squareB.setState(State.VOID);
		squareB= ((UpdaterForest)updater).checkVoid(squareB, squaresList);
		assertThat(squareB.getState(), is(res));
		
		Square squareC = new SquareImpl();
		squareC.setState(State.SHRUB);
		squareC= ((UpdaterForest)updater).checkVoid(squareC, squaresList);
		assertThat(squareC.getState(), is(not(res)));
	}

}
