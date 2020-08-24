package test.jSim.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import fr.jSlim.models.algorithm.Updater;
import fr.jSlim.models.algorithm.UpdaterForest;

/**
 * @author GMassonnat
 *
 */
class UpdaterImplTest {


	@Test
	final void testGetColumnMin() {
		
		Updater updater = new UpdaterForest(9,9);
		updater.setColumns(10);
		int a, res;

		a = 1;
		res =1; 		
		assertTrue("limite min",updater.getColumnMin(a) == res);
		
		a = 1;
		res=0;
		assertFalse("limite min", updater.getColumnMin(a)==res);
		
		a = 10;
		res =9; 		
		assertTrue("a=9",updater.getColumnMin(a) == res);
		
		a = 3;
		res =2; 		
		assertTrue("a=3",updater.getColumnMin(a) == res);
	}

	@Test
	final void testGetColumnMax() {
		Updater updater = new UpdaterForest(9,9);
		updater.setColumns(10);
		int a,res;
		
		a=1;
		res=2;
		assertTrue("a=1", updater.getColumnMax(a)==res);
		
		a=9;
		res=10;
		assertTrue("a=9", updater.getColumnMax(a)==res);
		
		a=10;
		res=11;
		assertFalse("limite max", updater.getColumnMax(a)==res);
		
		a=10;
		res=10;
		assertTrue("limite max", updater.getColumnMax(a)==res);
		
			
		
		
	}

	@Test
	final void testGetRowMin() {
		Updater updater = new UpdaterForest(9,9);
		updater.setRows(10);
		int a, res;

		a = 1;
		res =1; 		
		assertTrue("limite min",updater.getRowMin(a) == res);
		
		a = 1;
		res=0;
		assertFalse("limite min", updater.getRowMin(a)==res);
		
		a = 10;
		res =9; 		
		assertTrue("a=9",updater.getRowMin(a) == res);
		
		a = 3;
		res =2; 		
		assertTrue("a=3",updater.getRowMin(a) == res);
	}

	@Test
	final void testGetRowMax() {
		Updater updater = new UpdaterForest(9,9);
		updater.setRows(10);
		int a,res;
		
		a=1;
		res=2;
		assertTrue("a=1", updater.getRowMax(a)==res);
		
		a=9;
		res=10;
		assertTrue("a=9", updater.getRowMax(a)==res);
		
		a=10;
		res=11;
		assertFalse("limite max", updater.getRowMax(a)==res);
		
		a=10;
		res=10;
		assertTrue("limite max", updater.getRowMax(a)==res);
	}

}
