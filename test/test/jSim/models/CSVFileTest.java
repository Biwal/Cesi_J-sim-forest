package test.jSim.models;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.jSlim.models.cell.Square;
import fr.jSlim.models.cell.SquareImpl;
import fr.jSlim.models.enums.State;
import fr.jSlim.models.grid.CSVFile;
import fr.jSlim.models.grid.Grid;
import fr.jSlim.models.grid.GridImpl;

class CSVFileTest {

	
	@Test
	void testAddOneRow() throws IOException {
		Grid grid = new GridImpl(9,9);
		CSVFile csvFile = new CSVFile(grid);
		
		csvFile.addOneRow();
		assertThat(csvFile.getContentCSV().size(),is(1));
	}

	@Test
	void testConvertCounter() {
		Grid grid = null;
		grid = new GridImpl(9,9);
		CSVFile csvFile = new CSVFile(grid);
		
		double counter = 12;
		String res = "0,148";
		assertThat(csvFile.convertCounter(counter),is(res));
	}

	@Test
	void testIncrementCounter() {
		Grid grid = null;
		grid = new GridImpl(9,9);
		CSVFile csvFile = new CSVFile(grid);
		
		double counter = 50;
		State state = State.TREE;
		Square square = new SquareImpl();
		square.setState(State.TREE);
		
		double res = 51;
		assertThat(csvFile.incrementCounter(counter, state, square),is(res));
	}

	

}
