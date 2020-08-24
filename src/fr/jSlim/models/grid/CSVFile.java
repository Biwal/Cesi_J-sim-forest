package fr.jSlim.models.grid;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

import fr.jSlim.models.cell.Square;
import fr.jSlim.models.enums.State;

public class CSVFile {

	private Grid grid;
	private String url;
	private List<String[]> contentCSV;
	private CSVWriter writer;

	public CSVFile(Grid grid) {
		this.grid = grid;
		this.contentCSV = new ArrayList<String[]>();
	}


	public void initCSV() throws FileNotFoundException {
		contentCSV.add(new String[] { "vide", "jeune pousse", "arbuste", "arbre", "en feu", "en cendre", "infection" });
	}

	public void addOneRow() {
		double treeCounter = 0;
		double voidCounter = 0;
		double sproutCounter = 0;
		double shrubCounter = 0;
		double fireCounter = 0;
		double ashCounter = 0;
		double insectsCounter = 0;
		for (int i = 0; i < this.grid.getSquareGrid().size(); i++) {
			Square tmp = this.grid.getSquareGrid().get(i);
			treeCounter = incrementCounter(treeCounter,State.TREE,tmp);
			voidCounter = incrementCounter(voidCounter,State.VOID,tmp);
			sproutCounter = incrementCounter(sproutCounter,State.SPROUT,tmp);
			shrubCounter = incrementCounter(shrubCounter,State.SHRUB,tmp);
			fireCounter = incrementCounter(fireCounter,State.FIRE,tmp);
			ashCounter = incrementCounter(ashCounter,State.ASH,tmp);
			insectsCounter = incrementCounter(insectsCounter,State.INSECTS,tmp);
		}
		contentCSV.add(new String[] { convertCounter(voidCounter), convertCounter(sproutCounter),
				convertCounter(shrubCounter), convertCounter(treeCounter), convertCounter(fireCounter),
				convertCounter(ashCounter), convertCounter(insectsCounter) });
	}

	public String convertCounter(double counter) {
		DecimalFormat df = new DecimalFormat("##.###");
		df.setRoundingMode(RoundingMode.DOWN);
		return df.format(counter / this.grid.getSquareGrid().size());
	}
	public double incrementCounter(double counter, State state, Square square) {
		if(square.getState() == state) {
			counter++;
		}
		return counter;
	}

	public void exportCSV() throws IOException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		this.setWriter(new CSVWriter(
				new OutputStreamWriter(
						new FileOutputStream(
								url  +"\\Export-j-sim-forest-"+ dtf.format(now) + ".csv"),
						StandardCharsets.UTF_8),
				';', CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
				CSVWriter.DEFAULT_LINE_END));
		for (String strings[] : contentCSV) {
			writer.writeNext(strings);
		}
		writer.close();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public List<String[]> getContentCSV() {
		return contentCSV;
	}

	public void setContentCSV(List<String[]> contentCSV) {
		this.contentCSV = contentCSV;
	}

	public CSVWriter getWriter() {
		return writer;
	}

	public void setWriter(CSVWriter writer) {
		this.writer = writer;
	}
}
